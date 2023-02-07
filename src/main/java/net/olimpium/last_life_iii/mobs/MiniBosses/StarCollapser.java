package net.olimpium.last_life_iii.mobs.MiniBosses;


import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.Particles;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.advancements.MobsAdvancements.StarCollapserKillAdv;
import net.olimpium.last_life_iii.mobs.CustomMobSpawners;
import net.olimpium.last_life_iii.mobs.RandomMobNumber;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class StarCollapser implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!CustomMobSpawners.isEntityArtificial(e.getEntity().getUniqueId()))
            if (e.getEntity().getType().equals(EntityType.ENDERMAN)) {
                if (RandomMobNumber.rngNumb <= 100 && RandomMobNumber.rngNumb >= 0) {
                    //StarCollapserObject enderman = new StarCollapserObject(e.getLocation());
                    e.getEntity().setCustomName(ChatColor.DARK_PURPLE + "Star" + ChatColor.LIGHT_PURPLE + " Collapser");
                    ((Enderman)e.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                    ((Enderman)e.getEntity()).setHealth(40);
                    //e.setCancelled(true);
                }
            }
    }




    public Map<UUID, Double> i = new HashMap<>();
    public Map<UUID, Double> o = new HashMap<>();
    public int extratime = 2;
    public float MaxRadius = 15;
    @EventHandler
    public void onDeathEvent(EntityDeathEvent e){
        if (e.getEntity() instanceof Enderman){
            if(e.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || e.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) && e.getEntity().getCustomName().equals(ChatColor.DARK_PURPLE + "Star"+ ChatColor.LIGHT_PURPLE+" Collapser")){
                UUID uuid = e.getEntity().getUniqueId();
                i.put(uuid, 1d);
                o.put(uuid, 1d);
                extratime = 2;
                MaxRadius = 15;
                Location deathLocation = e.getEntity().getLocation().getBlock().getLocation();
                Entity endCrystal = e.getEntity().getWorld().spawnEntity(deathLocation, EntityType.ENDER_CRYSTAL);
                endCrystal.setInvulnerable(true);
                ((EnderCrystal) endCrystal).setShowingBottom(false);
                endCrystal.setGlowing(true);
                endCrystal.setCustomName(ChatColor.DARK_PURPLE+""+ChatColor.ITALIC+"Remaining Hits: "+ChatColor.LIGHT_PURPLE+"10");
                endCrystal.setCustomNameVisible(true);
                new BukkitRunnable(){
                    public void run() {
                        endCrystal.setInvulnerable(false);
                        Collection<Entity> insideEntities = endCrystal.getWorld().getNearbyEntities(deathLocation, 0.5, 0.5, 0.5, entity -> !entity.getType().equals(EntityType.ENDER_CRYSTAL));
                        if (!insideEntities.isEmpty()) {
                            for (Entity entity : insideEntities) {
                                if (entity instanceof LivingEntity) {
                                    if (!(entity instanceof EnderDragon) && !(entity instanceof Wither)){
                                        if (((LivingEntity) entity).getHealth() > 0) {
                                            ((LivingEntity) entity).damage(((LivingEntity) entity).getMaxHealth()/5);
                                        }
                                    }
                                }else {
                                    entity.remove();
                                    entity.teleport(new Location(deathLocation.getWorld(), deathLocation.getWorld().getSpawnLocation().getX(), 0, deathLocation.getWorld().getSpawnLocation().getZ()));
                                }
                            }
                        }
                        Collection<Entity> nearbyEntities = endCrystal.getNearbyEntities(i.get(uuid)-(i.get(uuid)/5), i.get(uuid)-(i.get(uuid)/5), i.get(uuid)-(i.get(uuid)/5));
                        if (!nearbyEntities.isEmpty()) {
                            for (Entity entity : nearbyEntities) {
                                Vector direction = new Vector(0,0,0).subtract(entity.getLocation().subtract(deathLocation).toVector()).normalize();
                                double force = 0.4d;
                                double playerControl = 0.4d;
                                entity.setVelocity(direction.midpoint(entity.getVelocity().multiply(playerControl)).multiply(force*(i.get(uuid)/5)));
                            }
                        }

                        int definition = 50;
                        List<FallingBlock> fallingBlocks = new ArrayList<>();
                        Location blockLocation = deathLocation.getBlock().getLocation();
                        //blocks
                        for (double u = 0; u <= Math.PI; u += Math.PI / definition) {
                            double radius = Math.sin(u)*i.get(uuid);
                            double y = Math.cos(u)*i.get(uuid);
                            for (double a = 0; a < Math.PI * 2; a+= Math.PI / definition) {
                                double x = Math.cos(a) * radius;
                                double z = Math.sin(a) * radius;
                                blockLocation.add(x, y, z);
                                Block block = blockLocation.getBlock();
                                Random random = new Random();
                                if (random.nextInt(75) == 1){
                                    Vector direction = new Vector(0,0,0).subtract(block.getLocation().subtract(new Location(deathLocation.getWorld(), deathLocation.getX(), deathLocation.getY(),deathLocation.getZ())).toVector()).multiply(3*(i.get(uuid)/5)).normalize().multiply(i.get(uuid)/2);
                                    block.getWorld().spawnParticle(Particle.REVERSE_PORTAL, blockLocation, 0, direction.getX(), direction.getY(),direction.getZ(), 0.1);
                                }

                                if (block.getBlockData().getMaterial() != Material.LAVA && block.getBlockData().getMaterial() != Material.WATER && block.getBlockData().getMaterial() != Material.AIR && block.getBlockData().getMaterial() != Material.BEDROCK && block.getBlockData().getMaterial() != Material.END_PORTAL && block.getBlockData().getMaterial() != Material.END_PORTAL_FRAME && block.getBlockData().getMaterial() != Material.NETHER_PORTAL&& block.getBlockData().getMaterial() != Material.AIR && block.getBlockData().getMaterial() != Material.CAVE_AIR && block.getBlockData().getMaterial() != Material.VOID_AIR){

                                    if (random.nextInt(4) == 1){
                                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation().clone(), block.getBlockData().clone());
                                        fallingBlock.setDropItem(false);
                                        fallingBlocks.add(fallingBlock);
                                        fallingBlock.setHurtEntities(false);
                                        block.setType(Material.AIR);
                                    }
                                }

                                blockLocation.subtract(x, y, z);
                            }
                        }

                        for (FallingBlock fallingBlock: fallingBlocks){
                            fallingBlock.setVelocity(new Vector(0,0,0).subtract(fallingBlock.getLocation().subtract(new Location(deathLocation.getWorld(), deathLocation.getX(), deathLocation.getY()+2,deathLocation.getZ())).toVector()).multiply(3*(i.get(uuid)/5)).normalize());
                            if (fallingBlock.isDead()){
                                fallingBlocks.remove(fallingBlock);
                            }
                        }


                        if (endCrystal.isDead()){
                            cancel();
                        }
                        if (i.get(uuid)>=MaxRadius){
                            o.replace(uuid, o.get(uuid)+1d/10d);
                            ((EnderCrystal) endCrystal).setBeamTarget(deathLocation.clone().add(0,300,0));
                        } else {
                            i.replace(uuid, i.get(uuid) + 0.05d);
                        }
                        if (o.get(uuid)>=extratime){
                            Collection<Entity> LastNearbyEntities = endCrystal.getNearbyEntities(i.get(uuid)+5, i.get(uuid)+5, i.get(uuid)+5);
                            for (Entity entity: LastNearbyEntities){
                                if (!entity.isDead()){
                                    if (entity instanceof LivingEntity){
                                        if (((LivingEntity) entity).getHealth()>0){
                                            ((LivingEntity) entity).damage(((LivingEntity) entity).getMaxHealth());
                                        }
                                    } else {
                                        entity.setVelocity((entity.getLocation().subtract(deathLocation).toVector()).normalize().add(new Vector(0, .5, 0)).multiply(5));
                                    }
                                }
                            }
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                                PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(Particles.EXPLOSION, true, (float) endCrystal.getLocation().getX(),
                                        (float) endCrystal.getLocation().getY(), (float) endCrystal.getLocation().getZ(),
                                        0, 0, 0, 100, 1
                                );
                                connection.sendPacket(particlePacket);
                                player.playSound(endCrystal.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10000, 0);
                            }
                            endCrystal.remove();
                            if (e.getEntity().getKiller() != null) {
                                StarCollapserKillAdv.advancement.grant(e.getEntity().getKiller());
                            }
                            fallingBlocks.clear();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Last_life_III.getPlugin(), 40, 2);
            }
        }
    }
}

