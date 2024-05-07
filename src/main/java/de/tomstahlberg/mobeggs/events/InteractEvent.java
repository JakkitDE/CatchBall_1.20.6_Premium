package de.tomstahlberg.mobeggs.events;

import de.tomstahlberg.mobeggs.MobEggs;
import de.tomstahlberg.mobeggs.utils.InventoryHandler;
import de.tomstahlberg.mobeggs.utils.JsonHandler;
import de.tomstahlberg.mobeggs.utils.PDCHandler;
import de.tomstahlberg.mobeggs.utils.StringChecker;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class InteractEvent implements Listener {
    @EventHandler
    public void playerInteract(PlayerInteractEvent event){
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
            player.sendMessage(jsonObject.toString());

            Location location = new Location(player.getLocation().getWorld(),event.getClickedBlock().getX(), event.getClickedBlock().getY()+1.0, event.getClickedBlock().getZ());
            JsonHandler.deserializeLivingEntity(jsonObject, location, player.getLocation().getWorld());

            InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
            //
            player.sendMessage("§6§lGolden§3&lSky §8x §2Du hast ein SafariNetz platziert.");
            event.setCancelled(true);
        }
    }
}
