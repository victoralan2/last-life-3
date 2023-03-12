package net.olimpium.last_life_iii.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.discordBot.Bot;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class VerificationSystem implements Listener {
    public static World verifyWorld = new WorldCreator("verify_lobby").createWorld();
    public static World normalWorld = Bukkit.getWorld("Last Life 3");
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getServer().getWorlds().add(verifyWorld);
        if (isVerified(e.getPlayer().getName())) {
            if (TimeSystem.getFixedTime() == 0){
                e.getPlayer().sendTitle("", ChatColor.RED +"El survival no ha empezado aún", 5, 100, 5);
                e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0f);

                for (Player player : verifyWorld.getPlayers()){
                    for (Player player2hide : verifyWorld.getPlayers()){
                        if (player!=player2hide){
                            e.getPlayer().hidePlayer(Last_life_III.getPlugin(), player2hide);
                            player.hidePlayer(Last_life_III.getPlugin(), player2hide);
                            player2hide.hidePlayer(Last_life_III.getPlugin(), player);
                        }
                    }
                }
                normalWorld.setTime(0);
                normalWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                e.getPlayer().teleport(new Location(verifyWorld, 0.5, 11, 0.5));
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
                verifyWorld.setDifficulty(Difficulty.PEACEFUL);
            } else {
                if (e.getPlayer().getWorld().getName().equals(verifyWorld.getName())){
                    e.getPlayer().teleport(normalWorld.getSpawnLocation());
                    e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                    e.getPlayer().getInventory().clear();
                    e.getPlayer().setGameMode(GameMode.SURVIVAL);
                }
            }
        } else {
            e.getPlayer().sendTitle( "",ChatColor.RED +"No estas verificado", 5, 200, 5);
            e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0f);
            for (Player player : verifyWorld.getPlayers()){
                for (Player player2hide : verifyWorld.getPlayers()){
                    if (player!=player2hide){
                        e.getPlayer().hidePlayer(Last_life_III.getPlugin(), player2hide);
                        player.hidePlayer(Last_life_III.getPlugin(), player2hide);
                        player2hide.hidePlayer(Last_life_III.getPlugin(), player);
                    }
                }
            }
            e.getPlayer().teleport(new Location(verifyWorld, 0.5, 11, 0.5));
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            verifyWorld.setDifficulty(Difficulty.PEACEFUL);
        }
    }
    public static void lastLifeStarted(){
        normalWorld.setTime(0);
        normalWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        for (Player player : Bukkit.getOnlinePlayers()){
            if (isVerified(player.getName())){
                player.teleport(normalWorld.getSpawnLocation());
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                for (Player playerToShow : Bukkit.getOnlinePlayers()){
                    if (player == playerToShow) continue;
                    player.showPlayer(Last_life_III.getPlugin(), player);
                    playerToShow.showPlayer(Last_life_III.getPlugin(), player);
                }
            }
        }
    }
    public static boolean isVerified(String name){
        for(Member member : Bot.bot.getGuildById("913493619875385425").getMembers()){
            if (!member.getEffectiveName().equals(name)) continue;
            if (!member.getRoles().isEmpty()) return true;
        }
        return false;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        if (!isVerified(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("Necesitas estar verificado para hablar");
        }
    }
    public static void playerVerified(Player player){
        player.kickPlayer(ChatColor.GREEN + "Has sido verificado correctamente!");
    }
}
