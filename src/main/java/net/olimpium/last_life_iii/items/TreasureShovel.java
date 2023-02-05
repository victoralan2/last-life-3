package net.olimpium.last_life_iii.items;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.Loc;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreasureShovel implements Listener {
    @EventHandler
    public void TreasureShovel(BlockBreakEvent event) throws IOException{
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.IRON_SHOVEL) && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Buscatesoros")) {
            Location loc = event.getBlock().getLocation();
            Player player = event.getPlayer();
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (loc.getBlock().getType().equals(Material.SAND) || loc.getBlock().getType().equals(Material.RED_SAND)){
                        if (!isBlockNatural(loc))
                            return;
                        boolean rng = new Random().nextInt(4)==0; // 25%
                        boolean rng2 = new Random().nextInt(5)==0; // 20%
                        if (!rng){
                            event.setDropItems(false);
                            if (rng2) {
                                ShovelSpecialRNG(event.getBlock().getType() == Material.RED_SAND, loc);
                            } else {
                                ShovelGeneralRNG(loc);
                            }
                        }
                }
            }
        }
    }
    public void ShovelGeneralRNG(Location loc){
        Random rng = new Random();
        double rngNumb = rng.nextInt(10000);
        double chance1 = 20 * 100; //hueso
        double chance2 = 15 * 100; //Polvo de hueso
        double chance3 = 20 * 100; //Palos
        double chance4 = 15 * 100; //Rotten Flesh
        double chance5 = 10 * 100; //Arrow
        double chance6 = 5 * 10; //Emerald
        double chance7 = 5 * 10; //Diamond
        double chance8 = 15 * 100;// Flint
        double chance9 = 4 * 100; //Arbusto muerto
        if(rngNumb <= chance1){
            loc.getWorld().dropItemNaturally(loc ,new ItemStack(Material.BONE));
        } else if (rngNumb <= chance1+chance2){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.BONE_MEAL));
        }else if (rngNumb <= chance1+chance2+chance3){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.STICK));
        } else if (rngNumb <= chance1+chance2+chance3+chance4){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.ROTTEN_FLESH));
        }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.ARROW));
        }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5+chance6){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.EMERALD));
        }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5+chance6+chance7){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.DIAMOND));
        }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5+chance6+chance7+chance8){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.FLINT));
        }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5+chance6+chance7+chance8+chance9){
            loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.DEAD_BUSH));
        }
    }
    public void ShovelSpecialRNG(boolean IsRedSand, Location loc){
        Random rng = new Random();
        ItemStack specialDrop;
        if (IsRedSand) {
            double rngNumb = rng.nextInt(10000);
            float chance1 = 50*100;//pepita de oro
            float chance2 = 20*100;//Lingote de oro
            float chance3 = 5*100;//Escorpion
            float chance4 = 15*100;//Redstone
            float chance5 = 95*10;//Manzana dorada
            float chance6 = 5*10;//Midas Curse

            if(rngNumb <= chance1){
                loc.getWorld().dropItemNaturally(loc ,new ItemStack(Material.GOLD_NUGGET, 3));
            } else if (rngNumb <= chance1+chance2){
                loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
            }else if (rngNumb <= chance1+chance2+chance3){
                Location SpawnLocation = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
                Silverfish entity = (Silverfish) loc.getWorld().spawnEntity(SpawnLocation, EntityType.SILVERFISH);

                entity.setCustomName(ChatColor.GOLD+"Alacrán Dorado");
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
                entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(2);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 4, true, true));
                entity.setCustomNameVisible(true);

            } else if (rngNumb <= chance1+chance2+chance3+chance4){
                loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.REDSTONE));
            }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5){
                loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLDEN_APPLE));
            }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5+chance6){
                ItemStack MidasCurse = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta midasmeta = MidasCurse.getItemMeta();
                midasmeta.setDisplayName(ChatColor.GOLD+"Maldición de midas");
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.WHITE+"Este libro contiene el conocimiento ");
                lore.add(ChatColor.WHITE + "para realizar la maldicion de midas. ");
                lore.add(ChatColor.WHITE + "Aunque este hecho polvo, aún puede tener algun uso");
                midasmeta.setLore(lore);
                MidasCurse.setItemMeta(midasmeta);
                loc.getWorld().dropItemNaturally(loc, MidasCurse);
            }


        } else {
            //normal sand
            float rngNumb = rng.nextInt(10000);
            float chance1 = 50*100; //Pepita de hierro
            float chance2 = 20*100; //Lingote de hierro
            float chance3 = 5*100; //Escorpion
            float chance4 = 10*100; //Nautilius
            float chance5 = 15*100; //Recipe
            float chance6;
            float chance7;
            float chance8;
            float chance9;

            if(rngNumb <= chance1){
                loc.getWorld().dropItemNaturally(loc ,new ItemStack(Material.IRON_NUGGET, 3));
            } else if (rngNumb <= chance1+chance2){
                loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.IRON_INGOT));
            }else if (rngNumb <= chance1+chance2+chance3){


                Location SpawnLocation = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5);
                Silverfish entity = (Silverfish) loc.getWorld().spawnEntity(SpawnLocation, EntityType.SILVERFISH);
                entity.setCustomName(ChatColor.YELLOW+"Tyrius");
                entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(1);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 4, true, true));
                entity.setCustomNameVisible(true);


            } else if (rngNumb <= chance1+chance2+chance3+chance4){
                loc.getWorld().dropItemNaturally(loc, new ItemStack(Material.NAUTILUS_SHELL));
            }else if (rngNumb <= chance1+chance2+chance3+chance4+chance5){

                ItemStack FilledMap = new ItemStack(Material.FILLED_MAP);
                ItemMeta itemMeta = FilledMap.getItemMeta();
                int rngNumber = rng.nextInt(3) + 1;
                itemMeta.setDisplayName(ChatColor.DARK_AQUA + ("Página antigua "+rngNumber+" /3 (Delante)"));

                FilledMap.setItemMeta(itemMeta);
                loc.getWorld().dropItemNaturally(loc,FilledMap);
            }
        }

    }
    @EventHandler
    public void onEnchant(EnchantItemEvent event){
        if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Treasure Shovel")){
            if (event.getEnchantsToAdd().containsKey(Enchantment.DIG_SPEED)){
                event.getEnchantsToAdd().remove(Enchantment.DIG_SPEED);

            }
        }
    }

    public boolean isBlockNatural(Location location) throws IOException {

        List<Loc> CoordList = loadBlocksLocations();
        String str = "{X=" + location.getX()+", Y=" +location.getY()+", Z="+location.getZ()+'}';

        for (int i = CoordList.size(); i > 0; i--) {
            if (CoordList.toArray()[i-1].toString().equals(str)){
                return false;
            }
        }
        return true;
    }

    public static List<Loc> loadBlocksLocations() throws IOException {
        Gson gson = new Gson();
        File file = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/BlockLocs.json");
        file.getParentFile().mkdir();

        List<Loc> blockLocation;

        JsonReader reader = new JsonReader(new FileReader(file));
        blockLocation = gson.fromJson(reader, List.class);

        try{
            reader.close();
        } catch (Exception e){

        }
        return blockLocation;
    }

}