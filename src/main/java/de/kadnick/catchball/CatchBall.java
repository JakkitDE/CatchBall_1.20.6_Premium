package de.kadnick.catchball;

import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import de.kadnick.catchball.commands.CommandsMain;
import de.kadnick.catchball.commands.CommandsMainTabCompleter;
import de.kadnick.catchball.configuration.ConfigHandler;
import de.kadnick.catchball.events.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CatchBall extends JavaPlugin {
    public static Plugin plugin;
    public static ConfigHandler configHandler;
    public static IslandPrivilege fangballUsePermission;
    public static ConsoleCommandSender console;
    /*
    * TODO
    *  - Release-1.0.0
    *  - Nothing here, take a look left..
    * */
    @Override
    public void onEnable() {
        console = getServer().getConsoleSender();
        console.sendMessage("§2Enabling CatchBall Plugin... ... ...");
        console.sendMessage("§6||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        console.sendMessage("§f{");
        console.sendMessage("               §7§lInfo");
        console.sendMessage("");
        console.sendMessage("               §7Written by");
        console.sendMessage("                       §c§lKadnick");
        console.sendMessage("");
        console.sendMessage("               §7Version");
        console.sendMessage("                       §e§lv1.0.3-SNAPSHOT");
        console.sendMessage("");
        console.sendMessage("               §7Variant");
        console.sendMessage("                       §b§lPremium");
        console.sendMessage("");
        console.sendMessage("");
        console.sendMessage("               §7§lSetup");
        console.sendMessage("");

        plugin = this;
        try {
            configHandler = new ConfigHandler(this);
            console.sendMessage("               §7language.yml & settings.yml");
            console.sendMessage("                       §2✓§l");
            console.sendMessage("");
        } catch (IOException e) {
            console.sendMessage("               §7ConfigurationHandler");
            console.sendMessage("                       §cx§l");
            console.sendMessage("");
            throw new RuntimeException(e);
        }
        if(configHandler.isSuperiorSkyBlockHookEnabled()){
            getServer().getPluginManager().registerEvents(new PluginInitializeSSB(), this);
            console.sendMessage("               §7SuperiorSkyblock2 Hook");
            console.sendMessage("                       §2✓§l");
            console.sendMessage("");
        }
        getServer().getPluginManager().registerEvents(new InteractEvent(), this);
        getServer().getPluginManager().registerEvents(new InteractEntity(), this);

        /* For development bound testing reasons only */
        // getServer().getPluginManager().registerEvents(new PlayerCatchesMob(), this);
        // getServer().getPluginManager().registerEvents(new PlayerReleasesMob(), this);

        getServer().getPluginCommand("catchball").setExecutor(new CommandsMain());
        getServer().getPluginCommand("catchball").setTabCompleter(new CommandsMainTabCompleter());

        console.sendMessage("");
        console.sendMessage("       §2Enabled");
        console.sendMessage("          §2✓");
        console.sendMessage("§f}");
        console.sendMessage("§6||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        console.sendMessage("§5§lCatch§3§lBall §6-> §2Disabling..");
        console.sendMessage("§5§lCatch§3§lBall §6-> §2Disabled.");
    }
}
