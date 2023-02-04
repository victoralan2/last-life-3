package net.olimpium.last_life_iii.items.Totems;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.InventoryUtils;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;



public class TotemDeReclamo implements Listener {

    public void revive(Player player) {
        Random rnd = new Random();
        if (rnd.nextInt(100) >= 50) {
            if (player.getBedSpawnLocation() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.stopSound(Sound.ITEM_TOTEM_USE);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f,0.85f);
                        player.teleport(player.getBedSpawnLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f,1.5f);

                    }
                }.runTaskLater(Last_life_III.getPlugin(), 2);
            } else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.stopSound(Sound.ITEM_TOTEM_USE);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f,0.85f);
                        player.teleport(player.getWorld().getSpawnLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1f,1.5f);
                    }
                }.runTaskLater(Last_life_III.getPlugin(), 2);
            }
        } else {
            if(player.getBedSpawnLocation() != null){
                ArrayList<Block> blocksRemoved = new ArrayList<>();
                ArrayList<Location> blocksPositions = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    blocksRemoved.add(player.getWorld().getBlockAt(player.getBedSpawnLocation()));
                    blocksPositions.add(player.getBedSpawnLocation());
                    player.getWorld().getBlockAt(player.getBedSpawnLocation()).setType(Material.STONE);
                }
                Block block = player.getWorld().getBlockAt(player.getBedSpawnLocation());
                block.setType(Material.BARREL);
                Block block1 = null;
                for (BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                    Block relative = block.getRelative(face);
                    if (relative.getType().equals(Material.AIR)) {
                        relative.setType(Material.BARREL);
                        block1 = relative;
                        break;
                    }
                }

                BlockState blockState = block.getState();
                Barrel barrel = (Barrel) blockState;

                BlockState blockState1 = Objects.requireNonNull(block1).getState();
                Barrel barrel1 = (Barrel) blockState1;

                for (ItemStack itemStack : player.getInventory().getContents()){
                    if (itemStack == null) continue;
                    if(!InventoryUtils.isFull(barrel.getInventory())) {
                        barrel.getInventory().addItem(itemStack);
                    }else{
                        Bukkit.broadcastMessage("BARREL IS FULL");
                        barrel1.getInventory().addItem(itemStack);
                    }
                }
                player.getInventory().clear();

                int i = 0;
                for (Block blockRemoved : blocksRemoved){
                    blocksPositions.get(i).getBlock().setType(blockRemoved.getType());
                    blocksPositions.get(i).getBlock().setBlockData(blockRemoved.getBlockData());
                    i++;
                }

            }
        }
    }
    @EventHandler
    public void onTotemActivate(EntityResurrectEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        Player player = ((Player) e.getEntity()).getPlayer();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
                revive(player);

            }
        }
        if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
            if (player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(ChatColor.AQUA.toString() + ChatColor.BOLD + "Tótem de Reclamo")) {
               revive(player);
            }
        }
    }
}