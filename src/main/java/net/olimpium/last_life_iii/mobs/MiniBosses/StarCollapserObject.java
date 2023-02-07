package net.olimpium.last_life_iii.mobs.MiniBosses;


import fr.skytasul.guardianbeam.Laser;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class StarCollapserObject {
    private int minTime = 5;
    private int maxTime = 20;
    private int Stage = 0;
    private double health;
    private double charge;
    private Player closestPlayer;
    private UUID endCrystalUUID;
    public StarCollapserObject(Location spawnLocation){
        Enderman enderman = (Enderman) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ENDERMAN);
        enderman.setCustomName(ChatColor.DARK_PURPLE + "Star" + ChatColor.LIGHT_PURPLE + " Collapser");
        enderman.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        enderman.setHealth(40);
        for (Player entity : Bukkit.getOnlinePlayers()) {
            if (entity.getLocation().subtract(enderman.getLocation()).toVector().length() <= 128) {
                enderman.setTarget(entity);
                closestPlayer = entity;
            }
        }
        EndermanAbility(enderman);
    }


    public void EndermanAbility(Enderman enderman){
        endCrystalUUID = UUID.randomUUID();
        Last_life_III.getPlugin().getServer().getScheduler().scheduleAsyncRepeatingTask(Last_life_III.getPlugin(), () ->{
            new BukkitRunnable() {
                public void run() {
                    if (enderman.isDead()) {
                        cancel();
                    } else {
                        if (enderman.getLastDamage() != 0) {
                            if (enderman.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || enderman.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                                if (Stage == 0){
                                    EnderCrystal enderCrystal = (EnderCrystal) enderman.getWorld().spawnEntity(enderman.getLocation(), EntityType.ENDER_CRYSTAL);
                                    endCrystalUUID = enderCrystal.getUniqueId();
                                    enderCrystal.setShowingBottom(false);
                                    enderman.addPassenger(enderCrystal);
                                    try {
                                        new Laser.GuardianLaser(enderCrystal.getLocation(), closestPlayer, 10, 20).start(Last_life_III.getPlugin());
                                    } catch (ReflectiveOperationException e) {
                                        e.printStackTrace();
                                    }
                                    Stage = 1;
                                } else {
                                    Bukkit.getEntity(endCrystalUUID).remove();
                                    Stage = 0;
                                }

                            }
                        }
                    }
                }
            }.runTask(Last_life_III.getPlugin());
        }, 1, 80);
    }



    public double getHealth(){
        return this.health;
    }
    public double getCharge(){
        return this.charge;
    }
    public int getStage(){
        return this.Stage;
    }
}
