package de.kadnick.catchball.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        yamlConfiguration.set("items.single","MAGMA_CREAM");
        yamlConfiguration.set("items.multi","MAGMA_CREAM");
        yamlConfiguration.set("sounds.catchSound","ENTITY_TURTLE_EGG_CRACK");
        yamlConfiguration.set("sounds.releaseSound","BLOCK_BREWING_STAND_BREW");
        yamlConfiguration.set("sounds.noPermissionSound", "ENTITY_VILLAGER_NO");
        yamlConfiguration.set("hooks.SuperiorSkyBlock2.enabled",false);
        yamlConfiguration.set("hooks.SuperiorSkyBlock2.onlyUseOnIslandIfAllowed",false);
        List<String> singleEmptyLoreList = new ArrayList<>();
        singleEmptyLoreList.add("§e* §9This is an empty single usable catchball.");
        List<String> singleFilledLoreList = new ArrayList<>();
        singleFilledLoreList.add("§e* §9This is a filled single usable catchball.");
        singleFilledLoreList.add("§e* §2Type §e>> §a%type%");
        singleFilledLoreList.add("§e* §2Nickname §e>> §a%nick%");
        List<String> multiEmptyLoreList = new ArrayList<>();
        multiEmptyLoreList.add("§e* §9This is an empty multi usable catchball.");
        List<String> multiFilledLoreList = new ArrayList<>();
        multiFilledLoreList.add("§e* §9This is a filled multi usable catchball.");
        multiFilledLoreList.add("§e* §2Type §e>> §a%type%");
        multiFilledLoreList.add("§e* §2Nickname §e>> §a%nick%");
        yamlConfiguration.set("lore.singleEmpty", singleEmptyLoreList);
        yamlConfiguration.set("lore.singleFilled", singleFilledLoreList);
        yamlConfiguration.set("lore.multiEmpty", multiEmptyLoreList);
        yamlConfiguration.set("lore.multiFilled", multiFilledLoreList);
        yamlConfiguration.set("name.singleEmpty", "§3CatchBall §7(§fSingle§7)");
        yamlConfiguration.set("name.singleFilled", "§3CatchBall §7(§fSingle§7) §7(§8%type%§7)");
        yamlConfiguration.set("name.multiEmpty", "§3CatchBall §7(§5Multi§7)");
        yamlConfiguration.set("name.multiFilled", "§3CatchBall §7(§5Multi§7) §7(§8%type%§7)");
        yamlConfiguration.set("nick.noNick", "§c-");
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
        yamlConfiguration.set("prefix","§6§lCatch§3§lBall §8x ");
        yamlConfiguration.set("messages.mobCatched","§2You catched a mob.");
        yamlConfiguration.set("messages.playerNotOnline","§7The player §e%player% §7is not online.");
        yamlConfiguration.set("messages.singleName","§fSingle");
        yamlConfiguration.set("messages.multiName","§5Multi");
        yamlConfiguration.set("messages.receivedSingle","§2You have received §e%amount% §2x §f%type% §2catchballs.");
        yamlConfiguration.set("messages.receivedMulti","§2You have received §e%amount% §2x §5%type% §2catchballs.");
        yamlConfiguration.set("messages.sent","§2Catchballs §7(§e%amount% §7x §e%type%§7) §2successfully have been sent to §e%player%§2.");
        yamlConfiguration.set("messages.mobReleased","§2You released a mob.");
        yamlConfiguration.set("messages.alreadyHasMob","§7This catchball already has a mob.");
        yamlConfiguration.set("messages.notInsideIsland","§7You are not allowed to use catchballs outside of an island.");
        yamlConfiguration.set("messages.notInAllowedWorld","§7You are not allowed to use catchballs in this world.");
        yamlConfiguration.set("messages.noPermissionOnIsland","§7You are not allowed to use catchballs on this island.");
        yamlConfiguration.set("messages.noPermission","§7You are not allowed to use catchballs.");
        yamlConfiguration.set("messages.noPermissionEntity","§7You are not allowed to use catchballs of type %type%.");
        List<String> helpList = new ArrayList<>();
        helpList.add("§c-------------------------");
        helpList.add("§2  Catchball      Info");
        helpList.add("§3/catchball give <player> <single|multi> <amount>");
        helpList.add("§c-------------------------");
        yamlConfiguration.set("messages.helpList",helpList);
        yamlConfiguration.set("messages.wrongSyntax","§7Wrong syntax! Type §e/catchball help §7for help..");

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
            Bukkit.getServer().getConsoleSender().sendMessage("§cCatchBall -> Configured Catch-Sound "+this.configuration.getString("sounds.catchSound")+" doesnt exist.");
            return null;
        }
    }
    public Sound getReleaseSound(){
        try {
            return Sound.valueOf(this.configuration.getString("sounds.releaseSound"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cCatchBall -> Configured Release-Sound "+this.configuration.getString("sounds.releaseSound")+" doesnt exist.");
            return null;
        }
    }
    public Sound getNoPermissionSound(){
        try {
            return Sound.valueOf(this.configuration.getString("sounds.noPermissionSound"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cCatchBall -> Configured No-Permission-Sound "+this.configuration.getString("sounds.catchSound")+" doesnt exist.");
            return null;
        }
    }
    public Material getMaterialSingle(){
        try{
            return Material.getMaterial(this.configuration.getString("items.single"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cCatchBall -> Configured Item Material for Single "+this.configuration.getString("items.single")+" doesnt exist.");
            return null;
        }
    }
    public Material getMaterialMulti(){
        try{
            return Material.getMaterial(this.configuration.getString("items.multi"));
        }catch (Exception e){
            Bukkit.getServer().getConsoleSender().sendMessage("§cCatchBall -> Configured Item Material for Single "+this.configuration.getString("items.multi")+" doesnt exist.");
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
    public String getNoPermissionEntityMessage(Entity entity){
        String message = this.language.getString("messages.noPermissionEntity");
        message = message.replace("%type%", entity.getType().name());
        return message;
    }
    public String getReceivedSingleMessage(String type, int amount){
        String receiveMessage = this.language.getString("messages.receivedSingle");
        receiveMessage = receiveMessage.replace("%amount%", ""+amount);
        receiveMessage = receiveMessage.replace("%type%", type);
        return this.language.getString("prefix")+receiveMessage;
    }
    public String getReceivedMultiMessage(String type, int amount){
        String receiveMessage = this.language.getString("messages.receivedMulti");
        receiveMessage = receiveMessage.replace("%amount%", ""+amount);
        receiveMessage = receiveMessage.replace("%type%", type);
        return this.language.getString("prefix")+receiveMessage;
    }
    public String getPlayerNotOnlineMessage(String player){
        String message = this.language.getString("messages.playerNotOnline");
        message = message.replace("%player%", player);
        return this.language.getString("prefix")+message;
    }
    public String getSentMessage(Player player, Integer amount, String type){
        String playerName = player.getName();
        String message = this.language.getString("messages.sent");
        message = message.replace("%player%", playerName);
        message = message.replace("%amount%", amount.toString());
        message = message.replace("%type%", type);
        return message;
    }
    public List<String> getHelpListMessage(){
        return language.getStringList("messages.helpList");
    }
    public String getWrongSyntaxMessage(){
        return this.language.getString("prefix")+this.language.getString("messages.wrongSyntax");
    }
    public String getSingleName(){
        return this.language.getString("messages.singleName");
    }
    public String getMultiName(){
        return this.language.getString("messages.multiName");
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
