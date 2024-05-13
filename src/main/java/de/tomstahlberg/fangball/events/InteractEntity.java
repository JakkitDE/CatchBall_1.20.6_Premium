package de.tomstahlberg.fangball.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import de.tomstahlberg.fangball.FangBall;
import de.tomstahlberg.fangball.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.io.IOException;

public class InteractEntity implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) throws IOException {
        Player player = event.getPlayer();
        if(!(event.getRightClicked() instanceof LivingEntity))
            return;
        if(event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if(event.getRightClicked() instanceof Player){
            return;
        }

        //Check if player has permission
        if(!(player.hasPermission("fangball.use")) && !(player.isOp())){
            if(!FangBall.configHandler.getNoPermissionMessage().equalsIgnoreCase(""))
                player.sendMessage(FangBall.configHandler.getNoPermissionMessage());
            if(FangBall.configHandler.noPermissionSoundEnabled()){
                player.playSound(player.getLocation(),FangBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
            }
            return;
        }

        //Check if in allowed world
        if(!FangBall.configHandler.getAllowedWorlds().contains(player.getWorld())){
            if(!player.hasPermission("fangball.use.world.bypass") || !player.isOp()){
                if(!FangBall.configHandler.getNotAllowedWorldMessage().equalsIgnoreCase(""))
                    player.sendMessage(FangBall.configHandler.getNotAllowedWorldMessage());

                if(FangBall.configHandler.noPermissionSoundEnabled()){
                    if(FangBall.configHandler.getNoPermissionSound() != null)
                        player.playSound(player.getLocation(),FangBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                }
                return;
            }
        }
        //Check for SuperiorSkyblock hook
        if(FangBall.configHandler.isSuperiorSkyBlockHookEnabled()){
            // Check if Player has permission on island
            if(SuperiorSkyblockAPI.getIslandAt(player.getLocation()) != null){
                if(!isPermitted(player) && !player.isOp()){
                    if(!FangBall.configHandler.getNoPermissionOnIslandMessage().equalsIgnoreCase(""))
                        player.sendMessage(FangBall.configHandler.getNoPermissionOnIslandMessage());
                    if(FangBall.configHandler.noPermissionSoundEnabled()){
                        if(FangBall.configHandler.getNoPermissionSound() != null)
                            player.playSound(player.getLocation(),FangBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                    }
                    return;
                }
            }
        }


        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();


        if(CleanMobEggHandler.isMobEggItem(itemStack)){
            if(CleanMobEggHandler.isFilledMobEggItem(itemStack)){
                if(!(FangBall.configHandler.getAlreadyFilledMessage().equalsIgnoreCase(""))){
                    player.sendMessage(FangBall.configHandler.getAlreadyFilledMessage());
                }
                if(FangBall.configHandler.noPermissionSoundEnabled()){
                    if(FangBall.configHandler.getNoPermissionSound() != null){
                        player.playSound(player.getLocation(), FangBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                    }
                }
                event.setCancelled(true);
            }else{
                JSONObject jsonObject = JsonHandler.serializeLivingEntity((LivingEntity) event.getRightClicked());

                MobEggCreator mobEggCreator = new MobEggCreator(jsonObject, player.getInventory().getItemInMainHand(), FangBall.plugin, event.getRightClicked().getPersistentDataContainer());
                ItemStack mobEggItem = mobEggCreator.getMobEggItem();
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                //player.getInventory().addItem(mobEggItem);
                handleItemGive(player, mobEggItem);

                event.getRightClicked().remove();
                if(!(FangBall.configHandler.getCatchedMessage().equalsIgnoreCase(""))){
                    player.sendMessage(FangBall.configHandler.getCatchedMessage());
                }
                event.setCancelled(true);
            }
        }
    }

    public void handleItemGive(Player player, ItemStack itemStack){
        Bukkit.getScheduler().runTaskLater(FangBall.plugin, new Runnable() {
            @Override
            public void run() {
                player.getInventory().addItem(itemStack);
                if(FangBall.configHandler.catchSoundEnabled()){
                    if(FangBall.configHandler.getCatchSound() != null){
                        player.playSound(player.getLocation(), FangBall.configHandler.getCatchSound(), 1.0f, 1.0f);
                    }
                }
            }
        }, 1);
    }
    private boolean isPermitted(Player player){
        Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island.hasPermission(superiorPlayer, FangBall.fangballUsePermission);
    }
}