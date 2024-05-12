package de.tomstahlberg.fangball.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    File configFile;
    File languageFile;
    YamlConfiguration configuration;
    YamlConfiguration language;
    public ConfigHandler(Plugin plugin) throws IOException {
        this.configFile = new File(plugin.getDataFolder(), "settings.yml");
        this.languageFile = new File(plugin.getDataFolder(), "language.yml");
        handleConfiguration();
        handleLanguage();
    }
    public YamlConfiguration getConfig(){
        return this.configuration;
    }
    public YamlConfiguration getLanguage(){
        return this.language;
    }
    private void handleConfiguration() throws IOException {
        if(this.configFile.exists()){
            this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
            return;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("","# Plugin made by Kadnick");
        yamlConfiguration.set("","# See documentation https://blablub.de/blablub");
        yamlConfiguration.set("sounds.catchSound","ENTITY_TURTLE_EGG_CRACK");
        yamlConfiguration.set("sounds.releaseSound","BLOCK_BREWING_STAND_BREW");
        yamlConfiguration.set("sounds.noPermissionSound", "ENTITY_VILLAGER_NO");
        yamlConfiguration.set("hooks.SuperiorSkyBlock2",false);
        yamlConfiguration.set("hooks.SuperiorSkyBlock2.onlyUseOnIslandIfAllowed",false);

        List<String> allowedWorlds = new ArrayList<>();
        allowedWorlds.add("world");
        yamlConfiguration.set("worlds",allowedWorlds);
        this.configuration = yamlConfiguration;
        yamlConfiguration.save(this.configFile);

    }
    private void handleLanguage() throws IOException {
        if(this.languageFile.exists()){
            this.language = YamlConfiguration.loadConfiguration(this.languageFile);
            return;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("","# Plugin made by Kadnick");
        yamlConfiguration.set("","# See documentation https://blablub.de/blablub");
        yamlConfiguration.set("prefix","§6§lGolden§3§lSky §8x");
        yamlConfiguration.set("messages.mobCatched","§6§lGolden§3§lSky §8x §2Du hast ein Mob eingefangen.");
        yamlConfiguration.set("messages.mobReleased","§6§lGolden§3§lSky §8x §2Du hast ein Mob freigelassen.");
        yamlConfiguration.set("messages.alreadyHasMob","§6§lGolden§3§lSky §8x §7Der FangBall hat bereits ein Mob.");
        yamlConfiguration.set("messages.notInAllowedWorld","§6§lGolden§3§lSky §8x §7In dieser Welt darfst du keine Fangbälle benutzen.");
        yamlConfiguration.set("messages.noPermissionOnIsland","§6§lGolden§3§lSky §8x §7Auf dieser Insel darfs du keine Fangbälle benutzen.");
        this.language = yamlConfiguration;
        yamlConfiguration.save(this.languageFile);
    }
    public boolean catchSoundEnabled(){
        if(this.configuration.getString("sounds.catchSound").equalsIgnoreCase("") ||
                this.configuration.getString("sounds.catchSound").equalsIgnoreCase("null") ||
                this.configuration.getString("sounds.catchSound").equalsIgnoreCase("false") ||
                this.configuration.get("sounds.catchSound") == null
        ){
            return false;
        }else{
            return true;
        }
    }
    public boolean releaseSoundEnabled(){
        if(this.configuration.getString("sounds.releaseSound").equalsIgnoreCase("") ||
                this.configuration.getString("sounds.releaseSound").equalsIgnoreCase("null") ||
                this.configuration.getString("sounds.releaseSound").equalsIgnoreCase("false") ||
                this.configuration.get("sounds.catchSound") == null
        ){
            return false;
        }else{
            return true;
        }
    }
    public boolean noPermissionSoundEnabled(){
        if(this.configuration.getString("sounds.noPermissionSound").equalsIgnoreCase("") ||
                this.configuration.getString("sounds.noPermissionSound").equalsIgnoreCase("null") ||
                this.configuration.getString("sounds.noPermissionSound").equalsIgnoreCase("false") ||
                this.configuration.get("sounds.noPermissionSound") == null
        ){
            return false;
        }else{
            return true;
        }
    }
    public Sound getCatchSound(){
        try {
            return Sound.valueOf(this.configuration.getString("sounds.catchSound"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cFangBall -> Configured Catch-Sound "+this.configuration.getString("sounds.catchSound")+" doesnt exist.");
            return null;
        }
    }
    public Sound getReleaseSound(){
        try {
            return Sound.valueOf(this.configuration.getString("sounds.releaseSound"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cFangBall -> Configured Release-Sound "+this.configuration.getString("sounds.releaseSound")+" doesnt exist.");
            return null;
        }
    }
    public Sound getNoPermissionSound(){
        try {
            return Sound.valueOf(this.configuration.getString("sounds.noPermissionSound"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cFangBall -> Configured No-Permission-Sound "+this.configuration.getString("sounds.catchSound")+" doesnt exist.");
            return null;
        }
    }
    public boolean isSuperiorSkyBlockHookEnabled(){
        return this.configuration.getBoolean("hooks.SuperiorSkyBlock2");
    }
    public boolean isSuperiorSkyBlockUsingOnlyOnIslandsIfAllowedEnabled(){
        return this.configuration.getBoolean("hooks.SuperiorSkyBlock2.onlyUseOnIslandIfAllowed");
    }
    public List<World> getAllowedWorlds(){
        List<World> worlds = new ArrayList<>();
        for(String worldString : this.configuration.getStringList("worlds")){
            worlds.add(Bukkit.getWorld(worldString));
        }
        return worlds;
    }
    public String getCatchedMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.mobCatched");
    }
    public String getReleasedMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.mobReleased");
    }
    public String getAlreadyFilledMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.alreadyHasMob");
    }
    public String getNotAllowedWorldMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.notInAllowedWorld");
    }
    public String getNoPermissionOnIslandMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.noPermissionOnIsland");
    }
}
