package net.olimpium.last_life_iii.mobs;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Difractor implements Listener {
    public static List<UUID> list = new ArrayList<>();
    public static double DifractorRadius = 7;
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if (!CustomMobSpawners.isEntityArtificial(e.getEntity().getUniqueId()))
            if(e.getEntity() instanceof Pillager){
                if (RandomMobNumber.rngNumb <= 100 && RandomMobNumber.rngNumb >= 0){
                    Pillager pillager = (Pillager) e.getEntity();
                    ItemStack banner = new ItemStack(Material.WHITE_BANNER, 1);
                    List<Pattern> patterns = new ArrayList<>();
                    patterns.add(new Pattern(DyeColor.PURPLE, PatternType.BRICKS));

                    BannerMeta bannerMeta = (BannerMeta)banner.getItemMeta();

                    bannerMeta.setPatterns(patterns);

                    banner.setItemMeta(bannerMeta);
                    pillager.getEquipment().setHelmet(banner);
                    pillager.setCustomName(ChatColor.DARK_PURPLE + "Catalyzer");
                    Catalyzer.list.add(pillager.getUniqueId());
                    e.setCancelled(true);
                }
            }
    }
    @EventHandler
    public void NegateArrowDMG(EntityDamageByEntityEvent e){
        if(e.getEntity().getType().equals(EntityType.PILLAGER)&&e.getEntity().getCustomName().contains(ChatColor.GRAY + "Difractor")){
            if(e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                Arrow arrow = (Arrow) e.getDamager();
                arrow.teleport(arrow.getLocation().add(0, -200,0));
                e.setCancelled(true);
                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_SHIELD_BLOCK, 1,2);
            }
        }
    }
    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if (e.getEntity().getCustomName() == null) return;
        if(e.getEntity().getType().equals(EntityType.PILLAGER)&&e.getEntity().getCustomName().contains(ChatColor.DARK_PURPLE + "Difractor")){
            list.remove(e.getEntity().getUniqueId());
        }
    }
    public static void Schelduler(Last_life_III plugin) {
        plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            try {
                if (list.isEmpty()) return;
                for (UUID uuid : list) {
                    Pillager catalyzer = (Pillager) Bukkit.getEntity(uuid);
                    if (catalyzer != null) {
                        double NumberOfParticles =  2 * Math.PI / 45;
                        for (double angle = 0; angle <= 2*Math.PI; angle = angle + NumberOfParticles){
                            double x = DifractorRadius*Math.cos(angle);
                            double z = DifractorRadius*Math.sin(angle);
                            Location location = catalyzer.getLocation().clone().add(new Vector(x, 9, z));



                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(42, 234, 218), 3f);
                            int random = 1;
                            catalyzer.getWorld().spawnParticle(Particle.REDSTONE, location, random, 0.05, 6, 0.05, 2, dustOptions, false);
                        }
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }, 0L, 2L);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            try {
                if (list.isEmpty()) return;
                for (UUID uuid : list) {
                    Pillager catalyzer = (Pillager) Bukkit.getEntity(uuid);
                    if (catalyzer != null) {

                        List<Entity> arrowsToKill = catalyzer.getNearbyEntities(1, 1, 1);
                        if (!arrowsToKill.isEmpty()) {
                            for (Entity entity : arrowsToKill) {
                                if (entity instanceof Arrow) {
                                    Arrow arrow = (Arrow) entity;
                                    arrow.getWorld().playSound(arrow.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1, 2);
                                    arrow.teleport(arrow.getLocation().add(0, -200, 0));
                                }
                            }
                        }
                        List<Entity> entities = catalyzer.getNearbyEntities(DifractorRadius, DifractorRadius, DifractorRadius);
                        if (entities.isEmpty()) return;
                        for (Entity entity : entities) {
                            if (entity.getType().equals(EntityType.ARROW)) {
                                Arrow arrow = (Arrow) entity;
                                if (!(arrow.getShooter() instanceof Player)) return;
                                arrow.getShooter();
                                if (!arrow.isInBlock()) {
                                    Vector direction = catalyzer.getEyeLocation().subtract(arrow.getLocation()).toVector().normalize();
                                    double force = 1d;
                                    double smoothness = 0.7;
                                    arrow.setVelocity(direction.multiply(smoothness).midpoint(arrow.getVelocity()).multiply(1 / smoothness).multiply(force));
                                }
                            }

                        }
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }, 0L, 1L);
    }
}
