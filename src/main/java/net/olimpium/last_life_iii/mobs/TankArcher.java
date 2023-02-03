package net.olimpium.last_life_iii.mobs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TankArcher implements Listener {
    Map<UUID,Integer> skeletonMap = new HashMap<>();
    @EventHandler
    public void onSpawn(EntitySpawnEvent e){
        if(e.getEntity() instanceof Skeleton){
            if (RandomMobNumber.rngNumb <= 50 && RandomMobNumber.rngNumb > 0){
                skeletonMap.put(e.getEntity().getUniqueId(),0);
                Skeleton skeleton = (Skeleton) e.getEntity();
                skeleton.setCustomName(ChatColor.BLACK+ChatColor.BOLD.toString()+"esqueleto chungo");
                skeleton.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                skeleton.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                skeleton.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                skeleton.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
                skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));

                skeleton.getEquipment().setHelmetDropChance(0);
                skeleton.getEquipment().setChestplateDropChance(0);
                skeleton.getEquipment().setLeggingsDropChance(0);
                skeleton.getEquipment().setBootsDropChance(0);
                skeleton.getEquipment().setItemInMainHandDropChance(0);
            }
        }
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(e.getEntity().getCustomName().equals(ChatColor.BLACK+ChatColor.BOLD.toString()+"esqueleto chungo")){
            if(!skeletonMap.get(e.getEntity().getUniqueId()).equals(4)){
                if(skeletonMap.get(e.getEntity().getUniqueId()).equals(3)){
                    e.setCancelled(true);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(),Sound.ITEM_SHIELD_BREAK,1,.75f);
                }else {
                    e.setCancelled(true);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_ANVIL_PLACE,1,2);
                    Vector direction = e.getEntity().getLocation().toVector().subtract(e.getDamager().getLocation().add(0,1.5,0).toVector()).multiply(-1);
                    e.getDamager().setVelocity(direction.multiply(0.5));
                }
                skeletonMap.replace(e.getEntity().getUniqueId(),skeletonMap.get(e.getEntity().getUniqueId())+1);
            }else{
                skeletonMap.replace(e.getEntity().getUniqueId(),skeletonMap.get(e.getEntity().getUniqueId()),0);

            }
        }
    }
    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(e.getEntity().getCustomName().equals(ChatColor.BLACK+ChatColor.BOLD.toString()+"esqueleto chungo")){
            skeletonMap.remove(e.getEntity().getUniqueId());
        }
    }
}
