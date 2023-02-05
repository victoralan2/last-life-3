package net.olimpium.last_life_iii;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.olimpium.last_life_iii.discordBot.Bot;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class fabricMod implements Listener, PluginMessageListener {
    public static TextChannel botLog = Bot.bot.getTextChannelById("1068967379104694333");

    public static String version = "7B230A4E4AC8972B7276601F6B17DDF5DBE498B4B7C70D9B1A752CF9DAF8B3BB".toLowerCase();
    public static List<UUID> authPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!authPlayers.contains(e.getPlayer().getUniqueId())) {
                    System.out.println(e.getPlayer().getName());
                    authPlayers.remove(e.getPlayer().getUniqueId());
                    e.getPlayer().kickPlayer(ChatColor.RED.toString() + ChatColor.BOLD + "Necesitas LAST LIFE MOD para poder jugar a este servidor.\nSi esto se trata de un error, reintentalo.");
                }
            }
        }.runTaskLater(Last_life_III.getPlugin(), 5);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message){
        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        String data = in.readLine().substring(1);
        String type = data.split("<<SEPARATOR>>")[0];
        String content = data.split("<<SEPARATOR>>")[1];
        System.out.println(data);
        if (!channel.equals("lastlife:packet")) return;

        if (type.equals("version")) {
            System.out.println();
            if(!content.equals(version)) player.kickPlayer(ChatColor.RED.toString()+ChatColor.BOLD+"Tienes otra version de LAST LIFE MOD\n Porfavor actualize el mod.");
            authPlayers.add(player.getUniqueId());



        } else if (type.substring(1).equals("modauth")){
            List<String> allowedMods = List.of("lastlife", "lazydfu", "lithium", "sodium", "phosphor");
            List<String> detectedMods = new ArrayList<>();
            List<String> mods = new ArrayList<>();

            boolean goodSoFar = true;
            if (content.contains("<<<MODSEPARATOR>>>")) {
                for (String mod : content.split("<<<MODSEPARATOR>>>")) {
                    if (mod.endsWith("3cc0f0907d")) continue;
                    if (mod.startsWith("fabric 0.")) continue;
                    mods.add(mod);
                    if (mod.startsWith("fabricloader")) continue;
                    if (mod.startsWith("java") || mod.equals("minecraft 1.16.5")) continue;
                    boolean isAllowed = false;
                    for(String allowedMod : allowedMods){
                        if (mod.toLowerCase().startsWith(allowedMod.toLowerCase())){
                            isAllowed = true;
                            break;
                        }
                    }
                    if (!isAllowed) {
                        detectedMods.add(mod);
                        goodSoFar = false;
                    }
                }
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            if (goodSoFar){
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setTitle(player.getName()+" - Successful join");
            } else {
                aSneekyLittlePlayerHasTryedToCheat(player);
                embedBuilder.setColor(Color.RED);
                embedBuilder.setTitle(player.getName()+" - 3rd party mods detected");
                botLog.sendMessage("@here a player has joined with sussy mods!!!!!!!111").queue();
            }

            List<String> modListList = new ArrayList<>();
            String modList = "";
            for (String mod : mods){
                if (modList.length() >= 900){
                    modListList.add(modList);
                    modList = "";
                }
                if (detectedMods.contains(mod)){
                    modList += "  - __**" + mod + "**__\n";
                }
                else{
                    modList += "  - " + mod + "\n";
                }
            }
            modListList.add(modList);
            int i = 0;
            for (String oneModList : modListList){
                if (i != 0){
                    embedBuilder.addField(" " ,oneModList, false);
                } else {
                    embedBuilder.addField("Mod List: " ,oneModList, false);
                }
                i++;

            }

            botLog.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
    public void aSneekyLittlePlayerHasTryedToCheat(Player cheater){
        cheater.kickPlayer(ChatColor.RED.toString()+ChatColor.BOLD+"Se ha detectado un mod no listado en #mods-permitidos.\nSi crees que esto es un error o que se tendria que añadir un mod a la lista, contaca a los administradores del servidor.\n Se esta investigando tu caso, si los admins no te contactan, contactales tu.");
        Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(cheater.getName(),ChatColor.RED.toString()+ChatColor.BOLD+"\nSe ha detectado un mod no listado en #mods-permitidos.\nSi crees que esto es un error o que se tendria que añadir un mod a la lista, contaca a los administradores del servidor.\n Se esta investigando tu caso, si los admins no te contactan, contactales tu.",null,"The Void");
    }
}

