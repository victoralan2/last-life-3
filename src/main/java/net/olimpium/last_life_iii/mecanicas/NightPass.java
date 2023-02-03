package net.olimpium.last_life_iii.mecanicas;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NightPass implements Listener {
    public boolean allPlayersSleeping = false;
    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent e){
        Player player = e.getPlayer();
        World world = player.getWorld();

        for (Player p : Bukkit.getOnlinePlayers()){
            if (p != player)
                if (p.isSleeping())
                    allPlayersSleeping = true;

        }
        if (allPlayersSleeping == false)
            return;
        player.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        new BukkitRunnable(){
            @Override
            public void run(){
                if (!allPlayersSleeping)
                    this.cancel();
                world.setTime(world.getTime()+20);
            }
        }.runTaskTimer(Last_life_III.getPlugin(), 0, 1);

    }
    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent e){
        boolean allPlayersSleeping2 = false;
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.isSleeping())
                allPlayersSleeping2 = true;
        }
        if (allPlayersSleeping2){
          //  e.setCancelled(true);
        } else {
            allPlayersSleeping = false;
            e.getPlayer().getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        }
    }
}
