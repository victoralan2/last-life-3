package net.olimpium.last_life_iii.mecanicas;

import net.olimpium.last_life_iii.utils.EXPUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;


public class BatDropsMoreEXP implements Listener {
    @EventHandler
    public void onBatKilled(EntityDeathEvent e){
        if(e.getEntity().getType().equals(EntityType.BAT)){
            if(e.getEntity().getKiller() != null){
               Player PlayerKiller = e.getEntity().getKiller();
               e.setDroppedExp(EXPUtils.getExpToLevelUp(PlayerKiller.getLevel()+1));
            }
        }

    }
    @EventHandler
    public void onBatHit(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Bat)) return;
        if(e.getDamager() instanceof Player){
            java.util.Random rnd = new Random();
            if((rnd.nextFloat()*100)<75){
                e.setCancelled(true);
            }

        }
    }
}
