package de.tomstahlberg.fangball.events;

import com.bgsoftware.superiorskyblock.api.events.PluginInitializeEvent;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RegisterPermissions implements Listener {
    @EventHandler
    public void onInit(PluginInitializeEvent event){
        final String fangBall = "fangball";
        IslandPrivilege.register(fangBall);
    }
}
