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
        Component textComponent = Component.text("§3§lFangball §7(§2Einweg§7)");
        itemMeta.displayName(textComponent);

        List<Component> components = new ArrayList<>();
        components.add(Component.text("§2Verwende ihn mit Bedacht, du kannst"));
        components.add(Component.text("§2ihn nur einmalig verwenden, um ein Mob"));
        components.add(Component.text("§2einzufangen."));
        itemMeta.lore(components);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getMultiMobEggItem(int amount){
        ItemStack itemStack = new ItemStack(Material.MAGMA_CREAM);
        itemStack.setAmount(amount);
        PDCHandler.setPDCString(FangBall.plugin, itemStack, "MobEggType", "Multi");
        ItemMeta itemMeta = itemStack.getItemMeta();
        Component textComponent = Component.text("§3§lFangball §7(§6Mehrweg§7)");
        itemMeta.displayName(textComponent);

        List<Component> components = new ArrayList<>();
        components.add(Component.text("§2Verwende ihn ganz nach eigenem Belieben. Du kannst"));
        components.add(Component.text("§2ihn so oft verwenden, wie du willst, um ein Mob"));
        components.add(Component.text("§2einzufangen."));
        itemMeta.lore(components);

        itemStack.setItemMeta(itemMeta);
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
