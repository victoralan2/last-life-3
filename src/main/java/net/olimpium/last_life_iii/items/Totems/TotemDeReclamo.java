package net.olimpium.last_life_iii.items.Totems;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.InventoryUtils;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static org.bukkit.persistence.PersistentDataType.TAG_CONTAINER;


public class TotemDeReclamo implements Listener {

    public void revive(Player player) {
        Random rnd = new Random();
        if (rnd.nextInt(100) >= 50) {
            if (player.getBedSpawnLocation() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.stopSound(Sound.ITEM_TOTEM_USE);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.85f);
                        player.teleport(player.getBedSpawnLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 1.5f);

                    }
                }.runTaskLater(Last_life_III.getPlugin(), 2);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.stopSound(Sound.ITEM_TOTEM_USE);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.85f);
                        player.teleport(VerificationSystem.normalWorld.getSpawnLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f, 1.5f);
                    }
                }.runTaskLater(Last_life_III.getPlugin(), 2);
            }
        } else {
            if (player.getBedSpawnLocation() != null) {
                int[] integers = player.getPersistentDataContainer().get(new NamespacedKey(Last_life_III.getPlugin(), "bedLocation"), PersistentDataType.INTEGER_ARRAY);
                Block block = new Location(VerificationSystem.normalWorld,integers[0],integers[1],integers[2]).getBlock();
                Block block1 = null;
                Directional bed = (Directional) block.getBlockData();
                block1 = block.getLocation().add(-bed.getFacing().getDirection().getX(), 0, -bed.getFacing().getDirection().getZ()).getBlock();

                block.setType(Material.BARREL);
                block1.setType(Material.BARREL);


                BlockState blockState = block.getState();
                Barrel barrel = (Barrel) blockState;

                BlockState blockState1 = Objects.requireNonNull(block1).getState();
                Barrel barrel1 = (Barrel) blockState1;

                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null) continue;
                    if (!InventoryUtils.isFull(barrel.getInventory())) {
                        barrel.getInventory().addItem(itemStack);
                    } else {
                        barrel1.getInventory().addItem(itemStack);
                    }
                }
                player.getInventory().clear();


            } else {
                Block block = VerificationSystem.normalWorld.getSpawnLocation().getBlock();
                Block block1 = block.getLocation().add(0, 1, 0).getBlock();
                block.setType(Material.BARREL);
                block1.setType(Material.BARREL);

                BlockState blockState = block.getState();
                Barrel barrel = (Barrel) blockState;


                BlockState blockState1 = Objects.requireNonNull(block1).getState();
                Barrel barrel1 = (Barrel) blockState1;

                if (!barrel.getInventory().isEmpty() || !barrel1.getInventory().isEmpty()) {
                    if (!barrel.getInventory().isEmpty()) {
                        for (ItemStack itemStack : barrel.getInventory().getContents()) {
                            if (itemStack == null) continue;
                            Item item = barrel.getWorld().dropItem(barrel.getLocation().add(0.5, 2, 0.5), itemStack);
                            item.setVelocity(new Vector(0, 0, 0));
                            barrel.getInventory().clear();
                        }
                    }
                    if (!barrel1.getInventory().isEmpty()) {
                        for (ItemStack itemStack : barrel1.getInventory().getContents()) {
                            if (itemStack == null) continue;
                            Item item = barrel1.getWorld().dropItem(barrel1.getLocation().add(0.5, 1, 0.5), itemStack);
                            item.setVelocity(new Vector(0, 0, 0));
                            barrel1.getInventory().clear();
                        }
                    }
                }

                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack == null) continue;
                    if (!InventoryUtils.isFull(barrel.getInventory())) {
                        barrel.getInventory().addItem(itemStack);
                    } else {
                        barrel1.getInventory().addItem(itemStack);
                    }
                }
                player.getInventory().clear();

            }
        }
    }

    @EventHandler
    public void onTotemActivate(EntityResurrectEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = ((Player) e.getEntity());
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
                player.getInventory().getItemInMainHand().setAmount(0);
                revive(player);
            } else if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
                if (player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
                    player.getInventory().getItemInOffHand().setAmount(0);
                    revive(player);
                }
            }
        } else if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
                player.getInventory().getItemInOffHand().setAmount(0);
                revive(player);
            } else if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
                    player.getInventory().getItemInMainHand().setAmount(0);
                    revive(player);
                }
            }
        }
    }

    @EventHandler
    public void sleepTagAdder(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();  // Get a Bukkit entity.

        player.getPersistentDataContainer().set(new NamespacedKey(Last_life_III.getPlugin(), "bedLocation"), PersistentDataType.INTEGER_ARRAY, new int[]{e.getBed().getX(),e.getBed().getY(),e.getBed().getZ()});
        Bukkit.broadcastMessage(Arrays.toString(player.getPersistentDataContainer().get(new NamespacedKey(Last_life_III.getPlugin(), "bedLocation"), PersistentDataType.INTEGER_ARRAY)));
    }
}
