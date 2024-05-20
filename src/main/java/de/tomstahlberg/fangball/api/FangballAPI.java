package de.tomstahlberg.fangball.api;

import de.tomstahlberg.fangball.FangBall;
import de.tomstahlberg.fangball.utils.JsonHandler;
import de.tomstahlberg.fangball.utils.PDCHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

public class FangballAPI {
    public static boolean isFangball(ItemStack itemStack){
        return PDCHandler.hasPDCString(FangBall.plugin, itemStack, "MobEggType");
    }
    public static boolean isSingleUsableFangball(ItemStack itemStack){
        if(PDCHandler.getPDCString(FangBall.plugin,itemStack, "MobEggType").equalsIgnoreCase("Single")){
            return true;
        }
        return false;
    }
    public static boolean isMultiUsableFangball(ItemStack itemStack){
        if(PDCHandler.getPDCString(FangBall.plugin,itemStack, "MobEggType").equalsIgnoreCase("Multi")){
            return true;
        }
        return false;
    }
    public static boolean isFilled(ItemStack itemStack){
        return PDCHandler.hasPDCString(FangBall.plugin, itemStack, "mobdata");
    }
    public static EntityType getEntityType(ItemStack itemStack){
        String jsonString = PDCHandler.getPDCString(FangBall.plugin, itemStack, "mobdata");
        JSONObject jsonObject = JsonHandler.parseJSONObject(jsonString);
        EntityType entityType = EntityType.valueOf(jsonObject.getString("type"));
        return entityType;
    }
}
