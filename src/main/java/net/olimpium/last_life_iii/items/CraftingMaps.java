package net.olimpium.last_life_iii.items;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.World;
import net.olimpium.last_life_iii.Last_life_III;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.*;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

public class CraftingMaps extends MapRenderer implements Listener {
    @Override
    public void render(MapView mv, MapCanvas mc, Player p){
        if (p.getInventory().getItemInMainHand().getItemMeta() != null){
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.DARK_AQUA + "Página antigua")){
                String pageNumb = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ")[2];
                Boolean Front = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().endsWith("(Delante)");
                try {
                    File file = new File(Last_life_III.getPlugin().getDataFolder().toPath().toAbsolutePath() + "/images/" + pageNumb + "delante" + Front  + ".png");
                    BufferedImage image = ImageIO.read(file);
                    mc.drawImage(0, 0, image);
                    mv.setUnlimitedTracking(false);
                    mv.setScale(MapView.Scale.CLOSEST);
                    mv.setCenterX(p.getWorld().getSpawnLocation().getBlockX());
                    mv.setCenterZ(p.getWorld().getSpawnLocation().getBlockZ());
                    mv.setTrackingPosition(false);

                } catch (IOException e) {

                }
            }
        }
    }
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e){
        try {
            if (e.getItem().getItemMeta() != null){
                if (e.getItem().getItemMeta().getDisplayName().contains(ChatColor.DARK_AQUA + "Página antigua")){
                    ItemMeta im = e.getItem().getItemMeta();
                    String pageNumb = e.getItem().getItemMeta().getDisplayName().split(" ")[2];
                    Boolean Front = e.getItem().getItemMeta().getDisplayName().endsWith("(Delante)");
                    if (Front)
                        im.setDisplayName(ChatColor.DARK_AQUA + "Página antigua " + pageNumb + " /3 (Detras)");
                    else
                        im.setDisplayName(ChatColor.DARK_AQUA + "Página antigua " + pageNumb + " /3 (Delante)");
                    e.getItem().setItemMeta(im);
                }
            }
        } catch (NullPointerException exception){

        }
    }
    //Add rendererr
    @EventHandler
    public void onMapInitialize(MapInitializeEvent e) {
        e.getMap().addRenderer(new CraftingMaps());
    }




    //Off hand canceller
    @EventHandler
    public void onPlayerSwapHands(PlayerSwapHandItemsEvent e){
        if (e.getOffHandItem().getItemMeta() != null) return;
        if (e.getOffHandItem().getItemMeta().getDisplayName().contains(ChatColor.DARK_AQUA + "Página antigua")){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerPutsItemInOffHand(InventoryClickEvent e){
        if (e.getInventory().getType().equals(InventoryType.CRAFTING)){
            new BukkitRunnable(){
                @Override
                public void run(){
                    if (e.getView().getPlayer().getInventory().getItemInOffHand().getItemMeta() != null){
                        if (e.getView().getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.DARK_AQUA + "Página antigua")){
                            e.getView().getPlayer().getInventory().addItem(e.getView().getPlayer().getInventory().getItem(EquipmentSlot.OFF_HAND));
                            e.getView().getPlayer().getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.AIR));
                        }

                    }
                }
            }.runTaskLater(Last_life_III.getPlugin(), 1);
        }
    }
    @EventHandler
    public void onPlayerDragItemInOffHand(InventoryDragEvent e){
        if (e.getInventory().getType().equals(InventoryType.CRAFTING)){
            new BukkitRunnable(){
                @Override
                public void run(){
                    if (e.getView().getPlayer().getInventory().getItemInOffHand().getItemMeta() != null){
                        if (e.getView().getPlayer().getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.DARK_AQUA + "Página antigua")){
                            e.getView().getPlayer().getInventory().addItem(e.getView().getPlayer().getInventory().getItem(EquipmentSlot.OFF_HAND));
                            e.getView().getPlayer().getInventory().setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.AIR));
                        }

                    }
                }
            }.runTaskLater(Last_life_III.getPlugin(), 1);
        }
    }
}