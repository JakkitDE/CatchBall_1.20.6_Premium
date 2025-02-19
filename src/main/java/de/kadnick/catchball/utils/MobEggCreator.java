package de.kadnick.catchball.utils;

import de.kadnick.catchball.CatchBall;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        this.persistentDataContainer = persistentDataContainer;
        this.mobbEggItem = new ItemStack(itemStack);
        createMobEggItem();
    }
    private void createMobEggItem() throws IOException {
        ItemStack itemStack = new ItemStack(this.mobbEggItem);
        // Set item name
        String itemName;
        if(CleanMobEggHandler.isMultiMobEggItem(CatchBall.plugin,itemStack)){
            itemName = CatchBall.configHandler.getMultiFilledName();

        }else{
            itemName = CatchBall.configHandler.getSingleFilledName();
        }
        itemName = itemName.replace("%type%", (String) jsonObject.get("type"));
        itemName = itemName.replace("_", " ");
        if(jsonObject.has("display_name")){
            itemName = itemName.replace("%nick%", (String) jsonObject.get("display_name"));
        }else{
            itemName = itemName.replace("%nick%", CatchBall.configHandler.getNoNick());
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(itemName));
        itemStack.setItemMeta(itemMeta);

        // Set item lore
        List<String> lore;
        if(CleanMobEggHandler.isMultiMobEggItem(CatchBall.plugin,itemStack)){
            lore = CatchBall.configHandler.getMultiFilledLore();
        }else{
            lore = CatchBall.configHandler.getSingleFilledLore();
        }
        for(int i = 0;i<lore.size();i++){
            String loreLine = lore.get(i);
            loreLine = loreLine.replace("%type%", (String) jsonObject.get("type"));
            loreLine = loreLine.replace("_", " ");
            if(jsonObject.has("display_name")){
                loreLine = loreLine.replace("%nick%", (String) jsonObject.get("display_name"));
            }else{
                loreLine = loreLine.replace("%nick%", CatchBall.configHandler.getNoNick());
            }
            lore.set(i, loreLine);
        }
        ComponentHandler.setLore(itemStack, lore);

        // Set item shining
        if(CleanMobEggHandler.isMultiMobEggItem(CatchBall.plugin,itemStack)){
            InventoryHandler.setShining(itemStack, CatchBall.configHandler.getShiningMulti());
        }else{
            InventoryHandler.setShining(itemStack, CatchBall.configHandler.getShiningSingle());
        }

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
