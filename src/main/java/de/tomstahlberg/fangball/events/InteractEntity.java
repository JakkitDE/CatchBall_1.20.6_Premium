package de.tomstahlberg.fangball.events;

import de.tomstahlberg.fangball.FangBall;
import de.tomstahlberg.fangball.utils.*;
import net.minecraft.world.Container;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftDonkey;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftSaddledInventory;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.io.IOException;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) throws IOException {
        Player player = event.getPlayer();
        //player.sendMessage("1");
        /*if(!(event.getHand() == EquipmentSlot.OFF_HAND))
            return;*/
        //player.sendMessage("2");
        if(!(event.getRightClicked() instanceof LivingEntity))
            return;
        //player.sendMessage("3");


        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        //player.sendMessage("4");
        //if(!(StringChecker.containsFireCharge(player.getInventory().getItemInMainHand().getType().toString())))
            //return;
        //player.sendMessage("5");

        if(CleanMobEggHandler.isMobEggItem(itemStack)){
            if(CleanMobEggHandler.isFilledMobEggItem(itemStack)){
                player.sendMessage("§6§lGolden§3§lSky §8x §2Der FangBall wird bereits bewohnt..");
                event.setCancelled(true);
            }else{
                JSONObject jsonObject = JsonHandler.serializeLivingEntity((LivingEntity) event.getRightClicked());

                MobEggCreator mobEggCreator = new MobEggCreator(jsonObject, player.getInventory().getItemInMainHand(), FangBall.plugin, event.getRightClicked().getPersistentDataContainer());
                ItemStack mobEggItem = mobEggCreator.getMobbEggItem();
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                player.getInventory().addItem(mobEggItem);

                event.getRightClicked().remove();
                player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast ein Mob eingefangen.");
                event.setCancelled(true);
            }
        }
    }

}
