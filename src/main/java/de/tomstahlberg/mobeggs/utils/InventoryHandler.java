package de.tomstahlberg.mobeggs.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryHandler {
    public static void removeOneItem(ItemStack itemStack){
        itemStack.setAmount(itemStack.getAmount()-1);
    }
}
