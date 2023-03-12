package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.TimeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class CloseCommand {
    @SubscribeEvent
    public void onCloseCommand(SlashCommandInteractionEvent event){
        if(!event.getName().equals("maintenance")) return;
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
        if(!TimeSystem.getIsInMaintenance()) {
            TimeSystem.setIsInMaintenance(true);

            if(event.getOption("tiempo")==null){

                Bot.serverMaintenance(true, null);
                event.reply("Last Life has been closed to the public").setEphemeral(true).queue();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp()) continue;
                            player.kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA EN MANTENIMIENTO\n" + ChatColor.YELLOW + " Consulte #anuncios para saber más información.");
                        }
                    }
                }.runTask(Last_life_III.getPlugin());

            }else{
                Bot.serverMaintenance(true, Objects.requireNonNull(event.getOption("tiempo")).getAsInt());
                event.reply("Last Life has been closed to the public, estimated time: "+ Objects.requireNonNull(event.getOption("tiempo")).getAsInt()+" minutes").setEphemeral(true).queue();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp()) continue;
                            player.kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA EN MANTENIMIENTO" + "\n" + ChatColor.GOLD + "TIEMPO ESPERADO: " + ChatColor.BOLD + Objects.requireNonNull(event.getOption("tiempo")).getAsInt() + " MINUTOS" + ChatColor.YELLOW + ChatColor.ITALIC + "\nConsulte #anuncios para poder saber más información.");
                        }
                    }
                }.runTask(Last_life_III.getPlugin());

            }
        }else{
            TimeSystem.setIsInMaintenance(false);
            Bot.serverMaintenance(false, null);
        }
    }
}
