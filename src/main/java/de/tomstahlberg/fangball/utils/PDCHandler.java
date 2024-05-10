package de.tomstahlberg.fangball.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PDCHandler {
    public static void setPDCString(Plugin plugin, ItemStack itemStack, String key, String value){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
    }
    public static String getPDCString(Plugin plugin, ItemStack itemStack, String key){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
    }
    public static boolean hasPDCString(Plugin plugin, ItemStack itemStack, String key){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return itemMeta.getPersistentDataContainer().has(namespacedKey);
    }
    public static void setPDCBytes(Plugin plugin, ItemStack itemStack, String key, byte[] value){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.BYTE_ARRAY, value);
        itemStack.setItemMeta(itemMeta);
    }
    public static byte[] getPDCBytes(Plugin plugin, ItemStack itemStack, String key){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.BYTE_ARRAY);
    }
    public static boolean hasPDCBytes(Plugin plugin, ItemStack itemStack, String key){
        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return itemMeta.getPersistentDataContainer().has(namespacedKey);
    }
}
