package de.tomstahlberg.mobeggs;

import de.tomstahlberg.mobeggs.events.InteractEntity;
import de.tomstahlberg.mobeggs.events.InteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobEggs extends JavaPlugin {
    public static Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new InteractEntity(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
