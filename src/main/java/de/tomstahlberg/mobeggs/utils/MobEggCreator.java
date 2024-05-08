package de.tomstahlberg.mobeggs.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MobEggCreator {
    String jsonObjectString;
    //ItemStack itemStack;
    ItemStack mobbEggItem;
    Plugin plugin;
    PersistentDataContainer persistentDataContainer;

    public MobEggCreator(JSONObject jsonObject, ItemStack itemStack, Plugin plugin, PersistentDataContainer persistentDataContainer) throws IOException {
        this.plugin = plugin;
        this.jsonObjectString = jsonObject.toString();
        //this.itemStack = itemStack;
        this.persistentDataContainer = persistentDataContainer;
        createMobEggItem();
    }
    private void createMobEggItem() throws IOException {
        ItemStack itemStack = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§3MobEgg");
        List<String> lore = new ArrayList<String>();
        lore.add("§eEin seltsames Objekt, das");
        lore.add("§eein Wesen enthalten könnte..");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        PDCHandler.setPDCString(this.plugin, itemStack, "mobdata", this.jsonObjectString);
        if(this.persistentDataContainer != null){
            if(!(this.persistentDataContainer.isEmpty())){
                PDCHandler.setPDCBytes(plugin, itemStack, "pdc", this.persistentDataContainer.serializeToBytes());
            }
        }

        this.mobbEggItem = itemStack;
    }
    public ItemStack getMobbEggItem(){
        return this.mobbEggItem;
    }
}
