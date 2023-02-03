package net.olimpium.last_life_iii.mecanicas;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Screamer {
    public static int timeBetweenScreamers = 10;
    public static void screemerRunable() {


        new BukkitRunnable() {
            @Override
            public void run() {
                timeBetweenScreamers = (new Random()).nextInt(40)+10;
                if (!Bukkit.getOnlinePlayers().isEmpty()){
                    int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
                    Player player = (Player) Bukkit.getOnlinePlayers().toArray()[random];
                    if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 10, 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 10, 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_HURT, 10, 1);
                    } else if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_STARE, 10, 1);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 10, 1);
                    } else if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
                        player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 5, 0);
                    }
                }
            }

        }.runTaskTimer(Last_life_III.getPlugin(), 0, (long) timeBetweenScreamers*20*60);
    }
}
