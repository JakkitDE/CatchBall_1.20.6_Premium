package de.kadnick.catchball.utils;

import de.kadnick.catchball.utils.nbt.NBTDeserialization;
import de.kadnick.catchball.utils.nbt.NBTSerialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;

public class JsonHandler {
    public static JSONObject serializeLivingEntity(LivingEntity entity) throws IOException {
        JSONObject json = new JSONObject();
        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        CompoundTag compoundTag = new CompoundTag();
        nmsEntity.save(compoundTag);
        // remove UUID from NBT due to warnings printed into console which may lead to errors
        compoundTag.remove("UUID");
        byte[] bytes = NBTSerialization.serializeNBT(compoundTag);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        json.put("nbt", encoded);
        json.put("type", entity.getType().name());
        if(ComponentHandler.getDisplayName(entity) != null)
            json.put("display_name", ComponentHandler.getDisplayName(entity));
        return json;
    }

    public static LivingEntity deserializeLivingEntity(JSONObject json, Location location, World world, Plugin plugin) throws IOException {
        EntityType entityType = EntityType.valueOf(json.getString("type"));
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, entityType);

        Entity nmsEntity = ((CraftEntity) entity).getHandle();
        CompoundTag compoundTag = new CompoundTag();
        String encoded = json.getString("nbt");
        byte[] bytes = Base64.getDecoder().decode(encoded);
        compoundTag = NBTDeserialization.deserializeNBT(bytes);
        nmsEntity.load(compoundTag);
        entity.teleport(location);

        return entity;
    }
    public static JSONObject parseJSONObject(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }catch (JSONException err){
            Bukkit.getServer().getConsoleSender().sendMessage("§cError§7: §eParseException while trying to parse JSON-Strings.");
            Bukkit.getServer().getConsoleSender().sendMessage(err.getMessage());
            return null;
        }
    }
}