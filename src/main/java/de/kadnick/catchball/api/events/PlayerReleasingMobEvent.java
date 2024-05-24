package de.kadnick.catchball.api.events;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReleasingMobEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final EntityType entityType;
    private boolean isCancelled;
    private Location location;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerReleasingMobEvent(Player player, EntityType entityType, Location location) {
        this.player = player;
        this.entityType = entityType;
        this.isCancelled = false;
        this.location = location;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }
    public EntityType getEntityType(){
        return this.entityType;
    }
    public Location getLocation(){
        return this.location;
    }
}
