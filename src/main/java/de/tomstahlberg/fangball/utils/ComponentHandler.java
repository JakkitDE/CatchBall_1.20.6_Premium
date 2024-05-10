package de.tomstahlberg.fangball.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class ComponentHandler {
    public static void setDisplayName(ItemStack itemStack, String message){
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component component = Component.text(message);
        itemMeta.displayName(component);
        itemStack.setItemMeta(itemMeta);
    }
    public static void setDisplayName(Entity entity, String message){
        Component component = Component.text(message);
        entity.customName(component);
    }
    public static String getDisplayName(Entity entity){
        return PlainTextComponentSerializer.plainText().serializeOrNull(entity.customName());
    }
    public static String getDisplayName(ItemStack itemStack){
        return PlainTextComponentSerializer.plainText().serializeOrNull(itemStack.getItemMeta().displayName());
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
    public static List<String> getLore(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(!itemMeta.hasLore())
            return new ArrayList<>();
        List<String> lore = new ArrayList<>();
        List<Component> components = new ArrayList<Component>(itemMeta.lore());
        for(Component component : components){
            String line = PlainTextComponentSerializer.plainText().serializeOrNull(component);
            lore.add(line);
        }
        return lore;
    }
}
