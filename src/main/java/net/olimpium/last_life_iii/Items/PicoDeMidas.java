package net.olimpium.last_life_iii.Items;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.Random;

public class PicoDeMidas implements Listener {
    @EventHandler
    public void PicoDeMidas(BlockBreakEvent event){
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.GOLDEN_PICKAXE) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Pico de Midas")) {

            Player player = event.getPlayer();
            if (player.isSneaking()) {

                Location location = player.getEyeLocation();
                BlockIterator blocksToAdd = new BlockIterator(location, 0, 2);
                Block block;

                if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                    int PlayerXP = player.getTotalExperience();
                    if (PlayerXP >= 80) {
                        player.giveExp(-80);
                    } else {
                        if (player.getHealth() >6){
                            player.setHealth(player.getHealth()-6);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 2, false, false));
                        } else {
                            player.setHealth(0);
                        }

                    }
                    while (blocksToAdd.hasNext()) {
                        block = blocksToAdd.next();
                        Location loc;

                        for (int i = 0; i < 27; i++) {
                            if (i == 0) {
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ() + 1);
                            } else if (i == 1)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ());
                            else if (i == 2)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() + 1, block.getZ() - 1);
                            else if (i == 3)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() + 1);
                            else if (i == 4)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ());
                            else if (i == 5)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ() - 1);
                            else if (i == 6)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() - 1, block.getZ() + 1);
                            else if (i == 7)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() - 1, block.getZ());
                            else if (i == 8)
                                loc = new Location(block.getWorld(), block.getX() + 1, block.getY() - 1, block.getZ() - 1);
                            else if (i == 9)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() + 1);
                            else if (i == 10)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ());
                            else if (i == 11)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ() - 1);
                            else if (i == 12)
                                loc = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
                            else if (i == 13)
                                loc = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());
                            else if (i == 14)
                                loc = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
                            else if (i == 15)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ() + 1);
                            else if (i == 16)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ());
                            else if (i == 17)
                                loc = new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ() - 1);
                            else if (i == 18)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ() + 1);
                            else if (i == 19)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ());
                            else if (i == 20)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() + 1, block.getZ() - 1);
                            else if (i == 21)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() + 1);
                            else if (i == 22)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ());
                            else if (i == 23)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ() - 1);
                            else if (i == 24)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() - 1, block.getZ() + 1);
                            else if (i == 25)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() - 1, block.getZ());
                            else if (i == 26)
                                loc = new Location(block.getWorld(), block.getX() - 1, block.getY() - 1, block.getZ() - 1);
                            else {
                                loc = new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ());
                            }
                            if (!loc.getBlock().isLiquid() && !loc.getBlock().getType().equals(Material.VOID_AIR) && !loc.getBlock().isLiquid() && !loc.getBlock().getType().equals(Material.CAVE_AIR) && !loc.getBlock().isLiquid() && !loc.getBlock().getType().equals(Material.AIR) && !loc.getBlock().isLiquid() && !loc.getBlock().getType().equals(Material.BEDROCK) && !loc.getBlock().getType().equals(Material.END_PORTAL) && !loc.getBlock().getType().equals(Material.END_PORTAL_FRAME) && !loc.getBlock().getType().equals(Material.NETHER_PORTAL)) {
                                if (loc.getBlock().getBiome().toString().contains("BADLANDS")) {
                                    Random rnd = new Random();
                                    //numero random del 1 al 100
                                    int rndNumb = rnd.nextInt(100);
                                    //chekea si el numero es mas pequeño o igual a 2
                                    int chance1 = 5;
                                    int chance2 = 25;
                                    int chance3 = 50;
                                    if (rndNumb <= chance1) {
                                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
                                        //checkea si el numero es mas pequeño o igual a 25
                                    } else if (rndNumb <= chance2 + chance1) {
                                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 2));

                                    } else if (rndNumb <= chance3 + chance2 + chance1) {
                                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 1));
                                    }

                                } else {
                                    Random rnd = new Random();
                                    //int NumbOfNuggets = rnd.nextInt(2) + 1;
                                    if (rnd.nextInt(100) > 50) {
                                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 1));
                                    }
                                }
                                loc.getBlock().setType(Material.AIR);
                                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BONE_BLOCK_PLACE, 1, 2);
                            }

                        }

                    }

                }
            } else if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                /*

                NO SNEAK ZONE

                */
                int PlayerXP = player.getTotalExperience();
                if (PlayerXP >= 4) {
                    player.giveExp(-4);
                } else {
                    if (player.getHealth() > 2) {
                        player.setHealth(player.getHealth() - 2);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2, false, false));
                    } else {
                        player.setHealth(0);
                    }
                }
                Location loc = event.getBlock().getLocation();
                loc.getBlock().setType(Material.AIR);
                event.setDropItems(false);
                if (loc.getBlock().getBiome().toString().contains("BADLANDS")) {
                    Random rnd = new Random();
                    //numero random del 1 al 100
                    int rndNumb = rnd.nextInt(100);
                    //chekea si el numero es mas pequeño o igual a 2
                    int chance1 = 20;
                    int chance2 = 25;
                    int chance3 = 55;
                    if (rndNumb <= chance1) {
                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
                        //checkea si el numero es mas pequeño o igual a 25
                    } else if (rndNumb <= chance2 + chance1) {
                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 3));

                    } else if (rndNumb <= chance3 + chance2 + chance1) {
                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 2));
                    }
                } else {
                    Random rnd = new Random();
                    //int NumbOfNuggets = rnd.nextInt(2) + 1;
                    if (rnd.nextInt(100) > 50) {
                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 1));
                    } else if (rnd.nextInt(100) > 50){
                        loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_NUGGET, 2));
                    }
                }
            }
        }
    }
}
