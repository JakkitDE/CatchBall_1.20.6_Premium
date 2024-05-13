package de.tomstahlberg.fangball.commands;

import de.tomstahlberg.fangball.utils.CleanMobEggHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandsMain implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //fangball give <player> <einweg|mehrweg> <anzahl>
        //          1       2           3           4
        //          0       1           2           3
        if(commandSender.hasPermission("fangball.admin.command.give") || commandSender.isOp()){
            if(strings.length == 4){
                if(strings[0].equalsIgnoreCase("give")){

                    Player player = Bukkit.getPlayer(strings[1]);
                    Integer amount = Integer.valueOf(strings[3]);
                    if(strings[2].equalsIgnoreCase("single")){
                        for(int i = 0;i<amount;i++){
                            player.getInventory().addItem(CleanMobEggHandler.getSingleMobEggItem(1));
                        }
                        if(amount >1){
                            player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast soeben "+amount+" Einweg Fangbälle erhalten.");
                        }else{
                            player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast soeben "+amount+" Einweg Fangball erhalten.");
                        }

                    }else if(strings[2].equalsIgnoreCase("multi")){
                        for(int i = 0;i<amount;i++){
                            player.getInventory().addItem(CleanMobEggHandler.getMultiMobEggItem(1));
                        }
                        if(amount >1){
                            player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast soeben "+amount+" §5Mehrweg §2Fangbälle erhalten.");
                        }else{
                            player.sendMessage("§6§lGolden§3§lSky §8x §2Du hast soeben "+amount+" §5Mehrweg §2Fangball erhalten.");
                        }
                    }else{
                        commandSender.sendMessage("§6§lGolden§3§lSky §8x §cFalsches Syntax!");
                        showHelp(player);
                    }
                }
            }else{
                showHelp(commandSender);
            }
        }


        return true;
    }

    private void showHelp(CommandSender sender){
        sender.sendMessage("§c-------------------------");
        sender.sendMessage("§2  Fangball      Info");
        sender.sendMessage("§3/fangball give <player> <single|multi> <amount>");
        sender.sendMessage("§c-------------------------");
    }
}
