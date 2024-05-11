package de.tomstahlberg.fangball.utils;

import com.sun.jna.platform.win32.Sspi;
import de.tomstahlberg.fangball.FangBall;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MobEggCreator {
    String jsonObjectString;
    JSONObject jsonObject;
    //ItemStack itemStack;
    ItemStack mobbEggItem;
    Plugin plugin;
    PersistentDataContainer persistentDataContainer;

    public MobEggCreator(JSONObject jsonObject, ItemStack itemStack, Plugin plugin, PersistentDataContainer persistentDataContainer) throws IOException {
        this.plugin = plugin;
        this.jsonObjectString = jsonObject.toString();
        this.jsonObject = jsonObject;
        //this.itemStack = itemStack;
        this.persistentDataContainer = persistentDataContainer;
        this.mobbEggItem = itemStack;
        createMobEggItem();
    }
    private void createMobEggItem() throws IOException {
        ItemStack itemStack = new ItemStack(this.mobbEggItem);
        InventoryHandler.setShining(itemStack);
        List<String> lore = ComponentHandler.getLore(itemStack);
        if(jsonObject.has("display_name")){
            lore.add("");
            lore.add("§a§lBelegt§7: §6"+this.jsonObject.get("display_name")+" §e#"+jsonObject.get("type"));
        }else{
            lore.add("");
            lore.add("§a§lBelegt§7: §e#"+this.jsonObject.get("type"));
        }
        ComponentHandler.setLore(itemStack, lore);
        PDCHandler.setPDCString(this.plugin, itemStack, "mobdata", this.jsonObjectString);
        if(this.persistentDataContainer != null){
            if(!(this.persistentDataContainer.isEmpty())){
                PDCHandler.setPDCBytes(plugin, itemStack, "pdc", this.persistentDataContainer.serializeToBytes());
            }
        }
        itemStack.setAmount(1);
        this.mobbEggItem = itemStack;
    }
    public ItemStack getMobEggItem(){
        return this.mobbEggItem;
    }
}
