package net.olimpium.last_life_iii.mecanicas;

import net.minecraft.server.v1_16_R3.ItemShears;
import net.minecraft.server.v1_16_R3.ItemShield;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldBlock implements Listener {
    @EventHandler
    public void onPlayerUseShield(EntityDamageByEntityEvent e){
        if (e.getEntity().getType().equals(EntityType.PLAYER)){
            Player player = (Player) e.getEntity();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
            if (player.isBlocking()){


                if (player.getInventory().getItemInMainHand().getItemMeta() != null)
                {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.SHIELD) && !itemInMainHand.getItemMeta().getDisplayName().equals(net.md_5.bungee.api.ChatColor.of("#CCCCCC")+"Escudo Macizo")){
                        player.setCooldown(Material.SHIELD, 20);
                        e.getEntity().playEffect(EntityEffect.SHIELD_BREAK);
                        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                player.getInventory().setItemInMainHand(itemInMainHand);
                            }
                        }.runTaskLater(Last_life_III.getPlugin(), 1);
                    }
                } else if (player.getInventory().getItemInOffHand().getItemMeta() != null){
                    if (player.getInventory().getItemInOffHand().getType().equals(Material.SHIELD) && !itemInOffHand.getItemMeta().getDisplayName().equals(net.md_5.bungee.api.ChatColor.of("#CCCCCC")+"Escudo Macizo")){
                        player.setCooldown(Material.SHIELD, 20);
                        e.getEntity().playEffect(EntityEffect.SHIELD_BREAK);
                        player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                player.getInventory().setItemInOffHand(itemInOffHand);
                            }
                        }.runTaskLater(Last_life_III.getPlugin(), 1);
                    }
                }
            }
        }
    }
}
