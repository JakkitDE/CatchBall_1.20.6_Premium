package de.kadnick.catchball.utils;

import de.kadnick.catchball.CatchBall;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CleanMobEggHandler {

    public static ItemStack getSingleMobEggItem(int amount){
        ItemStack itemStack = new ItemStack(CatchBall.configHandler.getMaterialSingle());
        itemStack.setAmount(amount);
        PDCHandler.setPDCString(CatchBall.plugin, itemStack, "MobEggType", "Single");
        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = CatchBall.configHandler.getSingleEmptyName();
        itemMeta.displayName(Component.text(itemName));
        itemStack.setItemMeta(itemMeta);
        List<String> lore = CatchBall.configHandler.getSingleEmptyLore();
        ComponentHandler.setLore(itemStack, lore);

        return itemStack;
    }

    public static ItemStack getMultiMobEggItem(int amount){
        ItemStack itemStack = new ItemStack(CatchBall.configHandler.getMaterialMulti());
        itemStack.setAmount(amount);
        PDCHandler.setPDCString(CatchBall.plugin, itemStack, "MobEggType", "Multi");
        ItemMeta itemMeta = itemStack.getItemMeta();
        String itemName = CatchBall.configHandler.getMultiEmptyName();
        itemMeta.displayName(Component.text(itemName));
        itemStack.setItemMeta(itemMeta);
        List<String> lore = CatchBall.configHandler.getMultiEmptyLore();
        ComponentHandler.setLore(itemStack, lore);

        return itemStack;
    }
    public static boolean isMobEggItem(ItemStack itemStack){
        return PDCHandler.hasPDCString(CatchBall.plugin, itemStack, "MobEggType");
    }
    public static boolean isMultiMobEggItem(Plugin plugin, ItemStack itemStack){
        if(PDCHandler.getPDCString(plugin,itemStack, "MobEggType").equalsIgnoreCase("Multi")){
            return true;
        }
        return false;
    }
    public static boolean isFilledMobEggItem(ItemStack itemStack){
        return PDCHandler.hasPDCString(CatchBall.plugin, itemStack, "mobdata");
    }
}
