package de.kadnick.catchball.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCatchingMobEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final LivingEntity entity;
    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    public PlayerCatchingMobEvent(Player player, LivingEntity entity) {
        this.player = player;
        this.entity = entity;
        this.isCancelled = false;
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
    public LivingEntity getEntity(){
        return this.entity;
    }
}
