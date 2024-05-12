package de.tomstahlberg.fangball.events;

import de.tomstahlberg.fangball.FangBall;
import de.tomstahlberg.fangball.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
            if(!(PDCHandler.hasPDCString(FangBall.plugin,event.getPlayer().getInventory().getItemInMainHand(), "mobdata")))
                return;
            if(event.getHand() == EquipmentSlot.OFF_HAND)
                return;
            //
            //Mob platzieren
            String jsonString = PDCHandler.getPDCString(FangBall.plugin, player.getInventory().getItemInMainHand(), "mobdata");
            JSONObject jsonObject = JsonHandler.parseJSONObject(jsonString);

            Location location = new Location(player.getLocation().getWorld(),event.getClickedBlock().getX(), event.getClickedBlock().getY()+1.0, event.getClickedBlock().getZ());
            LivingEntity entity = JsonHandler.deserializeLivingEntity(jsonObject, location, player.getLocation().getWorld(), FangBall.plugin);
            if(PDCHandler.hasPDCBytes(FangBall.plugin, player.getInventory().getItemInMainHand(), "pdc")){
                byte[] bytes = PDCHandler.getPDCBytes(FangBall.plugin, player.getInventory().getItemInMainHand(), "pdc");
                entity.getPersistentDataContainer().readFromBytes(bytes);
            }
            if(entity instanceof Tameable){
                Tameable tameable = (Tameable) entity;
                if(tameable.isTamed()){
                    tameable.setOwner(player);
                }
            }

            /* Check if item is single or multi */
            if(CleanMobEggHandler.isMultiMobEggItem(FangBall.plugin, player.getInventory().getItemInMainHand())){
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                player.getInventory().addItem(CleanMobEggHandler.getMultiMobEggItem(1));
            }else{
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
            }
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);

            //
            player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast ein Mob freigelassen.");
            event.setCancelled(true);
        }
    }
}
