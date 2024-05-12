package de.tomstahlberg.fangball;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.hooks.PermissionsProvider;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import de.tomstahlberg.fangball.commands.CommandsMain;
import de.tomstahlberg.fangball.commands.CommandsMainTabCompleter;
import de.tomstahlberg.fangball.configuration.ConfigHandler;
import de.tomstahlberg.fangball.events.InteractEntity;
import de.tomstahlberg.fangball.events.InteractEvent;
import de.tomstahlberg.fangball.events.RegisterPermissions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class FangBall extends JavaPlugin {
    public static Plugin plugin;
    public static ConfigHandler configHandler;
    public static IslandPrivilege fangballUsePermission;
    /*
    * TODO
    *
    * Wenn man ein Mob platziert, dann sicherstellen, das das neue Item clear ist..
    * */
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        try {
            configHandler = new ConfigHandler(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(configHandler.isSuperiorSkyBlockHookEnabled()){
            if(Bukkit.getServer().getPluginManager().getPlugin("SuperiorSkyblock2") == null){
                Bukkit.getServer().getConsoleSender().sendMessage("§cFangBall -> SuperiorSkyblock2 Hook enabled, but is not installed. Disabling hook for current session....");
                configHandler.disableSuperiorSkyblockHook();
            }else{
                Bukkit.getServer().getConsoleSender().sendMessage("§2FangBall -> SuperiorSkyblock2 Hook enabled and plugin found.");
                //getServer().getPluginManager().registerEvents(new RegisterPermissions(), this);
                IslandPrivilege.register("fangball");
                fangballUsePermission = IslandPrivilege.getByName("fangball");
            }
        }
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
