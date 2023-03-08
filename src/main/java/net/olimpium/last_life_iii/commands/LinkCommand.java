package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.discordBot.Bot;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LinkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = ((Player) commandSender).getPlayer();
            if(args.length == 1){
                boolean tokenFound = Bot.playerRegistered(args[0].toUpperCase(), player);
                if(tokenFound){
                    VerificationSystem.playerVerified(player);
                } else {
                    player.sendMessage(ChatColor.RED+"No se ha encontrado el token");
                }
            }else {
                player.sendMessage(ChatColor.RED+"Porfavor escriba "+ChatColor.BOLD+" UN "+ChatColor.RESET+ChatColor.RED+"token");
            }
        }
        return true;
    }
}