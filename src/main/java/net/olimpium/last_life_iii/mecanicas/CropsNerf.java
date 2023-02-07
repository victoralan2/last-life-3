package net.olimpium.last_life_iii.mecanicas;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Random;

public class CropsNerf implements Listener {
    @EventHandler
    public void onCropGrow(BlockFertilizeEvent e) {
        Random rnd = new Random();
        float randomNumb = rnd.nextFloat();
        if (randomNumb <= 0.8) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE_MEAL)) {
                    e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                }
            } else if (e.getPlayer().getInventory().getItemInOffHand().getItemMeta() != null){
                if (e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.BONE_MEAL)) {
                    e.getPlayer().getInventory().getItemInOffHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                }
            }
            e.setCancelled(true);
            }
        }

    }
