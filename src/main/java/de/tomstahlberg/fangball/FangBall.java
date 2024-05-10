package de.tomstahlberg.fangball;

import de.tomstahlberg.fangball.commands.CommandsMain;
import de.tomstahlberg.fangball.commands.CommandsMainTabCompleter;
import de.tomstahlberg.fangball.events.InteractEntity;
import de.tomstahlberg.fangball.events.InteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class FangBall extends JavaPlugin {
    public static Plugin plugin;
    /*
    * TODO
    * Fangball prüfen ob einweg oder mehrweg, beim Platzieren dann leeres Mehrweg bekommen oder
    * Einweg nehmen,
    *
    * Gefüllten Fangball DisplayName und Lore anpassen
    *
    * Wenn man ein Mob platziert, dann sicherstellen, das das neue Item clear ist..
    * */
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new InteractEntity(), this);

        getServer().getPluginCommand("fangball").setExecutor(new CommandsMain());
        getServer().getPluginCommand("fangball").setTabCompleter(new CommandsMainTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
