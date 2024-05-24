package de.kadnick.catchball.commands;

import de.kadnick.catchball.CatchBall;
import de.kadnick.catchball.utils.CleanMobEggHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsMain implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //catchball give <player> <einweg|mehrweg> <anzahl>
        //          1       2           3           4
        //          0       1           2           3
        if(commandSender.hasPermission("catchball.admin.command.give") || commandSender.isOp()){
            if(strings.length == 4){
                if(strings[0].equalsIgnoreCase("give")){

                    if(Bukkit.getPlayer(strings[1]) == null){
                        commandSender.sendMessage(CatchBall.configHandler.getPlayerNotOnlineMessage(strings[1]));
                        return true;
                    }
                    Player player = Bukkit.getPlayer(strings[1]);
                    Integer amount = Integer.valueOf(strings[3]);
                    if(strings[2].equalsIgnoreCase("single")){
                        for(int i = 0;i<amount;i++){
                            player.getInventory().addItem(CleanMobEggHandler.getSingleMobEggItem(1));
                        }
                        player.sendMessage(CatchBall.configHandler.getReceivedSingleMessage(CatchBall.configHandler.getSingleName(),amount));
                        if(Bukkit.getPlayer(strings[1]) != commandSender){
                            commandSender.sendMessage(CatchBall.configHandler.getSentMessage(Bukkit.getPlayer(strings[1]), amount, CatchBall.configHandler.getSingleName()));
                        }
                    }else if(strings[2].equalsIgnoreCase("multi")){
                        for(int i = 0;i<amount;i++){
                            player.getInventory().addItem(CleanMobEggHandler.getMultiMobEggItem(1));
                        }
                        player.sendMessage(CatchBall.configHandler.getReceivedMultiMessage(CatchBall.configHandler.getMultiName(),amount));
                        if(Bukkit.getPlayer(strings[1]) != commandSender){
                            commandSender.sendMessage(CatchBall.configHandler.getSentMessage(Bukkit.getPlayer(strings[1]), amount, CatchBall.configHandler.getMultiName()));
                        }
                    }else{
                        commandSender.sendMessage(CatchBall.configHandler.getWrongSyntaxMessage());
                    }
                }else if (strings[0].equalsIgnoreCase("help")){
                    showHelp(commandSender);
                }else{
                    commandSender.sendMessage(CatchBall.configHandler.getWrongSyntaxMessage());
                }
            }else if(strings.length == 1 && strings[0].equalsIgnoreCase("help")){
                showHelp(commandSender);
            }else{
                commandSender.sendMessage(CatchBall.configHandler.getWrongSyntaxMessage());
            }
        }


        return true;
    }

    private void showHelp(CommandSender sender){
        for(String message : CatchBall.configHandler.getHelpListMessage()){
            sender.sendMessage(message);
        }
    }
}
