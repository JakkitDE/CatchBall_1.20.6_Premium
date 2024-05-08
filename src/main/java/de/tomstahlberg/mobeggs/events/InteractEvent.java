package de.tomstahlberg.mobeggs.events;

import de.tomstahlberg.mobeggs.MobEggs;
import de.tomstahlberg.mobeggs.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.json.JSONObject;

import java.io.IOException;

public class InteractEvent implements Listener {
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
                return;
            if(!(StringChecker.containsFireCharge(player.getInventory().getItemInMainHand().getType().toString())))
                return;
            if(!(PDCHandler.hasPDCString(MobEggs.plugin,event.getPlayer().getInventory().getItemInMainHand(), "mobdata")))
                return;

            //
            //Mob platzieren
            String jsonString = PDCHandler.getPDCString(MobEggs.plugin, player.getInventory().getItemInMainHand(), "mobdata");
            JSONObject jsonObject = JsonHandler.parseJSONObject(jsonString);

            Location location = new Location(player.getLocation().getWorld(),event.getClickedBlock().getX(), event.getClickedBlock().getY()+1.0, event.getClickedBlock().getZ());
            LivingEntity entity = JsonHandler.deserializeLivingEntity(jsonObject, location, player.getLocation().getWorld(), MobEggs.plugin);
            if(PDCHandler.hasPDCBytes(MobEggs.plugin, player.getInventory().getItemInMainHand(), "pdc")){
                byte[] bytes = PDCHandler.getPDCBytes(MobEggs.plugin, player.getInventory().getItemInMainHand(), "pdc");
                entity.getPersistentDataContainer().readFromBytes(bytes);
            }

            /* Check if item is single or multi */
            if(CleanMobEggHandler.isMultiMobEggItem(MobEggs.plugin, player.getInventory().getItemInMainHand())){
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                player.getInventory().addItem(CleanMobEggHandler.getMultiMobEggItem(1));
            }else{
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
            }
            //
            player.sendMessage("§6§lGolden§3&lSky §8x §2Du hast ein Mob freigelassen.");
            event.setCancelled(true);
        }
    }
}
