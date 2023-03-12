package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BanCommand {
    @SubscribeEvent
    public void onBanCommand(SlashCommandInteractionEvent event){
        if(!event.getName().equals("mineban")) return;
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;


        Date date = new Date();
        Player player  = Bukkit.getPlayer(event.getOption("usuario").getAsMember().getEffectiveName());
        Member member = event.getOption("usuario").getAsMember();
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(),event.getOption("razón").getAsString(),Date.from(date.toInstant().plus(event.getOption("tiempo").getAsInt(), ChronoUnit.MINUTES)),event.getMember().getEffectiveName());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HAS SIDO BANEADO\n"+ChatColor.RESET+ChatColor.GOLD+ChatColor.BOLD+"Tiempo restante: "+event.getOption("tiempo")+" minutos.\n"+ChatColor.RESET+ChatColor.YELLOW+"Razón: "+event.getOption("razón").getAsString());
            }
        }.runTask(Last_life_III.getPlugin());

        member.getGuild().timeoutFor(member.getUser(),event.getOption("tiempo").getAsInt(),TimeUnit.MINUTES)
                    .reason(event.getOption("razón").getAsString())
                    .queue();

        event.reply("Se ha baneado satifactoriamente a "+member.getAsMention()).setEphemeral(true).queue();
    }
    @SubscribeEvent
    public void onContextBan(UserContextInteractionEvent event){
        if(!event.getName().equals("Banear de Last Life")) return;
        //TO
        event.getGuild().ban(event.getTargetMember().getUser(), 1, TimeUnit.DAYS).queue();
        Bukkit.getBanList(BanList.Type.NAME).addBan(event.getTargetMember().getEffectiveName(), "Has sido baneado", null, event.getMember().getEffectiveName());
        new BukkitRunnable(){
            @Override
            public void run(){
                Bukkit.getServer().getPlayer(event.getTargetMember().getEffectiveName()).kickPlayer(ChatColor.DARK_RED.toString()+ChatColor.BOLD+"Has sido baneado");
            }
        }.runTask(Last_life_III.getPlugin());
        event.reply("Se ha baneado satisfactoriamente a " + event.getTargetMember().getAsMention()).setEphemeral(true).queue();

    }


}