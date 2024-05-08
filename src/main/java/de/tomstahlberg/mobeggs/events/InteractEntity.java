package de.tomstahlberg.mobeggs.events;

import de.tomstahlberg.mobeggs.MobEggs;
import de.tomstahlberg.mobeggs.utils.InventoryHandler;
import de.tomstahlberg.mobeggs.utils.JsonHandler;
import de.tomstahlberg.mobeggs.utils.MobEggCreator;
import de.tomstahlberg.mobeggs.utils.StringChecker;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.json.JSONObject;

import java.io.IOException;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) throws IOException {
        if(!(event.getHand() == EquipmentSlot.OFF_HAND))
            return;
        Player player = event.getPlayer();
        if(!(event.getRightClicked() instanceof LivingEntity))
            return;



        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        if(!(StringChecker.containsFireCharge(player.getInventory().getItemInMainHand().getType().toString())))
            return;


        JSONObject jsonObject = JsonHandler.serializeLivingEntity((LivingEntity) event.getRightClicked());

        MobEggCreator mobEggCreator = new MobEggCreator(jsonObject, player.getInventory().getItemInMainHand(), MobEggs.plugin, event.getRightClicked().getPersistentDataContainer());
        ItemStack mobEggItem = mobEggCreator.getMobbEggItem();
        InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
        player.getInventory().addItem(mobEggItem);

        event.getRightClicked().remove();
        player.sendMessage("§6§lGolden§3&lSky §8x §2Du hast ein Mob eingefangen.");
        event.setCancelled(true);
    }

}
