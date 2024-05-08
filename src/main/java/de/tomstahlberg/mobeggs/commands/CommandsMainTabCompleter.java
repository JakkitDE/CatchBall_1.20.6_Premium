package de.tomstahlberg.mobeggs.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandsMainTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if(commandSender.hasPermission("fangball.admin.command.give") || commandSender.isOp()){
            switch (strings.length){
                case 1:
                    list.add("give");
                    break;
                case 2:
                    for(Player player : Bukkit.getOnlinePlayers()){
                        list.add(player.getName());
                    }
                    break;
                case 3:
                    list.add("single");
                    list.add("multi");
                    break;
                case 4:
                    list.add("1");
                    list.add("8");
                    list.add("16");
                    list.add("32");
                    list.add("64");
                    break;
                default:
                    list.add("error");
            }
        }

        return list;
    }
}
