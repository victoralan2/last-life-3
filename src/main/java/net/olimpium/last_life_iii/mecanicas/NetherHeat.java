package net.olimpium.last_life_iii.mecanicas;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class NetherHeat implements Listener {
    public Map<UUID, Double> timesDamaged = new HashMap<>();

    public static void BukkitSchedulerNetherCheck() {
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Last_life_III.getPlugin(), () -> {
            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().getEnvironment().equals(World.Environment.NETHER) && player.getGameMode() != GameMode.CREATIVE) {
                        if (player.getInventory().getBoots() == null) {
                            player.setFireTicks(30);
                        } else if (player.getInventory().getBoots().getItemMeta().isUnbreakable()) {
                            player.setFireTicks(30);
                        } else {
                            for (int i = 0; i < 10; i++) {
                                ItemStack itemStack = player.getInventory().getBoots();
                                ItemMeta itemmeta = itemStack.getItemMeta();
                                if (itemmeta instanceof Damageable) {
                                    if (!itemmeta.getEnchants().containsKey(Enchantment.DURABILITY)) {
                                        if (((Damageable) itemmeta).getDamage() < itemStack.getType().getMaxDurability() - 1) {
                                            ((Damageable) itemmeta).setDamage(((Damageable) itemmeta).getDamage() + 1);
                                            player.getInventory().getBoots().setItemMeta(itemmeta);

                                        } else {
                                            player.playEffect(EntityEffect.BREAK_EQUIPMENT_BOOTS);
                                            player.getInventory().getBoots().setAmount(0);
                                        }
                                    } else {
                                        if ((new Random().nextFloat()) * 100 < (60 + (40 / (itemmeta.getEnchants().get(Enchantment.DURABILITY) + 1)))) {
                                            if (((Damageable) itemmeta).getDamage() < itemStack.getType().getMaxDurability() - 1) {
                                                ((Damageable) itemmeta).setDamage(((Damageable) itemmeta).getDamage() + 1);
                                                player.getInventory().getBoots().setItemMeta(itemmeta);
                                            } else {
                                                player.playEffect(EntityEffect.BREAK_EQUIPMENT_BOOTS);
                                                player.getInventory().getBoots().setAmount(0);
                                            }
                                        }
                                    }
                                }
                                Thread.sleep(200);
                            }
                        }
                    }
                }
            } catch (NullPointerException c) {

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0L, 20L);
    }

    @EventHandler
    public void onFireDmg(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            if (event.getEntity() instanceof Player){
                if (event.getEntity().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    UUID uuid = event.getEntity().getUniqueId();
                    if (timesDamaged.containsKey(uuid)){
                        timesDamaged.replace(event.getEntity().getUniqueId(), timesDamaged.get(event.getEntity().getUniqueId()) + 1);
                    } else {
                        timesDamaged.put(event.getEntity().getUniqueId(), 1d);
                    }
                    double n = 1.05d;
                    event.setDamage(Math.pow(n, timesDamaged.get(event.getEntity().getUniqueId())));








                    /*if (ThreadGoingOn.containsKey(uuid)){
                        if (ThreadGoingOn.get(uuid)){
                            if (!DmgInLessThan10Sec.containsKey(uuid)){
                                DmgInLessThan10Sec.put(uuid, true);
                            } else{
                                DmgInLessThan10Sec.replace(uuid, true);
                            }
                        }
                    }
                    Bukkit.broadcastMessage(ThreadGoingOn.get(uuid).toString());
                    if (!ThreadGoingOn.get(uuid)){
                        if (!ThreadGoingOn.containsKey(uuid)){
                            ThreadGoingOn.put(uuid, true);
                        }
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Bukkit.broadcastMessage("New Thread!");
                                DmgInLessThan10Sec.replace(uuid, false);
                                for (int i = 20; i> 0; i--){
                                    if (DmgInLessThan10Sec.get(uuid)){
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Bukkit.broadcastMessage("Thread over");
                                        this.cancel();
                                        break;
                                    }
                                }
                                if (!this.isCancelled()){
                                    Bukkit.broadcastMessage("10 secs");
                                    timesDamaged.replace(uuid, 0d);
                                }
                            }
                        }.runTaskAsynchronously(Last_life_III.getPlugin());
                    }*/

                }
            }
        }
    }
    @EventHandler
    public void onPlayerChangeDimension(PlayerChangedWorldEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        if (timesDamaged.containsKey(uuid)) {
            timesDamaged.replace(uuid, 0d);
        } else {
            timesDamaged.put(uuid, 0d);
        }
    }
}