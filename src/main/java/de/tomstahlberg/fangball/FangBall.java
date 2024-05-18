package de.tomstahlberg.fangball;

import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import de.tomstahlberg.fangball.commands.CommandsMain;
import de.tomstahlberg.fangball.commands.CommandsMainTabCompleter;
import de.tomstahlberg.fangball.configuration.ConfigHandler;
import de.tomstahlberg.fangball.events.InteractEntity;
import de.tomstahlberg.fangball.events.InteractEvent;
import de.tomstahlberg.fangball.events.PluginInitializeSSB;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class FangBall extends JavaPlugin {
    public static Plugin plugin;
    public static ConfigHandler configHandler;
    public static IslandPrivilege fangballUsePermission;
    /*
    * TODO
    *  Fix Warning
    * [02:56:58 WARN]: [EntityLookup] Failed to remove entity Villager['Librarian'/272, uuid='c3bb607b-8ec0-456e-b6f7-a6c2141120bf', l='ServerLevel[SuperiorWorld]', x=607.82, y=100.00, z=-0.50, cpos=[37, -1], tl=17218, v=false, removed=UNLOADED_TO_CHUNK] by uuid, current entity mapped: null
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
            getServer().getPluginManager().registerEvents(new PluginInitializeSSB(), this);
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
