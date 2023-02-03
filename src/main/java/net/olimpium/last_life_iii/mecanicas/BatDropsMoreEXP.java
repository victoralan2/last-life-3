package net.olimpium.last_life_iii.mecanicas;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDeathEvent;


public class BatDropsMoreEXP implements Listener {
    @EventHandler
    public void onBatKilled(EntityDeathEvent e){
        if(e.getEntity().getType().equals(EntityType.BAT)){
            if(e.getEntity().getKiller() != null){
               Player PlayerKiller = e.getEntity().getKiller();
               PlayerKiller.setExp(0);
               PlayerKiller.giveExp(PlayerKiller.getExpToLevel());
            }
        }

    }
}
