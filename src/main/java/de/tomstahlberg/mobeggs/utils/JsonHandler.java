package de.tomstahlberg.mobeggs.utils;

import com.google.gson.JsonParser;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;

public class JsonHandler {
    public static JSONObject serializeLivingEntity(LivingEntity entity) throws IOException {
        JSONObject json = new JSONObject();

        // Basic properties
        json.put("type", entity.getType().name());
        json.put("uuid", entity.getUniqueId().toString());
        //json.put("display_name", entity.getCustomName()); @deprecated
        json.put("display_name", ComponentHandler.getDisplayName(entity));
        json.put("health", entity.getHealth());
        json.put("is_dead", entity.isDead());
        json.put("is_glowing", entity.isGlowing());
        json.put("is_silent", entity.isSilent());

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
        if(entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH) != null){
            json.put("horse_jump_strength", entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue());
        }
        if(entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) != null){
            json.put("zombie_spawn_reinforcements", entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).getValue());
        }

        if(entity instanceof Ageable){
            Ageable ageable = (Ageable) entity;
            json.put("is_adult", ageable.isAdult());
        }
        if(entity instanceof Sheep){
            Sheep sheep = (Sheep) entity;
            json.put("sheep_color", sheep.getColor().toString());
            json.put("sheep_sheared", sheep.isSheared());
        }


        // Velocity
        JSONObject velocityJson = new JSONObject();
        Vector velocity = entity.getVelocity();
        velocityJson.put("x", velocity.getX());
        velocityJson.put("y", velocity.getY());
        velocityJson.put("z", velocity.getZ());
        json.put("velocity", velocityJson);

        // Potion effects
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

        // Add more attributes as needed

        return json;
    }

    public static LivingEntity deserializeLivingEntity(JSONObject json, Location location, World world, Plugin plugin) throws IOException {
        // Assuming you have some way to get the living entity type from the JSON
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf((String) json.get("type")));

        // Basic properties
        entity.setHealth((int) json.get("health"));
        if(json.has("display_name")){
            //entity.setCustomName((String) json.get("display_name")); @deprecated
            ComponentHandler.setDisplayName(entity, json.getString("display_name"));
        }
        entity.setGlowing((boolean) json.get("is_glowing"));
        entity.setSilent((boolean) json.get("is_silent"));

        AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            Double value;
            if(json.get("max_health") instanceof Double){
                value = json.getDouble("max_health");
            }else{
               value = Double.valueOf(json.get("max_health").toString()) ;
            }
            maxHealthAttribute.setBaseValue(value);

        }
        AttributeInstance maxArmorAttribute = entity.getAttribute(Attribute.GENERIC_ARMOR);
        if (maxArmorAttribute != null) {
            Double value;
            if(json.get("armor") instanceof Double){
                value = json.getDouble("armor");
            }else{
                value = Double.valueOf(json.get("armor").toString()) ;
            }
            maxArmorAttribute.setBaseValue(value);
        }
        AttributeInstance maxArmorToughnessAttribute = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        if (maxArmorToughnessAttribute != null) {
            Double value;
            if(json.get("armor_toughness") instanceof Double){
                value = json.getDouble("armor_toughness");
            }else{
                value = Double.valueOf(json.get("armor_toughness").toString()) ;
            }
            maxArmorToughnessAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackDamageAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (maxAttackDamageAttribute != null) {
            Double value;
            if(json.get("attack_damage") instanceof Double){
                value = json.getDouble("attack_damage");
            }else{
                value = Double.valueOf(json.get("attack_damage").toString()) ;
            }
            maxAttackDamageAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackKnockbackAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK);
        if (maxAttackKnockbackAttribute != null) {
            Double value;
            if(json.get("attack_knockback") instanceof Double){
                value = json.getDouble("attack_knockback");
            }else{
                value = Double.valueOf(json.get("attack_knockback").toString()) ;
            }
            maxAttackKnockbackAttribute.setBaseValue(value);
        }
        AttributeInstance maxAttackSpeedAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (maxAttackSpeedAttribute != null) {
            Double value;
            if(json.get("attack_speed") instanceof Double){
                value = json.getDouble("attack_speed");
            }else{
                value = Double.valueOf(json.get("attack_speed").toString()) ;
            }
            maxAttackSpeedAttribute.setBaseValue(value);
        }
        AttributeInstance maxFollowRangeAttribute = entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE);
        if (maxFollowRangeAttribute != null) {
            Double value;
            if(json.get("follow_range") instanceof Double){
                value = json.getDouble("follow_range");
            }else{
                value = Double.valueOf(json.get("follow_range").toString()) ;
            }
            maxFollowRangeAttribute.setBaseValue(value);
        }
        AttributeInstance maxKnockbackResistanceAttribute = entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        if (maxKnockbackResistanceAttribute != null) {
            Double value;
            if(json.get("knockback_resistance") instanceof Double){
                value = json.getDouble("knockback_resistance");
            }else{
                value = Double.valueOf(json.get("knockback_resistance").toString()) ;
            }
            maxKnockbackResistanceAttribute.setBaseValue(value);
        }
        AttributeInstance maxLuckAttribute = entity.getAttribute(Attribute.GENERIC_LUCK);
        if (maxLuckAttribute != null) {
            Double value;
            if(json.get("luck") instanceof Double){
                value = json.getDouble("luck");
            }else{
                value = Double.valueOf(json.get("luck").toString()) ;
            }
            maxLuckAttribute.setBaseValue(value);
        }
        AttributeInstance maxAbsorptionAttribute = entity.getAttribute(Attribute.GENERIC_MAX_ABSORPTION);
        if (maxAbsorptionAttribute != null) {
            Double value;
            if(json.get("max_absorption") instanceof Double){
                value = json.getDouble("max_absorption");
            }else{
                value = Double.valueOf(json.get("max_absorption").toString()) ;
            }
            maxAbsorptionAttribute.setBaseValue(value);
        }
        AttributeInstance maxMovementSpeedAttribute = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (maxMovementSpeedAttribute != null) {
            Double value;
            if(json.get("movement_speed") instanceof Double){
                value = json.getDouble("movement_speed");
            }else{
                value = Double.valueOf(json.get("movement_speed").toString()) ;
            }
            maxMovementSpeedAttribute.setBaseValue(value);
        }
        AttributeInstance maxHorseJumpStrengthAttribute = entity.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
        if (maxHorseJumpStrengthAttribute != null) {
            Double value;
            if(json.get("horse_jump_strength") instanceof Double){
                value = json.getDouble("horse_jump_strength");
            }else{
                value = Double.valueOf(json.get("horse_jump_strength").toString()) ;
            }
            maxHorseJumpStrengthAttribute.setBaseValue(value);
        }
        AttributeInstance maxZombieSpawnReinforcementsAttribute = entity.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
        if (maxZombieSpawnReinforcementsAttribute != null) {
            Double value;
            if(json.get("zombie_spawn_reinforcements") instanceof Double){
                value = json.getDouble("zombie_spawn_reinforcements");
            }else{
                value = Double.valueOf(json.get("zombie_spawn_reinforcements").toString());
            }
            maxZombieSpawnReinforcementsAttribute.setBaseValue(value);
        }

        if(json.has("is_adult")){
            Ageable ageable = (Ageable) entity;
            if(json.getBoolean("is_adult")){
                ageable.setAdult();
            }else{
                ageable.setBaby();
            }
        }
        if(json.has("sheep_color")){
            Sheep sheep = (Sheep) entity;
            sheep.setColor(DyeColor.valueOf(json.getString("sheep_color")));
            sheep.setSheared(json.getBoolean("sheep_sheared"));
        }

        // Potion effects
        JSONArray effectsJson = (JSONArray) json.get("potion_effects");
        for (Object effectObj : effectsJson) {
            JSONObject effectJson = (JSONObject) effectObj;
            String effectType = (String) effectJson.get("type");
            PotionEffectType type = PotionEffectType.getByName(effectType);
            int duration = ((Long) effectJson.get("duration")).intValue();
            int amplifier = ((Long) effectJson.get("amplifier")).intValue();
            entity.addPotionEffect(new PotionEffect(type, duration, amplifier));
        }


        // Add more attributes as needed

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
}
