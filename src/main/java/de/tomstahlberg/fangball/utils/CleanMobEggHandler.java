package de.tomstahlberg.fangball.utils;

import de.tomstahlberg.fangball.FangBall;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CleanMobEggHandler {

    public static ItemStack getSingleMobEggItem(int amount){
        ItemStack itemStack = new ItemStack(Material.MAGMA_CREAM);
        itemStack.setAmount(amount);
        PDCHandler.setPDCString(FangBall.plugin, itemStack, "MobEggType", "Single");
        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = FangBall.configHandler.getSingleEmptyName();
        itemMeta.displayName(Component.text(itemName));
        itemStack.setItemMeta(itemMeta);
        List<String> lore = FangBall.configHandler.getSingleEmptyLore();
        ComponentHandler.setLore(itemStack, lore);

        return itemStack;
    }

    public static ItemStack getMultiMobEggItem(int amount){
        ItemStack itemStack = new ItemStack(Material.MAGMA_CREAM);
        itemStack.setAmount(amount);
        PDCHandler.setPDCString(FangBall.plugin, itemStack, "MobEggType", "Multi");
        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = FangBall.configHandler.getMultiEmptyName();
        itemMeta.displayName(Component.text(itemName));
        itemStack.setItemMeta(itemMeta);
        List<String> lore = FangBall.configHandler.getMultiEmptyLore();
        ComponentHandler.setLore(itemStack, lore);

        return itemStack;
    }
    public static boolean isMobEggItem(ItemStack itemStack){
        return PDCHandler.hasPDCString(FangBall.plugin, itemStack, "MobEggType");
    }
    public static boolean isMultiMobEggItem(Plugin plugin, ItemStack itemStack){
        if(PDCHandler.getPDCString(plugin,itemStack, "MobEggType").equalsIgnoreCase("Multi")){
            return true;
        }
        return false;
    }
    public static boolean isFilledMobEggItem(ItemStack itemStack){
        return PDCHandler.hasPDCString(FangBall.plugin, itemStack, "mobdata");
    }
}
