package de.tomstahlberg.mobeggs;

import de.tomstahlberg.mobeggs.commands.CommandsMain;
import de.tomstahlberg.mobeggs.commands.CommandsMainTabCompleter;
import de.tomstahlberg.mobeggs.events.InteractEntity;
import de.tomstahlberg.mobeggs.events.InteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobEggs extends JavaPlugin {
    public static Plugin plugin;
    /*
    * TODO
    * Fangball prüfen ob einweg oder mehrweg, beim Platzieren dann leeres Mehrweg bekommen oder
    * Einweg nehmen,
    *
    * Gefüllten Fangball DisplayName und Lore anpassen
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
