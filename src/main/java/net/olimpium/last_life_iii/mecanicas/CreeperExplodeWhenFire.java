package net.olimpium.last_life_iii.mecanicas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class CreeperExplodeWhenFire implements Listener {
    @EventHandler
    public void onCreeperOnFire(ProjectileHitEvent e) throws NullPointerException{
        if (e.getHitEntity() == null) return;
        if (e.getHitEntity().getType().equals(EntityType.CREEPER) && e.getEntity().getType().equals(EntityType.ARROW)){
            Arrow arrow = (Arrow) e.getEntity();
            if (arrow.getFireTicks() > 0){
                    Creeper creeper = (Creeper) e.getHitEntity();
                    creeper.ignite();
                }
        }
    }
}
