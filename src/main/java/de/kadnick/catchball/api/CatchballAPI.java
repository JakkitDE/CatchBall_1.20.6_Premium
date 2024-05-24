package de.kadnick.catchball.api;

import de.kadnick.catchball.CatchBall;
import de.kadnick.catchball.utils.JsonHandler;
import de.kadnick.catchball.utils.PDCHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

public class CatchballAPI {
    public static boolean isCatchBall(ItemStack itemStack){
        return PDCHandler.hasPDCString(CatchBall.plugin, itemStack, "MobEggType");
    }
    public static boolean isSingleUsableCatchBall(ItemStack itemStack){
        if(PDCHandler.getPDCString(CatchBall.plugin,itemStack, "MobEggType").equalsIgnoreCase("Single")){
            return true;
        }
        return false;
    }
    public static boolean isMultiUsableCatchBall(ItemStack itemStack){
        if(PDCHandler.getPDCString(CatchBall.plugin,itemStack, "MobEggType").equalsIgnoreCase("Multi")){
            return true;
        }
        return false;
    }
    public static boolean isFilled(ItemStack itemStack){
        return PDCHandler.hasPDCString(CatchBall.plugin, itemStack, "mobdata");
    }
    public static EntityType getEntityType(ItemStack itemStack){
        String jsonString = PDCHandler.getPDCString(CatchBall.plugin, itemStack, "mobdata");
        JSONObject jsonObject = JsonHandler.parseJSONObject(jsonString);
        EntityType entityType = EntityType.valueOf(jsonObject.getString("type"));
        return entityType;
    }
}
