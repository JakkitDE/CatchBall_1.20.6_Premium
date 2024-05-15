package de.tomstahlberg.fangball.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHandler {
    public static void removeOneItem(ItemStack itemStack){
        itemStack.setAmount(itemStack.getAmount()-1);
    }
    public static void setShining(ItemStack itemStack, Boolean value){
        ItemMeta itemMeta = itemStack.getItemMeta();
        //itemStack.addUnsafeEnchantment(Enchantment.MENDING, 1);
        //itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setEnchantmentGlintOverride(value);
        itemStack.setItemMeta(itemMeta);
    }
    public static void removeShining(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.removeEnchantments();
        //itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
    }
}
