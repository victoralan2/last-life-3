package net.olimpium.last_life_iii.mobs;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OverheatedCreeper implements Listener {
    public static Map<UUID, Integer> timePassed = new HashMap<>();
    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(e.getEntity() instanceof Creeper) {
            if (RandomMobNumber.rngNumb <= 5 && RandomMobNumber.rngNumb > 0) {
                e.getEntity().setCustomName(net.md_5.bungee.api.ChatColor.of("#FF4500") + "Over"+ net.md_5.bungee.api.ChatColor.of("#FF8300") + "heated" + net.md_5.bungee.api.ChatColor.of("#B7AC44") + " Creeper");
                ((Creeper) e.getEntity()).setAI(false);

                List<Entity> entities = e.getEntity().getNearbyEntities(64,64,64);

                for (Entity nearbyEntity: entities) {
                    if(nearbyEntity instanceof Player){
                        Player player = (Player) nearbyEntity;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.ITALIC.toString() + ChatColor.GRAY + "Hueles un lejano olor a quemado..."));

                    }
                }

                entities = e.getEntity().getNearbyEntities(32,32,32);
                for (Entity nearbyEntity: entities) {
                    if(nearbyEntity instanceof Player){
                        Player player = (Player) nearbyEntity;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.ITALIC.toString() + ChatColor.GRAY + "Hueles un cada vez mas fuerte olor a quemado..."));

                    }
                }
                entities =  e.getEntity().getNearbyEntities(16,16,16);
                for (Entity nearbyEntity: entities) {
                    if (nearbyEntity instanceof Player) {
                        Player player = (Player) nearbyEntity;
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.ITALIC.toString() + ChatColor.GRAY + "Algo se esta quemando... Tendrías que hacer algo"));

                    }
                }
                timePassed.put(e.getEntity().getUniqueId(), 0);
                new BukkitRunnable(){
                    @Override
                    public void run() {

                        if (e.getEntity().isDead()) this.cancel();
                        if (timePassed.get(e.getEntity().getUniqueId()) < 20*15){
                            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getEntity().getLocation().add(0,1,0), 10, 0.2, 0.5, 0.2, 0.05);
                            e.getEntity().getWorld().spawnParticle(Particle.LAVA, e.getEntity().getLocation().add(0,1,0), 1, 0.2, 0.5, 0.2, 0.05);
                            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getEntity().getLocation().add(0,1,0), 2, 0.2, 0.5, 0.2, 0.01);


                            timePassed.replace(e.getEntity().getUniqueId(), timePassed.get(e.getEntity().getUniqueId()) + 1);
                        } else {
                            if (!e.getEntity().isDead()){
                                ((Creeper) e.getEntity()).setExplosionRadius(6);
                                ((Creeper) e.getEntity()).setMaxFuseTicks(1);
                                for (int x = e.getLocation().getBlockX()-1; x <= e.getLocation().getBlockX()+1; x++) {
                                    for (int z = e.getLocation().getBlockZ()-1; z <= e.getLocation().getBlockZ()+1; z++) {
                                        e.getLocation().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY(), z).setType(Material.AIR);
                                    }
                                }
                                ((Creeper) e.getEntity()).ignite();
                                timePassed.remove(e.getEntity().getUniqueId());
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(Last_life_III.getPlugin(),0, 1);


            }
        }
    }
}

