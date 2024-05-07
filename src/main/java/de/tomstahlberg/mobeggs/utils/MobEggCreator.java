package de.tomstahlberg.mobeggs.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MobEggCreator {
    String jsonObjectString;
    //ItemStack itemStack;
    ItemStack mobbEggItem;
    Plugin plugin;

    public MobEggCreator(JSONObject jsonObject, ItemStack itemStack, Plugin plugin){
        this.plugin = plugin;
        this.jsonObjectString = jsonObject.toString();
        //this.itemStack = itemStack;
        createMobEggItem();
    }
    private void createMobEggItem(){
        ItemStack itemStack = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§3MobEgg");
        List<String> lore = new ArrayList<String>();
        lore.add("§eEin seltsames Objekt, das");
        lore.add("§eein Wesen enthalten könnte..");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        PDCHandler.setPDCString(this.plugin, itemStack, "mobdata", this.jsonObjectString);
        this.mobbEggItem = itemStack;
    }
    public ItemStack getMobbEggItem(){
        return this.mobbEggItem;
    }
}
