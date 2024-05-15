package de.tomstahlberg.fangball.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
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
        yamlConfiguration.set("sounds.catchSound","ENTITY_TURTLE_EGG_CRACK");
        yamlConfiguration.set("sounds.releaseSound","BLOCK_BREWING_STAND_BREW");
        yamlConfiguration.set("sounds.noPermissionSound", "ENTITY_VILLAGER_NO");
        yamlConfiguration.set("hooks.SuperiorSkyBlock2.enabled",false);
        yamlConfiguration.set("hooks.SuperiorSkyBlock2.onlyUseOnIslandIfAllowed",false);
        List<String> singleEmptyLoreList = new ArrayList<>();
        singleEmptyLoreList.add("§eThis is a empty single use fangball.");
        List<String> singleFilledLoreList = new ArrayList<>();
        singleFilledLoreList.add("§eThis is a filled single use fangball.");
        singleFilledLoreList.add("§eType %type%");
        singleFilledLoreList.add("§eNickname %nick%");
        List<String> multiEmptyLoreList = new ArrayList<>();
        multiEmptyLoreList.add("§eThis is a empty multi use fangball.");
        List<String> multiFilledLoreList = new ArrayList<>();
        multiFilledLoreList.add("§eThis is a filled multi use fangball.");
        multiFilledLoreList.add("§eType %type%");
        multiFilledLoreList.add("§eNickname %nick%");
        yamlConfiguration.set("lore.singleEmpty", singleEmptyLoreList);
        yamlConfiguration.set("lore.singleFilled", singleFilledLoreList);
        yamlConfiguration.set("lore.multiEmpty", multiEmptyLoreList);
        yamlConfiguration.set("lore.multiFilled", multiFilledLoreList);
        yamlConfiguration.set("name.singleEmpty", "§eEmpty Single Use");
        yamlConfiguration.set("name.singleFilled", "§eFilled Single Use (%type%)");
        yamlConfiguration.set("name.multiEmpty", "§eEmpty Multi Use");
        yamlConfiguration.set("name.multiFilled", "§eFilled Multi Use (%type%)");
        yamlConfiguration.set("nick.noNick", "§c/");
        yamlConfiguration.set("shining.filledSingle", true);
        yamlConfiguration.set("shining.filledMulti", true);


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
        yamlConfiguration.set("prefix","§6§lFang§3§lBall §8x");
        yamlConfiguration.set("messages.mobCatched","§2You catched a mob.");
        yamlConfiguration.set("messages.mobReleased","§2You released a mob.");
        yamlConfiguration.set("messages.alreadyHasMob","§7This fangball already has a mob.");
        yamlConfiguration.set("messages.notInsideIsland","§7You are not allowed to use fangballs outside of an island.");
        yamlConfiguration.set("messages.notInAllowedWorld","§7You are not allowed to use fangballs in this world.");
        yamlConfiguration.set("messages.noPermissionOnIsland","§7You are not allowed to use fangballs on this island.");
        yamlConfiguration.set("messages.noPermission","§7You are not allowed to use fangballs.");
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
        return this.configuration.getBoolean("hooks.SuperiorSkyBlock2.enabled");
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
    public String getNotInsideIslandMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.notInsideIsland");
    }
    public String getNotAllowedWorldMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.notInAllowedWorld");
    }
    public String getNoPermissionOnIslandMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.noPermissionOnIsland");
    }
    public String getNoPermissionMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.noPermission");
    }
    public void disableSuperiorSkyblockHook(){
        this.configuration.set("hooks.SuperiorSkyBlock2.enabled",false);
    }
    public List<String> getSingleEmptyLore(){
        return this.configuration.getStringList("lore.singleEmpty");
    }
    public List<String> getSingleFilledLore(){
        return this.configuration.getStringList("lore.singleFilled");
    }
    public List<String> getMultiEmptyLore(){
        return this.configuration.getStringList("lore.multiEmpty");
    }
    public List<String> getMultiFilledLore(){
        return this.configuration.getStringList("lore.multiFilled");
    }
    public String getSingleEmptyName(){
        return this.configuration.getString("name.singleEmpty");
    }
    public String getSingleFilledName(){
        return this.configuration.getString("name.singleFilled");
    }
    public String getMultiEmptyName(){
        return this.configuration.getString("name.multiEmpty");
    }
    public String getMultiFilledName(){
        return this.configuration.getString("name.multiFilled");
    }
    public String getNoNick(){
        return this.configuration.getString("nick.noNick");
    }
    public boolean getShiningSingle(){
        return this.configuration.getBoolean("shining.filledSingle");
    }
    public boolean getShiningMulti(){
        return this.configuration.getBoolean("shining.filledMulti");
    }
}
