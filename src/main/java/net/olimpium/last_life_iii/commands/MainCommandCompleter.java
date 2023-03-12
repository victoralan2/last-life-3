package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.advancements.AdvancementManager;
import net.olimpium.last_life_iii.items.LastItem;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> subCommands = new ArrayList<>();
        List<String> goodSubCommands = new ArrayList<>();
        if (args.length == 1) {
            subCommands.add("start");
            subCommands.add("close");
            subCommands.add("open");
            subCommands.add("team");
            subCommands.add("trustadd");
            subCommands.add("trustrem");
            subCommands.add("help");
            subCommands.add("give");
            subCommands.add("advancement");
            try {
                for (String sub : subCommands){
                    goodSubCommands.add(sub);
                    if (!sub.toLowerCase().contains(args[0].toLowerCase())){
                        goodSubCommands.remove(sub);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return goodSubCommands;
        } else if (args.length >= 2) {
            switch (args[0]) {
                case "give":
                    for (LastItem lastItem : LastItem.values()) {
                        subCommands.add(lastItem.toString());
                    }
                    break;

                case "advancement":
                    if (args.length == 2){
                        subCommands.add("grant");
                        subCommands.add("revoke");
                    } else {
                        System.out.println(args.length);
                        switch (args[1]){
                            case "grant": {
                               if (args.length == 3){
                                   for (Player player : Bukkit.getOnlinePlayers()){
                                       System.out.println(player.getName());
                                       subCommands.add(player.getName());
                                   }
                                } else if (args.length > 3){
                                    subCommands.add("*");
                                    Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
                                    while (iterator.hasNext()){
                                        Advancement advancement = iterator.next();
                                        if (advancement.getKey().getKey().contains("recipes/")) continue;
                                        subCommands.add(advancement.getKey().getNamespace() + ":" + advancement.getKey().getKey());
                                    }
                                    for (com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement : AdvancementManager.advancementList){
                                        subCommands.add(advancement.getKey().getNamespace() + ":" + advancement.getKey().getKey());
                                    }
                                }
                            }
                            case "revoke":{
                                if (args.length == 3){
                                    for (Player player : Bukkit.getOnlinePlayers()){
                                        subCommands.add(player.getName());
                                    }
                                } else if (args.length > 3){
                                    subCommands.add("*");
                                    Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
                                    while (iterator.hasNext()){
                                        Advancement advancement = iterator.next();
                                        if (advancement.getKey().getKey().contains("recipes/")) continue;

                                        subCommands.add(advancement.getKey().getNamespace() + ":" + advancement.getKey().getKey());
                                    }
                                    for (com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement : AdvancementManager.advancementList){
                                        subCommands.add(advancement.getKey().getNamespace() + ":" + advancement.getKey().getKey());
                                    }
                                }
                            }
                        }
                        for (String sub : subCommands){
                            goodSubCommands.add(sub);
                            if (!sub.toLowerCase().contains(args[2].toLowerCase())){
                                goodSubCommands.remove(sub);
                            }
                        }
                    }
            }
            for (String sub : subCommands){
                goodSubCommands.add(sub);
                if (!sub.toLowerCase().contains(args[args.length-1].toLowerCase())){
                    goodSubCommands.remove(sub);
                }
            }
            return goodSubCommands;
        }
        return null;
    }
}
