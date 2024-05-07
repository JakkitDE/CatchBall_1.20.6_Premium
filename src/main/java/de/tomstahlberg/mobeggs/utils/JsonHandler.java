package de.tomstahlberg.mobeggs.utils;

import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;

public class JsonHandler {
    public static JSONObject serializeLivingEntity(LivingEntity entity) {
        JSONObject json = new JSONObject();

        // Basic properties
        json.put("type", entity.getType().name());
        json.put("uuid", entity.getUniqueId().toString());
        json.put("display_name", entity.getCustomName());
        json.put("health", entity.getHealth());
        json.put("max_health", entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        json.put("is_dead", entity.isDead());
        json.put("is_glowing", entity.isGlowing());
        json.put("is_silent", entity.isSilent());
        json.put("is_swimming", entity.isSwimming());

        if(!(entity.getPersistentDataContainer().isEmpty())){
            JSONObject pdc = new JSONObject();
            Set<NamespacedKey> keys = entity.getPersistentDataContainer().getKeys();
            for(NamespacedKey key : keys){
                pdc.put(key.getKey(), entity.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
            json.put("pdc", pdc);
        }

        /*// Location
        JSONObject locationJson = new JSONObject();
        locationJson.put("x", entity.getLocation().getX());
        locationJson.put("y", entity.getLocation().getY());
        locationJson.put("z", entity.getLocation().getZ());
        json.put("location", locationJson);*/

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

    public static LivingEntity deserializeLivingEntity(JSONObject json, Location location, World world, Plugin plugin) {
        // Assuming you have some way to get the living entity type from the JSON
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.valueOf((String) json.get("type")));

        // Basic properties
        entity.setHealth((int) json.get("health"));
        AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            int maxHealth = (int) json.get("max_health");
            maxHealthAttribute.setBaseValue(maxHealth);
        }
        if(json.has("display_name")){
            entity.setCustomName((String) json.get("display_name"));
        }
        entity.setGlowing((boolean) json.get("is_glowing"));
        entity.setSilent((boolean) json.get("is_silent"));
        entity.setSwimming((boolean) json.get("is_swimming"));

        /*// Location
        JSONObject locationJson = (JSONObject) json.get("location");
        double x = (double) locationJson.get("x");
        double y = (double) locationJson.get("y");
        double z = (double) locationJson.get("z");
        Location location = new Location(entity.getWorld(), x, y, z);
        entity.teleport(location);*/

        // Velocity
        //JSONObject velocityJson = (JSONObject) json.get("velocity");

        //double velX;
        //double velY;
        //double velZ;


        //double velX = (double) velocityJson.get("x");
        //double velY = (double) velocityJson.get("y");
        //double velZ = (double) velocityJson.get("z");
        //entity.setVelocity(new Vector(velX, velY, velZ));

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

        if(json.has("pdc")){
            JSONObject pdc = json.getJSONObject("pdc");
            for(String key : pdc.keySet()){
                String value = pdc.getString(key);
                NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
                entity.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
            }
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
