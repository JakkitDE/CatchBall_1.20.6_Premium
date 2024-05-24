package de.kadnick.catchball.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import de.kadnick.catchball.CatchBall;
import de.kadnick.catchball.api.CatchballAPI;
import de.kadnick.catchball.api.events.PlayerReleasedMobEvent;
import de.kadnick.catchball.api.events.PlayerReleasingMobEvent;
import de.kadnick.catchball.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

import java.io.IOException;

public class InteractEvent implements Listener {
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR)
                return;
            if(!(PDCHandler.hasPDCString(CatchBall.plugin,event.getPlayer().getInventory().getItemInMainHand(), "mobdata")))
                return;
            if(event.getHand() == EquipmentSlot.OFF_HAND)
                return;

            if(!(CatchballAPI.isCatchBall(event.getPlayer().getInventory().getItemInMainHand())))
                return;
            //Call PlayerReleasingMobEvent and run if not cancelled
            EntityType type = CatchballAPI.getEntityType(event.getPlayer().getInventory().getItemInMainHand());
            PlayerReleasingMobEvent playerReleasingMobEvent = new PlayerReleasingMobEvent(player, type, event.getInteractionPoint());
            Bukkit.getPluginManager().callEvent(playerReleasingMobEvent);
            if(playerReleasingMobEvent.isCancelled())
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

            //Check if in allowed world
            if(!player.hasPermission("catchball.use.world.bypass") || !player.isOp()){
                if(!CatchBall.configHandler.getAllowedWorlds().contains(player.getWorld())){
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

            //
            //Mob platzieren
            String jsonString = PDCHandler.getPDCString(CatchBall.plugin, player.getInventory().getItemInMainHand(), "mobdata");
            JSONObject jsonObject = JsonHandler.parseJSONObject(jsonString);

            Location location = event.getInteractionPoint();
            LivingEntity entity = JsonHandler.deserializeLivingEntity(jsonObject, location, player.getLocation().getWorld(), CatchBall.plugin);

            PermissionHandler permissionHandler = new PermissionHandler(entity, player);
            if(!permissionHandler.isPermitted()){
                if(!CatchBall.configHandler.getNoPermissionEntityMessage(entity).equalsIgnoreCase(""))
                    player.sendMessage(CatchBall.configHandler.getNoPermissionEntityMessage(entity));
                if(CatchBall.configHandler.noPermissionSoundEnabled()){
                    player.playSound(player.getLocation(), CatchBall.configHandler.getNoPermissionSound(), 1.0f, 1.0f);
                }
                entity.remove();
                return;
            }

            if(PDCHandler.hasPDCBytes(CatchBall.plugin, player.getInventory().getItemInMainHand(), "pdc")){
                byte[] bytes = PDCHandler.getPDCBytes(CatchBall.plugin, player.getInventory().getItemInMainHand(), "pdc");
                entity.getPersistentDataContainer().readFromBytes(bytes);
            }
            /*if(entity instanceof Tameable){
                Tameable tameable = (Tameable) entity;
                if(tameable.isTamed()){
                    tameable.setOwner(player);
                }
            }*/

            /* Check if item is single or multi */
            if(CleanMobEggHandler.isMultiMobEggItem(CatchBall.plugin, player.getInventory().getItemInMainHand())){
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
                player.getInventory().addItem(CleanMobEggHandler.getMultiMobEggItem(1));
            }else{
                InventoryHandler.removeOneItem(player.getInventory().getItemInMainHand());
            }

            if(CatchBall.configHandler.releaseSoundEnabled()){
                if(CatchBall.configHandler.getReleaseSound() != null){
                    player.playSound(player.getLocation(), CatchBall.configHandler.getReleaseSound(), 1.0f, 1.0f);
                }
            }
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);

            //
            if(!(CatchBall.configHandler.getReleasedMessage().equalsIgnoreCase(""))){
                player.sendMessage(CatchBall.configHandler.getReleasedMessage());
            }

            // Call PlayerReleasedMobEvent
            PlayerReleasedMobEvent playerReleasedMobEvent = new PlayerReleasedMobEvent(player, entity);
            Bukkit.getPluginManager().callEvent(playerReleasedMobEvent);
            event.setCancelled(true);
        }
    }
    private boolean isPermitted(Player player){
        Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);
        return island.hasPermission(superiorPlayer, CatchBall.fangballUsePermission);
    }
}
