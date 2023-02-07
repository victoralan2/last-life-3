package net.olimpium.last_life_iii.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.*;

public class SandLocationSaver implements Listener {
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) throws IOException, NullPointerException {
        Block block = event.getBlock();
        if (block.getType().equals(Material.SAND) || block.getType().equals(Material.RED_SAND) ){
            Location belowBlockLoc = new Location(block.getWorld(), block.getX(), block.getY()-1, block.getZ());
            if (belowBlockLoc.getBlock().getType().equals(Material.AIR) || belowBlockLoc.getBlock().getType().equals(Material.VOID_AIR) || belowBlockLoc.getBlock().getType().equals(Material.CAVE_AIR)){
                Vector vector = new Vector(0, -1, 0);
                RayTraceResult Result = block.getWorld().rayTraceBlocks(block.getLocation(), vector, 250);
                Location BannedBlockLocation = new Location(block.getWorld(), Result.getHitBlock().getLocation().getX(),Result.getHitBlock().getLocation().getY()+1, Result.getHitBlock().getLocation().getZ());
                saveBlockLocation(BannedBlockLocation);
            } else{
                saveBlockLocation(block.getLocation());
            }
        }
    }

    public static List<Loc> newBlockLocations = new ArrayList<>();
    public static void saveBlockLocation(Location loc) throws IOException, NullPointerException {
        Gson gson = new Gson();
        File file = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/BlockLocs.json");
        file.getParentFile().mkdir();
        file.createNewFile();



        List<Loc> oldBlockLocations = loadBlocksLocations();

        //newBlockLocations = setBlockLocs(oldBlockLocations, loc);

        Loc location = new Loc(loc.getX(), loc.getY(), loc.getZ());
        newBlockLocations.add(location);
        try {
            newBlockLocations.addAll(oldBlockLocations);
        } catch (Exception e){

        }
        Writer writer = new FileWriter(file, false);

        gson.toJson(newBlockLocations, writer);
        newBlockLocations.clear();
        writer.flush();
        writer.close();
    }
    public static List<Loc> setBlockLocs(List<Loc> oldLocs, Location loc){
        //Variable
        List<Loc> newLocList = new ArrayList<>();

        //Añade los nuevos datos y los arhivos a esta nueva viariable
        newLocList.add(new Loc(loc.getX(), loc.getY(), loc.getZ()));
        //checks if map is empty

        if (oldLocs != null){
            newLocList.addAll(oldLocs);
        }
        //Bukkit.broadcastMessage(newLocList.toArray()[0].toString());
        //Devuelve el nuevo archivo
        return newLocList;
    }
    public static List<Loc> loadBlocksLocations() throws IOException, NullPointerException{
        Gson gson = new Gson();
        File file = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/BlockLocs.json");
        file.getParentFile().mkdir();

        List<Loc> oldBlockLocations;


        JsonReader reader = new JsonReader(new FileReader(file));
        if (file.exists()){
            oldBlockLocations = gson.fromJson(reader, List.class);
        } else {
            oldBlockLocations = null;
        }
        try{
            reader.close();
        } catch (Exception e){

        }
        return oldBlockLocations;
    }

}