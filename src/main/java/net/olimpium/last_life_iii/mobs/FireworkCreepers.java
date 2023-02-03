package net.olimpium.last_life_iii.mobs;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;



public class FireworkCreepers implements Listener {
    @EventHandler
    public void onCreeperSpawns(EntitySpawnEvent e){
        if (e.getEntity() instanceof Creeper){
            if (RandomMobNumber.rngNumb <= 50 && RandomMobNumber.rngNumb >= 0){
                e.getEntity().setCustomName(ChatColor.DARK_RED + "Firework Creeper");
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED,1);
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if(!e.getEntity().isDead()){
                            e.getEntity().getWorld().spawnParticle(Particle.REDSTONE,e.getEntity().getLocation().add(0,1,0),5,0.25,0.5,0.25,0,dustOptions);
                            e.getEntity().getWorld().spawnParticle(Particle.FIREWORKS_SPARK,e.getEntity().getLocation().add(0,1,0),1,0.25,0.5,0.25,0);
                        }
                    }
                }.runTaskTimer(Last_life_III.getPlugin(),0,1);
            }
        }
    }

    @EventHandler
    public void onCreeperExplodes(EntityExplodeEvent e) {

        if (e.getEntity().getCustomName().contains(ChatColor.DARK_RED + "Firework Creeper")){
            Creeper creeper = (Creeper) e.getEntity();
            Location creeperLoc = creeper.getEyeLocation();
            double power = 1;
            double rate = 2 * Math.PI / 45;
            for (double a = 0; a < 2 * Math.PI; a += rate) {
                double x = power * Math.sin(a);
                double z = power * Math.cos(a);
                Location destLoc = creeperLoc.clone().add(new Vector(x, 0, z));
                Vector velocity = destLoc.subtract(creeperLoc).toVector().multiply(0.5);

                Entity entity = e.getLocation().getWorld().spawnEntity(creeperLoc, EntityType.FIREWORK);
                Firework firework = (Firework) entity;
                FireworkMeta meta = firework.getFireworkMeta();
                FireworkEffect.Builder fireworkEffect = FireworkEffect.builder();
                fireworkEffect.withTrail();
                fireworkEffect.withColor(Color.fromRGB(107, 0, 0));
                meta.addEffect(fireworkEffect.build());
                meta.setPower(4);
                firework.setCustomNameVisible(false);
                firework.setCustomName("Firework");
                firework.setFireworkMeta(meta);
                firework.setBounce(true);
                firework.setShotAtAngle(true);
                firework.setGravity(false);
                firework.setVelocity(velocity);
            }
        }
    }
    @EventHandler
    public void onFireWorkHit(EntityDamageByEntityEvent e){
        if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            if (e.getDamager().getCustomName().contains("Firework"))
                e.setCancelled(true);
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) e.getEntity();
                livingEntity.damage(20, e.getEntity());
            }
        }
    }
}