package net.olimpium.last_life_iii.ai;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.DataManager;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerInformationRecopilador implements Listener {
	public static HashMap<UUID, JSONArray> playersBlocks = new HashMap<>();
	public static final double phi = 0.01;
	public static File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata");
	public static HashMap<UUID, DataManager> uuidjsonObjectHashMap = new HashMap<>();
	public static void addPlayer(UUID player){
		DataManager playerDataManager = new DataManager(dataFile.getAbsolutePath() + "/" + player + "_blocks.json");
		JSONObject object = new JSONObject();
		playerDataManager.setData(object);
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
	}
	public static void retrievePlayer(UUID player, File file){
		System.out.println("UUID: " + player);
		DataManager playerDataManager = new DataManager(file.getAbsolutePath());
		playerDataManager.loadDataFromFile();
		playersBlocks.put(player, (JSONArray) playerDataManager.getData().get("blocks"));
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
		System.out.println("JSONSTRING: " + playerDataManager.getData().toJSONString());
	}
	public static void init(){
		for (File file : dataFile.listFiles())
			retrievePlayer(UUID.fromString(file.getName().split("_")[0]), file);

		mark(VerificationSystem.normalWorld);
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e) {
		JSONObject obj = new JSONObject();
		obj.put("type", e.getBlock().getType().toString());
		obj.put("X", e.getBlock().getX());
		obj.put("Y", e.getBlock().getY());
		obj.put("Z", e.getBlock().getZ());
		obj.put("multiplier", getMultiplier(e.getBlock().getType()));
		obj.put("epoch-second", System.currentTimeMillis()/1000);

		System.out.println(obj.toJSONString());
		if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
			addPlayer(e.getPlayer().getUniqueId());
			JSONArray blocks = new JSONArray();

			blocks.add(obj);
			playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), blocks);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("blocks", blocks);
		} else {
			JSONArray playerBlocks = playersBlocks.get(e.getPlayer().getUniqueId());
			if (playerBlocks == null){
				System.out.println("IS NULL!!");
				playersBlocks.put(e.getPlayer().getUniqueId(), new JSONArray());
				return;
			}
			playerBlocks.add(obj);
			playersBlocks.replace(e.getPlayer().getUniqueId(), playerBlocks);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("blocks", playerBlocks);
		}
	}
	public void onPlayerOpenChest(PlayerInteractEvent e) {
		if (e.getClickedBlock().getType().equals(Material.CHEST)
				|| e.getClickedBlock().getType().equals(Material.BARREL)
				|| e.getClickedBlock().getType().equals(Material.FURNACE)
				|| e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) {
			JSONArray playerBlocks = playersBlocks.get(e.getPlayer().getUniqueId());
			if (playerBlocks == null) {
				System.out.println("IS NULL!!");
				playersBlocks.put(e.getPlayer().getUniqueId(), new JSONArray());
				return;
			}
			//playerBlocks.add(obj);
			playersBlocks.replace(e.getPlayer().getUniqueId(), playerBlocks);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("blocks", playerBlocks);
		}
	}

	public static Point2D guessCentroid(Player player){
		JSONArray array = playersBlocks.get(player.getUniqueId());

		ArrayList<Point> points = new ArrayList<>();
		ArrayList<Double> multipliers = new ArrayList<>();
		for (Object object : array){
			JSONObject jsonObject = (JSONObject) object;
			Point point = new Point(Integer.parseInt(jsonObject.get("X").toString()), Integer.parseInt(jsonObject.get("Z").toString()));
			// Decay of block to make older blocks less important
			double decay = calculateDecay(Integer.parseInt(jsonObject.get("epoch-second").toString()));
			if (decay<= phi){
				array.remove(object);
				continue;
			}
			multipliers.add(Integer.parseInt(jsonObject.get("multiplier").toString()) * decay);
			points.add(point);
		}
		Point2D centroid = getCentroid(points, multipliers);
		return centroid;
	}
	public static Point2D getCentroid(ArrayList<Point> points, ArrayList<Double> multiplier) {
		double x = 0.0;
		double y = 0.0;
		double n = 0;
		int i = 0;
		for (Point p : points) {
			x += p.getX()*multiplier.get(i);
			y += p.getY()*multiplier.get(i);
			n+= multiplier.get(i);
			i++;
		}

		x /= n;
		y /= n;

		return new Point((int)x, (int)y);
	}
	public Integer getMultiplier(Material material){
		if (material.toString().toLowerCase().endsWith("bed"))
			return 20;
		if (material.equals(Material.CHEST))
			return 15;
		if (material.equals(Material.FURNACE))
			return 20;
		if (material.equals(Material.REDSTONE))
			return 2;
		if (material.equals(Material.BARREL))
			return 15;
		if (material.equals(Material.ENCHANTING_TABLE))
			return 20;
		return 1;
	}
	public static Double calculateDecay(int epochSecond){
		long currentEpochSecond = System.currentTimeMillis()/1000;
		return (1d/(currentEpochSecond-epochSecond + 1d));
	}
	private static Block mark = null;
	public static void mark(World world) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (mark!=null){
					mark.setType(Material.AIR);
				}

				Point2D centroid = guessCentroid(Bukkit.getPlayer("TheDracon_"));

				Location location = new Location(world,centroid.getX(), 80, centroid.getY());

				mark = location.getBlock();
				mark.setType(Material.REDSTONE_BLOCK);
				Bukkit.broadcastMessage(mark.getLocation().toString());
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 1, 5);
	}
}
