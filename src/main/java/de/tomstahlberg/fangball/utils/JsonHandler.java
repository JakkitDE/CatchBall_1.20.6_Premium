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
        jsonPutAttributeIfExists(json, Attribute.GENERIC_MAX_HEALTH ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_ARMOR ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_ARMOR_TOUGHNESS ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_ATTACK_DAMAGE ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_ATTACK_KNOCKBACK ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_ATTACK_SPEED ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_FOLLOW_RANGE ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_KNOCKBACK_RESISTANCE ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_LUCK ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_MAX_ABSORPTION ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_MOVEMENT_SPEED ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_JUMP_STRENGTH ,entity);
        jsonPutAttributeIfExists(json, Attribute.ZOMBIE_SPAWN_REINFORCEMENTS ,entity);
        // Added in 1.20.6
        jsonPutAttributeIfExists(json, Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_GRAVITY ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_SAFE_FALL_DISTANCE ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_SCALE ,entity);
        jsonPutAttributeIfExists(json, Attribute.GENERIC_STEP_HEIGHT ,entity);
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
        entitySetAttributeIfExists(json, Attribute.GENERIC_MAX_HEALTH ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_ARMOR ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_ARMOR_TOUGHNESS ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_ATTACK_DAMAGE ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_ATTACK_KNOCKBACK ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_ATTACK_SPEED ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_FOLLOW_RANGE ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_KNOCKBACK_RESISTANCE ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_LUCK ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_MAX_ABSORPTION ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_MOVEMENT_SPEED ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_JUMP_STRENGTH ,entity);
        entitySetAttributeIfExists(json, Attribute.ZOMBIE_SPAWN_REINFORCEMENTS ,entity);
        // Added in 1.20.6
        entitySetAttributeIfExists(json, Attribute.GENERIC_FALL_DAMAGE_MULTIPLIER ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_GRAVITY ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_SAFE_FALL_DISTANCE ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_SCALE ,entity);
        entitySetAttributeIfExists(json, Attribute.GENERIC_STEP_HEIGHT ,entity);
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
        ////return entity;
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
    private static void jsonPutAttributeIfExists(JSONObject json, Attribute attribute, LivingEntity entity){
        if(entity.getAttribute(attribute) == null)
            return;
        String attributeKey = attribute.getKey().getKey();
        json.put(attributeKey, ((Double) entity.getAttribute(attribute).getValue()).doubleValue());
    }
    private static void entitySetAttributeIfExists(JSONObject json, Attribute attribute, LivingEntity entity){
        if(entity.getAttribute(attribute) == null)
            return;
        AttributeInstance attributeInstance = entity.getAttribute(Attribute.GENERIC_SCALE);
        String attributeKey = attribute.getKey().getKey();
        if (attributeInstance != null && json.has(attributeKey)) {
            Double value;
            if(json.get(attributeKey) instanceof Double){
                value = json.getDouble(attributeKey);
            }else{
                value = Double.valueOf(json.get(attributeKey).toString());
            }
            entity.getAttribute(attribute).setBaseValue(value);
        }
    }
}