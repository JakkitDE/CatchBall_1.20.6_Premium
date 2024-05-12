package de.tomstahlberg.fangball.utils;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemSerializationHandler {
    public static String getBase64String(ItemStack itemStack){
        byte[] bytes = itemStack.serializeAsBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static ItemStack getItemStackFromBase64String(String base64String){
        byte[] bytes = Base64.getDecoder().decode(base64String);
        return ItemStack.deserializeBytes(bytes);
    }
}
