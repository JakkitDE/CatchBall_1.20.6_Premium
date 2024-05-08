package de.tomstahlberg.mobeggs.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ComponentHandler {
    public static void setDisplayName(ItemStack itemStack, String message){
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component component = Component.text(message);
        itemMeta.displayName(component);
        itemStack.setItemMeta(itemMeta);
    }
    public static void setLore(ItemStack itemStack, List<String> loreList){
        List<Component> components = new ArrayList<>();
        for(String loreLine : loreList){
            components.add(Component.text(loreLine));
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.lore(components);
        itemStack.setItemMeta(itemMeta);
    }
}
