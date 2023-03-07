package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.olimpium.last_life_iii.utils.TimeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CloseCommand {
    @SubscribeEvent
    public void onCloseCommand(SlashCommandInteractionEvent event){

        // TODO: hazer esto
        if(!event.getName().equals("maintenance")) return;
        if(!TimeSystem.getIsInMaintenance()) {
            TimeSystem.setIsInMaintenance(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) continue;
                    player.kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA EN MANTENIMIENTO\n" + ChatColor.YELLOW + " Consulte #anuncios para saber más información.");
            }
        } else{
            TimeSystem.setIsInMaintenance(false);
            Bot.serverMaintenance(false, null);
        }
        if(event.getOption("tiempo") != null){
            Bot.serverMaintenance(true, event.getOption("tiempo").getAsInt());
        }else{
            Bot.serverMaintenance(true, null);
        }
    }
}
