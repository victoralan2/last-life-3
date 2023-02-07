package net.olimpium.last_life_iii.mobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Random;

public class RandomMobNumber implements Listener {

    public static float rngNumb = 0;
        @EventHandler
        public void mobSpawn(EntitySpawnEvent e){
            if (CustomMobSpawners.isEntityArtificial(e.getEntity().getUniqueId())){
                e.setCancelled(true);
                return;
            }
            if (!e.getEntity().getType().equals(EntityType.DROPPED_ITEM)){
            Random rnd = new Random();
            rngNumb = rnd.nextFloat()*100;

            }
        }
}
