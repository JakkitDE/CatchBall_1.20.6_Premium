package de.tomstahlberg.fangball.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PermissionHandler {
    private String permission;
    private Player player;
    public PermissionHandler(LivingEntity entity, Player player){
        this.permission = "fangball.use.mob."+entity.getType().name().toLowerCase();
        this.player = player;
    }
    public boolean isPermitted(){
        if(this.player.hasPermission(this.permission) || this.player.hasPermission("fangball.use.mob.*") || player.isOp())
            return true;
        return false;
    }
    public String getPermission(){
        return this.permission;
    }
}
