package net.olimpium.last_life_iii.HealthSystem;


import net.olimpium.last_life_iii.discordBot.Bot;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LifeSystem implements Listener{
    @EventHandler
    public void onPlayerDie(PlayerDeathEvent e) {
        Player player = e.getEntity();
        double Health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (Health > 2) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Health-2);
            for (Player allplayers : Bukkit.getOnlinePlayers()){
                allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_BLAZE_DEATH,100000, 0);
                allplayers.sendTitle(ChatColor.GOLD + "-=|" + ChatColor.RED + player.getPlayer().getName() + ChatColor.RED + " HA MUERTO" + ChatColor.GOLD + "|=-",ChatColor.GOLD + "-=| " + ChatColor.DARK_PURPLE + ((Health / 2) - 1) +  " corazones restantes"+ ChatColor.GOLD + " |=-", 1, 100, 5);
            }
        } else {
            if (player.getGameMode() != GameMode.SPECTATOR) {
                Bot.playerDeath(player,"");
                player.setGameMode(GameMode.SPECTATOR);
                for (Player allplayers : Bukkit.getOnlinePlayers()){
                    allplayers.playSound(allplayers.getLocation(), Sound.ENTITY_WITHER_DEATH,10, 1);
                    allplayers.sendTitle(ChatColor.GOLD + "-=|" + ChatColor.RED + player.getPlayer().getName() + ChatColor.RED + " HA MUERTO" + ChatColor.GOLD + "|=-",ChatColor.GOLD + "-=| " + ChatColor.YELLOW + "PARA SIEMPRE"+ChatColor.GOLD + " |=-", 1, 100, 5);
                }
            }
        }
    }
}
