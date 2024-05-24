package de.kadnick.catchball.events;

import de.kadnick.catchball.api.events.PlayerCatchingMobEvent;
import de.kadnick.catchball.api.events.PlayerReleasingMobEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerReleasesMob implements Listener {
    @EventHandler
    public void onCatch(PlayerReleasingMobEvent event){
        /* nothing so see here */
    }
}
