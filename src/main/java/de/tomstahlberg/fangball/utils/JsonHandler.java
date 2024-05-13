package de.tomstahlberg.fangball.utils;

import de.tomstahlberg.fangball.FangBall;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class JsonHandler {
    public static JSONObject serializeLivingEntity(LivingEntity entity) throws IOException {
        // Create new JSON
        JSONObject json = new JSONObject();

        // Some basic properties
        json.put("type", entity.getType().name());
        json.put("uuid", entity.getUniqueId().toString());

        // If entity is tamed
        if(entity instanceof Tameable){
            if(((Tameable) entity).isTamed()){
                json.put("is_tamed", true);
            }
        }

        // If Entity has inventory (e.g. donkey)
        if(entity instanceof ChestedHorse){
            if(((ChestedHorse) entity).isCarryingChest()){
                List<byte[]> itemsList = new ArrayList<>();
                ChestedHorse chestedHorse = (ChestedHorse) entity;
                for(int i = 0; i<chestedHorse.getInventory().getSize();i++){
                    Inventory inventory = chestedHorse.getInventory();
                    if(inventory.getItem(i) == null){
                        //serializiere ein Item mit besonderer verfickten Gabe meine Fresse
                        itemsList.add(stupidItem().serializeAsBytes());
                    }else{
                        itemsList.add(inventory.getItem(i).serializeAsBytes());
                    }
                }
                JSONArray jsonArray = new JSONArray();
                for(byte[] bytes : itemsList){
                    String base64String = Base64.getEncoder().encodeToString(bytes);
                    jsonArray.put(base64String);
                }
                json.put("inventory", jsonArray);
            }else{
                ChestedHorse chestedHorse = (ChestedHorse) entity;
                if(chestedHorse.getInventory().getItem(0) != null){
                    if(chestedHorse.getInventory().getItem(0).getType() == Material.SADDLE){
                        json.put("saddled","true");
                    }
                }
            }
        }

        // If entity can be saddled
        if(entity instanceof Steerable){
            Steerable steerable = (Steerable) entity;
            if(steerable.hasSaddle()){
                json.put("saddled","true");
            }
        }

        // If entity has equipment, get equipment
        if(entity.getEquipment() != null){
            EntityEquipment entityEquipment = entity.getEquipment();
            ItemStack helmet = entityEquipment.getHelmet();
            if(helmet != null && helmet.getType() != Material.AIR){
                json.put("helmet", ItemSerializationHandler.getBase64String(helmet));
                json.put("helmetDropChance", entityEquipment.getHelmetDropChance());
            }
            ItemStack chestPlate = entityEquipment.getChestplate();
            if(chestPlate != null && chestPlate.getType() != Material.AIR){
                json.put("chestPlate", ItemSerializationHandler.getBase64String(chestPlate));
                json.put("chestPlateDropChance", entityEquipment.getChestplateDropChance());
            }
            ItemStack leggings = entityEquipment.getLeggings();
            if(leggings != null && leggings.getType() != Material.AIR){
                json.put("leggings", ItemSerializationHandler.getBase64String(leggings));
                json.put("leggingsDropChance", entityEquipment.getLeggingsDropChance());
            }
            ItemStack boots = entityEquipment.getBoots();
            if(boots != null && boots.getType() != Material.AIR){
                json.put("boots", ItemSerializationHandler.getBase64String(boots));
                json.put("bootsDropChance", entityEquipment.getBootsDropChance());
            }
            ItemStack itemMainHand = entityEquipment.getItemInMainHand();
            if(itemMainHand != null && itemMainHand.getType() != Material.AIR){
                json.put("itemMainHand", ItemSerializationHandler.getBase64String(itemMainHand));
                json.put("itemMainHandDropChance", entityEquipment.getItemInMainHandDropChance());
            }
            ItemStack itemOffHand = entityEquipment.getItemInOffHand();
            if(itemOffHand != null && itemOffHand.getType() != Material.AIR){
                json.put("itemOffHand",ItemSerializationHandler.getBase64String(itemOffHand));
                json.put("itemOffHandDropChance", entityEquipment.getItemInOffHandDropChance());
            }
        }

        // Some other basic properties
        //json.put("display_name", entity.getCustomName()); @deprecated
        json.put("display_name", ComponentHandler.getDisplayName(entity));
        json.put("health", entity.getHealth());
        json.put("is_dead", entity.isDead());
        json.put("is_glowing", entity.isGlowing());
        json.put("is_silent", entity.isSilent());

        // Get all entity attributes
        if(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null){
            json.put("max_health", entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_ARMOR) != null){
            json.put("armor", entity.getAttribute(Attribute.GENERIC_ARMOR).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS) != null){
            json.put("armor_toughness", entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null){
            json.put("attack_damage", entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK) != null){
            json.put("attack_knockback", entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null){
            json.put("attack_speed", entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE) != null){
            json.put("follow_range", entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null){
            json.put("knockback_resistance", entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_LUCK) != null){
            json.put("luck", entity.getAttribute(Attribute.GENERIC_LUCK).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_MAX_ABSORPTION) != null){
            json.put("max_absorption", entity.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null){
            json.put("movement_speed", entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
        }
        /*if(entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH) != null){
            json.put("horse_jump_strength", entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue());
        }@removed */
        if(entity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH) != null){
            json.put("jump_strength", entity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).getValue());
        }
        if(entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) != null){
            json.put("zombie_spawn_reinforcements", entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).getValue());
        }
        // added in 1.20.6
        if(entity.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER) != null){
            json.put("fall_damage_multiplier", entity.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_GRAVITY) != null){
            json.put("gravity", entity.getAttribute(Attribute.GENERIC_GRAVITY).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE) != null){
            json.put("save_fall_distance", entity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_SCALE) != null){
            json.put("scale", entity.getAttribute(Attribute.GENERIC_SCALE).getValue());
        }
        if(entity.getAttribute(Attribute.GENERIC_STEP_HEIGHT) != null){
            json.put("step_height", entity.getAttribute(Attribute.GENERIC_STEP_HEIGHT).getValue());
        }

        // If entity has AI
        json.put("ai_status", entity.hasAI());

        // If entity is adult or baby
        if(entity instanceof Ageable){
            Ageable ageable = (Ageable) entity;
            json.put("is_adult", ageable.isAdult());
        }
        // If entity is sheep, save color and sheared status
        if(entity instanceof Sheep){
            Sheep sheep = (Sheep) entity;
            json.put("sheep_color", sheep.getColor().toString());
            json.put("sheep_sheared", sheep.isSheared());
        }

        // If entity has potion effects
        JSONArray effectsJson = new JSONArray();
        Collection<PotionEffect> effects = entity.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            JSONObject effectJson = new JSONObject();
            effectJson.put("type", effect.getType().getName());
            effectJson.put("duration", effect.getDuration());
            effectJson.put("amplifier", effect.getAmplifier());
            effectsJson.put(effectJson);
        }
        json.put("potion_effects", effectsJson);



        // Save JSON
        return json;
    }

    public static LivingEntity deserializeLivingEntity(JSONObject json, Location location, World world, Plugin plugin) throws IOException {
        // Create and spawn entity
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf((String) json.get("type")));

        // Get and Set entity is tamed
        if(json.has("is_tamed")){
            Tameable tameable = (Tameable) entity;
            tameable.setTamed(json.getBoolean("is_tamed"));
        }

        // Get and Set entity`s inventory (e.g. Donkey)
        if(json.has("inventory")){
            ChestedHorse chestedHorse = (ChestedHorse) entity;
            chestedHorse.setCarryingChest(true);
            JSONArray jsonArray = json.getJSONArray("inventory");
            List<byte[]> itemsList = new ArrayList<>();

            for(int i = 0;i<jsonArray.length();i++){
                String base64String = jsonArray.getString(i);
                byte[] bytes = Base64.getDecoder().decode(base64String);
                itemsList.add(bytes);
            }

            for(int i = 0;i<chestedHorse.getInventory().getSize();i++){
                byte[] serializedItem = itemsList.get(i);
                ItemStack itemStack = ItemStack.deserializeBytes(serializedItem);
                if(isStupidItem(itemStack))
                    continue;

                chestedHorse.getInventory().setItem(i, itemStack);

            }
        }

        // Get and Set entity is saddled
        if(json.has("saddled")){
            if(entity instanceof ChestedHorse){
                ChestedHorse chestedHorse = (ChestedHorse) entity;
                chestedHorse.getInventory().setItem(0, new ItemStack(Material.SADDLE));
            }else if (entity instanceof Steerable){
                Steerable steerable = (Steerable) entity;
                steerable.setSaddle(true);
            }
        }

        // Get and Set entity`s equipment if accessible
        if(json.has("helmet")){
            entity.getEquipment().setHelmet(ItemSerializationHandler.getItemStackFromBase64String(json.getString("helmet")));
            entity.getEquipment().setHelmetDropChance(json.getFloat("helmetDropChance"));
        }
        if(json.has("chestPlate")){
            entity.getEquipment().setChestplate(ItemSerializationHandler.getItemStackFromBase64String(json.getString("chestPlate")));
            entity.getEquipment().setChestplateDropChance(json.getFloat("chestPlateDropChance"));
        }
        if(json.has("leggings")){
            entity.getEquipment().setLeggings(ItemSerializationHandler.getItemStackFromBase64String(json.getString("leggings")));
            entity.getEquipment().setLeggingsDropChance(json.getFloat("leggingsDropChance"));
        }
        if(json.has("boots")){
            entity.getEquipment().setBoots(ItemSerializationHandler.getItemStackFromBase64String(json.getString("boots")));
            entity.getEquipment().setBootsDropChance(json.getFloat("bootsDropChance"));
        }
        if(json.has("itemMainHand")){
            entity.getEquipment().setItemInMainHand(ItemSerializationHandler.getItemStackFromBase64String(json.getString("itemMainHand")));
            entity.getEquipment().setItemInMainHandDropChance(json.getFloat("itemMainHandDropChance"));
        }
        if(json.has("itemOffHand")){
            entity.getEquipment().setItemInOffHand(ItemSerializationHandler.getItemStackFromBase64String(json.getString("itemOffHand")));
            entity.getEquipment().setItemInOffHandDropChance(json.getFloat("itemOffHandDropChance"));
        }

        // Get and Set some basic properties
        if(json.has("display_name")){
            //entity.setCustomName((String) json.get("display_name")); @deprecated
            ComponentHandler.setDisplayName(entity, json.getString("display_name"));
        }
        entity.setGlowing((boolean) json.get("is_glowing"));
        entity.setSilent((boolean) json.get("is_silent"));

        // Get and Set entity attributes
        AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null && json.has("max_health")) {
            Double value;
            if(json.get("max_health") instanceof Double){
                value = json.getDouble("max_health");
            }else{
               value = Double.valueOf(json.get("max_health").toString()) ;
            }
            maxHealthAttribute.setBaseValue(value);

        }
        AttributeInstance maxArmorAttribute = entity.getAttribute(Attribute.GENERIC_ARMOR);
        if (maxArmorAttribute != null && json.has("armor")) {
            Double value;
            if(json.get("armor") instanceof Double){
                value = json.getDouble("armor");
            }else{
                value = Double.valueOf(json.get("armor").toString()) ;
            }
            maxArmorAttribute.setBaseValue(value);
        }
        AttributeInstance maxArmorToughnessAttribute = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        if (maxArmorToughnessAttribute != null && json.has("armor_toughness")) {
            Double value;
            if(json.get("armor_toughness") instanceof Double){
                value = json.getDouble("armor_toughness");
            }else{
                value = Double.valueOf(json.get("armor_toughness").toString()) ;
            }
            maxArmorToughnessAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackDamageAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (maxAttackDamageAttribute != null && json.has("attack_damage")) {
            Double value;
            if(json.get("attack_damage") instanceof Double){
                value = json.getDouble("attack_damage");
            }else{
                value = Double.valueOf(json.get("attack_damage").toString()) ;
            }
            maxAttackDamageAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackKnockbackAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK);
        if (maxAttackKnockbackAttribute != null && json.has("attack_knockback")) {
            Double value;
            if(json.get("attack_knockback") instanceof Double){
                value = json.getDouble("attack_knockback");
            }else{
                value = Double.valueOf(json.get("attack_knockback").toString()) ;
            }
            maxAttackKnockbackAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackSpeedAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (maxAttackSpeedAttribute != null && json.has("attack_speed")) {
            Double value;
            if(json.get("attack_speed") instanceof Double){
                value = json.getDouble("attack_speed");
            }else{
                value = Double.valueOf(json.get("attack_speed").toString()) ;
            }
            maxAttackSpeedAttribute.setBaseValue(value);
        }
        AttributeInstance maxFollowRangeAttribute = entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
        if (maxFollowRangeAttribute != null && json.has("follow_range")) {
            Double value;
            if(json.get("follow_range") instanceof Double){
                value = json.getDouble("follow_range");
            }else{
                value = Double.valueOf(json.get("follow_range").toString()) ;
            }
            maxFollowRangeAttribute.setBaseValue(value);
        }
        AttributeInstance maxKnockbackResistanceAttribute = entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (maxKnockbackResistanceAttribute != null && json.has("knockback_resistance")) {
            Double value;
            if(json.get("knockback_resistance") instanceof Double){
                value = json.getDouble("knockback_resistance");
            }else{
                value = Double.valueOf(json.get("knockback_resistance").toString()) ;
            }
            maxKnockbackResistanceAttribute.setBaseValue(value);
        }
        AttributeInstance maxLuckAttribute = entity.getAttribute(Attribute.GENERIC_LUCK);
        if (maxLuckAttribute != null && json.has("luck")) {
            Double value;
            if(json.get("luck") instanceof Double){
                value = json.getDouble("luck");
            }else{
                value = Double.valueOf(json.get("luck").toString()) ;
            }
            maxLuckAttribute.setBaseValue(value);
        }
        AttributeInstance maxAbsorptionAttribute = entity.getAttribute(Attribute.GENERIC_MAX_ABSORPTION);
        if (maxAbsorptionAttribute != null && json.has("max_absorption")) {
            Double value;
            if(json.get("max_absorption") instanceof Double){
                value = json.getDouble("max_absorption");
            }else{
                value = Double.valueOf(json.get("max_absorption").toString()) ;
            }
            maxAbsorptionAttribute.setBaseValue(value);
        }
        AttributeInstance maxMovementSpeedAttribute = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (maxMovementSpeedAttribute != null && json.has("movement_speed")) {
            Double value;
            if(json.get("movement_speed") instanceof Double){
                value = json.getDouble("movement_speed");
            }else{
                value = Double.valueOf(json.get("movement_speed").toString()) ;
            }
            maxMovementSpeedAttribute.setBaseValue(value);
        }
        AttributeInstance maxJumpStrengthAttribute = entity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH);
        if (maxJumpStrengthAttribute != null && json.has("jump_strength")) {
            Double value;
            if(json.get("jump_strength") instanceof Double){
                value = json.getDouble("jump_strength");
            }else{
                value = Double.valueOf(json.get("jump_strength").toString()) ;
            }
            maxJumpStrengthAttribute.setBaseValue(value);
        }
        AttributeInstance maxZombieSpawnReinforcementsAttribute = entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
        if (maxZombieSpawnReinforcementsAttribute != null && json.has("zombie_spawn_reinforcements")) {
            Double value;
            if(json.get("zombie_spawn_reinforcements") instanceof Double){
                value = json.getDouble("zombie_spawn_reinforcements");
            }else{
                value = Double.valueOf(json.get("zombie_spawn_reinforcements").toString());
            }
            maxZombieSpawnReinforcementsAttribute.setBaseValue(value);
        }

        // added in 1.20.6
        AttributeInstance maxFallDamageMultiplierAttribute = entity.getAttribute(Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER);
        if (maxFallDamageMultiplierAttribute != null && json.has("fall_damage_multiplier")) {
            Double value;
            if(json.get("fall_damage_multiplier") instanceof Double){
                value = json.getDouble("fall_damage_multiplier");
            }else{
                value = Double.valueOf(json.get("fall_damage_multiplier").toString());
            }
            maxFallDamageMultiplierAttribute.setBaseValue(value);
        }
        AttributeInstance maxGravityAttribute = entity.getAttribute(Attribute.GENERIC_GRAVITY);
        if (maxGravityAttribute != null && json.has("gravity")) {
            Double value;
            if(json.get("gravity") instanceof Double){
                value = json.getDouble("gravity");
            }else{
                value = Double.valueOf(json.get("gravity").toString());
            }
            maxGravityAttribute.setBaseValue(value);
        }
        AttributeInstance maxSafeFallDistanceAttribute = entity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE);
        if (maxSafeFallDistanceAttribute != null && json.has("save_fall_distance")) {
            Double value;
            if(json.get("save_fall_distance") instanceof Double){
                value = json.getDouble("save_fall_distance");
            }else{
                value = Double.valueOf(json.get("save_fall_distance").toString());
            }
            maxSafeFallDistanceAttribute.setBaseValue(value);
        }
        AttributeInstance maxScaleAttribute = entity.getAttribute(Attribute.GENERIC_SCALE);
        if (maxScaleAttribute != null && json.has("scale")) {
            Double value;
            if(json.get("scale") instanceof Double){
                value = json.getDouble("scale");
            }else{
                value = Double.valueOf(json.get("scale").toString());
            }
            maxScaleAttribute.setBaseValue(value);
        }
        AttributeInstance maxStepHeightAttribute = entity.getAttribute(Attribute.GENERIC_STEP_HEIGHT);
        if (maxStepHeightAttribute != null && json.has("step_height")) {
            Double value;
            if(json.get("step_height") instanceof Double){
                value = json.getDouble("step_height");
            }else{
                value = Double.valueOf(json.get("step_height").toString());
            }
            maxStepHeightAttribute.setBaseValue(value);
        }

        // Get and Set entity`s AI status
        if(json.has("ai_status")){
            entity.setAI(json.getBoolean("ai_status"));
        }

        // Get and Set entity is adult or baby
        if(json.has("is_adult")){
            Ageable ageable = (Ageable) entity;
            if(json.getBoolean("is_adult")){
                ageable.setAdult();
            }else{
                ageable.setBaby();
            }
        }

        // Get and Set if entity is a sheep, sheep color and sheared status
        if(json.has("sheep_color")){
            Sheep sheep = (Sheep) entity;
            sheep.setColor(DyeColor.valueOf(json.getString("sheep_color")));
            sheep.setSheared(json.getBoolean("sheep_sheared"));
        }


        // Get and Set potion effects
        JSONArray effectsJson = (JSONArray) json.get("potion_effects");
        for (Object effectObj : effectsJson) {
            JSONObject effectJson = (JSONObject) effectObj;
            String effectType = (String) effectJson.get("type");
            PotionEffectType type = PotionEffectType.getByName(effectType);
            int duration = ((Long) effectJson.get("duration")).intValue();
            int amplifier = ((Long) effectJson.get("amplifier")).intValue();
            entity.addPotionEffect(new PotionEffect(type, duration, amplifier));
        }
        // Finally set entity health (at the end to prevent duplication bug when health > maxHealth)
        entity.setHealth((int) json.get("health"));

        // Return entity for further processing if needed
        return entity;
    }

    public static JSONObject parseJSONObject(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }catch (JSONException err){
            Bukkit.getServer().getConsoleSender().sendMessage("§cFehler§7: §eParseException beim Parsen des JSON-Strings.");
            Bukkit.getServer().getConsoleSender().sendMessage(err.getMessage());
            return null;
        }
    }
    private static ItemStack stupidItem(){
        ItemStack itemStack = new ItemStack(Material.TORCH);
        PDCHandler.setPDCString(FangBall.plugin, itemStack, "stupiditem", "true");
        return itemStack;
    }
    private static boolean isStupidItem(ItemStack itemStack){
        return PDCHandler.hasPDCString(FangBall.plugin, itemStack, "stupiditem");
    }


}
