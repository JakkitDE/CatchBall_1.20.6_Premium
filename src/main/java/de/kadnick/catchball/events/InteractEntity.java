package de.kadnick.catchball.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import de.kadnick.catchball.CatchBall;
import de.kadnick.catchball.api.events.PlayerCatchingMobEvent;
import de.kadnick.catchball.utils.*;
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
        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
            return;
        //Call PlayerCatchingMobEvent and run if not cancelled
        PlayerCatchingMobEvent playerCatchingMobEvent = new PlayerCatchingMobEvent(player, ((LivingEntity) event.getRightClicked()));
        Bukkit.getPluginManager().callEvent(playerCatchingMobEvent);
        if(playerCatchingMobEvent.isCancelled())
            return;

        //Check if player has permission
        if(!(player.hasPermission("catchball.use")) && !(player.isOp())){
            if(!CatchBall.configHandler.getNoPermissionMessage().equalsIgnoreCase(""))
                player.sendMessage(CatchBall.configHandler.getNoPermissionMessage());
            if(CatchBall.configHandler.noPermissionSoundEnabled()){
                player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
            }
            return;
        }
        PermissionHandler permissionHandler = new PermissionHandler((LivingEntity) event.getRightClicked(), player);
        if(!permissionHandler.isPermitted()){
            if(!CatchBall.configHandler.getNoPermissionEntityMessage(event.getRightClicked()).equalsIgnoreCase(""))
                player.sendMessage(CatchBall.configHandler.getNoPermissionEntityMessage(event.getRightClicked()));
            if(CatchBall.configHandler.noPermissionSoundEnabled()){
                player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
            }
            return;
        }

        //Check if in allowed world
        if(!CatchBall.configHandler.getAllowedWorlds().contains(player.getWorld())){
            if(!player.hasPermission("catchball.use.world.bypass") || !player.isOp()){
                if(!CatchBall.configHandler.getNotAllowedWorldMessage().equalsIgnoreCase(""))
                    player.sendMessage(CatchBall.configHandler.getNotAllowedWorldMessage());

                if(CatchBall.configHandler.noPermissionSoundEnabled()){
                    if(CatchBall.configHandler.getNoPermissionSound() != null)
                        player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                }
                return;
            }
        }
        //Check for SuperiorSkyblock hook
        if(CatchBall.configHandler.isSuperiorSkyBlockHookEnabled()){
            // Check if Player has permission on island
            if(SuperiorSkyblockAPI.getIslandAt(player.getLocation()) != null){
                if(!isPermitted(player) && !player.isOp()){
                    if(!CatchBall.configHandler.getNoPermissionOnIslandMessage().equalsIgnoreCase(""))
                        player.sendMessage(CatchBall.configHandler.getNoPermissionOnIslandMessage());
                    if(CatchBall.configHandler.noPermissionSoundEnabled()){
                        if(CatchBall.configHandler.getNoPermissionSound() != null)
                            player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                    }
                    return;
                }
            }else{
                // If player is not on island but hook enabled
                if(CatchBall.configHandler.isSuperiorSkyBlockUsingOnlyOnIslandsIfAllowedEnabled()){
                    if(!(player.hasPermission("catchball.use.bypass.insideisland")) && !(player.isOp())){
                        if(!CatchBall.configHandler.getNotInsideIslandMessage().equalsIgnoreCase(""))
                            player.sendMessage(CatchBall.configHandler.getNotInsideIslandMessage());
                        if(CatchBall.configHandler.noPermissionSoundEnabled()){
                            if(CatchBall.configHandler.getNoPermissionSound() != null)
                                player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                        }
                        return;
                    }
                }
            }
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();


        if(CleanMobEggHandler.isMobEggItem(itemStack)){
            if(CleanMobEggHandler.isFilledMobEggItem(itemStack)){
                if(!(CatchBall.configHandler.getAlreadyFilledMessage().equalsIgnoreCase(""))){
                    player.sendMessage(CatchBall.configHandler.getAlreadyFilledMessage());
                }
                if(CatchBall.configHandler.noPermissionSoundEnabled()){
                    if(CatchBall.configHandler.getNoPermissionSound() != null){
                        player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                    }
                }
                event.setCancelled(true);
            }else{
                JSONObject jsonObject = JsonHandler.serializeLivingEntity((LivingEntity) event.getRightClicked());
                MobEggCreator mobEggCreator = new MobEggCreator(jsonObject, player.getInventory().getItemInMainHand(), CatchBall.plugin, event.getRightClicked().getPersistentDataContainer());
                ItemStack mobEggItem = mobEggCreator.getMobEggItem();
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                handleItemGive(player, mobEggItem);
                event.getRightClicked().remove();
                if(!(CatchBall.configHandler.getCatchedMessage().equalsIgnoreCase(""))){
                    player.sendMessage(CatchBall.configHandler.getCatchedMessage());
                }
                event.setCancelled(true);
            }
        }
    }

    public void handleItemGive(Player player, ItemStack itemStack){
        Bukkit.getScheduler().runTaskLater(CatchBall.plugin, new Runnable() {
            @Override
            public void run() {
                player.getInventory().addItem(itemStack);
                if(CatchBall.configHandler.catchSoundEnabled()){
                    if(CatchBall.configHandler.getCatchSound() != null){
                        player.playSound(player.getLocation(), CatchBall.configHandler.getCatchSound(), 1.0f, 1.0f);
                    }
                }
            }
        }, 10);
    }
    private boolean isPermitted(Player player){
        Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island.hasPermission(superiorPlayer, CatchBall.fangballUsePermission);
    }
}