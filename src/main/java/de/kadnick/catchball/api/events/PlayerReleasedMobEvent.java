package de.kadnick.catchball.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReleasedMobEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final LivingEntity entity;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public PlayerReleasedMobEvent(Player player, LivingEntity entity) {
        this.player = player;
        this.entity = entity;
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
