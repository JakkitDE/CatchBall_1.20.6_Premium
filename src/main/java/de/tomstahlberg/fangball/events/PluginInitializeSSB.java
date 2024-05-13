package de.tomstahlberg.fangball.events;

import com.bgsoftware.superiorskyblock.api.events.PluginInitializeEvent;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PluginInitializeSSB implements Listener {
    @EventHandler
    public void onInitialize(PluginInitializeEvent event){
        IslandPrivilege.register("FANGBALL");
    }
}
