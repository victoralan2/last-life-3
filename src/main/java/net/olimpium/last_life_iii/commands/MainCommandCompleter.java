package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.Items.LastItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        } else if (args.length == 2) {
            switch (args[0]) {
                case "give":
                    for (LastItem lastItem : LastItem.values()) {
                        subCommands.add(lastItem.toString());
                    }
            }
            for (String sub : subCommands){
                goodSubCommands.add(sub);
                if (!sub.toLowerCase().contains(args[1].toLowerCase())){
                    goodSubCommands.remove(sub);
                }
            }
            return goodSubCommands;
        }
        return null;
    }
}
