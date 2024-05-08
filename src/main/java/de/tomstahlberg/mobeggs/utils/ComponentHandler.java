package de.tomstahlberg.mobeggs.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ComponentHandler {
    public static void setDisplayName(ItemStack itemStack, String message){
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component component = Component.text(message);
        itemMeta.displayName(component);
    }
}
