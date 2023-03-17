package net.olimpium.last_life_iii.ai;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.DataManager;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerInformationRecopilador implements Listener {
	public static HashMap<UUID, JSONArray> playersBlocks = new HashMap<>();
	public static double TIME_FACTOR = 1;

	// THE DECAY VALUE WHERE BLOCKS GET DELETED
	public static double BLOCK_LIFETIME = 15;


	// THE HIGHER THIS NUMBER THE HIGHER THE DECAY REGENERATES
	public static double DECAY_REMOVER = 1;

	// THE HIGHER THIS NUMBER THE HIGHER THE DECAY
	public static double DECAY_ADDER = DECAY_REMOVER/2;

	// THE RANGE WHERE DECAY STARTS (ANY LOWER AND DECAY WILL REGENERATE)
	public static int DECAY_RANGE = 32;


	// THE REINFORCEMENT CAP (MAX SECONDS)
	public static double REINFORCEMENT_CAP = 60;
	// THE AMOUNT OF SECONDS THAT IT REMOVES EACH SECOND

	public static double REINFORCEMENT_DECAY = 1;

	// THE AMOUNT OF SECONDS IT ADDS EACH SECOND
	public static double REINFORCEMENT_ADDER = REINFORCEMENT_DECAY/2;


	// THE RANGE WHERE REINFORCEMENT CAN GET UP
	public static int REINFORCEMENT_RANGE = 16;


	// THE AMOUNT IN SECONDS OF TIME THAT THE PLAYER HAS TO BE DISCONNECTED TO COUNT
	public static long MINIMUM_DISCONNECT_TIME = (long) (10*TIME_FACTOR);


	// THE CHANCE (1/UPDATE_CHANCE) OF A PLAYER WALKING TO UPDATE THERE BLOCKS
	public static int UPDATE_CHANCE = (int) (5*TIME_FACTOR);
	// THE CHANCE (1/BLOCK_CHANCE) OF A REGULAR BLOCK OF STORING
	public static int BLOCK_CHANCE = 50;

	public static File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata");
	public static HashMap<UUID, DataManager> uuidjsonObjectHashMap = new HashMap<>();

	public static void init(){
		for (File file : dataFile.listFiles()) {
			retrievePlayer(UUID.fromString(file.getName().split("_")[0]), file);
			playerUpdateTimer(UUID.fromString(file.getName().split("_")[0]));
		}
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e) {
		if (!e.getBlock().getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;
		if (getMultiplier(e.getBlock().getType()).equals(1)){
			Random random = new Random();
			if (random.nextInt(BLOCK_CHANCE) != 1) return;
		}
		JSONObject obj = new JSONObject();
		obj.put("type", "BLOCK");
		obj.put("X", e.getBlock().getX());
		obj.put("Y", e.getBlock().getY());
		obj.put("Z", e.getBlock().getZ());
		obj.put("materialMultiplier", getMultiplier(e.getBlock().getType()));
		obj.put("decay", 0);
		obj.put("reinforcement", 0d);
		if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
			addPlayer(e.getPlayer().getUniqueId());
			JSONArray dataPoints = new JSONArray();

			dataPoints.add(obj);
			playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), dataPoints);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", dataPoints);
			playerUpdateTimer(e.getPlayer().getUniqueId());
		} else {
			JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
			if (playerDataPoints == null){

				playersBlocks.put(e.getPlayer().getUniqueId(), new JSONArray());
				return;
			}
			playerDataPoints.add(obj);
			playersBlocks.replace(e.getPlayer().getUniqueId(), playerDataPoints);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", playerDataPoints);
		}
	}
	@EventHandler
	public void onPlayerOpenChest(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) return;
		if (!e.getClickedBlock().getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;

		if (e.getClickedBlock().getType().equals(Material.CHEST)
				|| e.getClickedBlock().getType().equals(Material.BARREL)
				|| e.getClickedBlock().getType().equals(Material.FURNACE)
				|| e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)
				|| e.getClickedBlock().getType().toString().endsWith("_BED")) {

			if (!playersBlocks.containsKey(e.getPlayer().getUniqueId()) || !uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())) return;

			JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
			int i = 0;
			boolean found = false;
			for (Object object : (JSONArray) playerDataPoints.clone()){
				if (object == null) {
					throw new RuntimeException("Object is null at i = " + i);
				}
				JSONObject jsonObject = (JSONObject) object;
				if (Integer.parseInt(jsonObject.get("X").toString()) == e.getClickedBlock().getX()
						&& Integer.parseInt(jsonObject.get("Y").toString()) == e.getClickedBlock().getY()
						&& Integer.parseInt(jsonObject.get("Z").toString()) == e.getClickedBlock().getZ()){
					jsonObject.replace("decay", 0d);
					if (e.getClickedBlock().getBlockData() instanceof Chest chest){
						if (!chest.getType().equals(Chest.Type.SINGLE)){
							jsonObject.replace("reinforcement", Double.parseDouble(jsonObject.get("reinforcement").toString()) + 2*REINFORCEMENT_ADDER);
						}
					}
					playerDataPoints.remove(i);
					playerDataPoints.add(i, jsonObject);
					found = true;
					break;
				}
				i++;
			}
			if (!found){
				JSONObject obj = new JSONObject();
				obj.put("type", "BLOCK");
				obj.put("X", e.getClickedBlock().getX());
				obj.put("Y", e.getClickedBlock().getY());
				obj.put("Z", e.getClickedBlock().getZ());
				obj.put("materialMultiplier", getMultiplier(e.getClickedBlock().getType()));
				obj.put("decay", 0.0);

				if (e.getClickedBlock().getBlockData() instanceof Chest chest){
					if (!chest.getType().equals(Chest.Type.SINGLE)){
						obj.put("reinforcement",  2d);
					} else {
						obj.put("reinforcement", 0.5d);
					}
				} else {
					obj.put("reinforcement", 0.5d);
				}
				if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
					addPlayer(e.getPlayer().getUniqueId());
					JSONArray dataPoints = new JSONArray();

					dataPoints.add(obj);
					playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), dataPoints);
					uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", dataPoints);
				} else {
					playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
					if (playerDataPoints == null){
						playersBlocks.put(e.getPlayer().getUniqueId(), new JSONArray());
						return;
					}
					playerDataPoints.add(obj);
				}
			}
			playersBlocks.replace(e.getPlayer().getUniqueId(), playerDataPoints);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", playerDataPoints);
		}
	}
	public static Point2D guessCentroid(Player player){
		JSONArray array = (JSONArray) playersBlocks.get(player.getUniqueId()).clone();

		ArrayList<Point> points = new ArrayList<>();
		ArrayList<Double> multipliers = new ArrayList<>();
		if (array==null)
			return null;
		for (Object object : array){
			if (object == null) continue;
			JSONObject jsonObject = (JSONObject) object;
			Point point = new Point(Integer.parseInt(jsonObject.get("X").toString()), Integer.parseInt(jsonObject.get("Z").toString()));

			// Decay of block to make older blocks less important
			double decay = Double.parseDouble(jsonObject.get("decay").toString());

			// materialMultiplier * ( decay / reinforcement )
			Double multiplier = Integer.parseInt(jsonObject.get("materialMultiplier").toString()) / (decay+1);

			multipliers.add(multiplier);
			points.add(point);
		}
		Point2D centroid = getCentroid(points, multipliers);
		return centroid;
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		uuidjsonObjectHashMap.get(uuid).changeData("lastDisconnect", System.currentTimeMillis()/1000);
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		JSONArray jsonArray = (JSONArray) uuidjsonObjectHashMap.get(uuid).getData().get("dataPoints");
		long lastTimeDisconnected = Long.parseLong(uuidjsonObjectHashMap.get(uuid).getData().get("lastDisconnect").toString());
		long timeDisconnected = System.currentTimeMillis()/1000-lastTimeDisconnected;

		if (timeDisconnected > MINIMUM_DISCONNECT_TIME){
			JSONObject obj = new JSONObject();
			obj.put("type", "DISCONNECT");
			obj.put("X", e.getPlayer().getLocation().getBlockX());
			obj.put("Y", e.getPlayer().getLocation().getBlockY());
			obj.put("Z", e.getPlayer().getLocation().getBlockZ());
			obj.put("materialMultiplier", 100);
			obj.put("decay", 0);
			obj.put("reinforcement", 0);
			if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
				addPlayer(e.getPlayer().getUniqueId());
				JSONArray dataPoints = new JSONArray();

				dataPoints.add(obj);
				playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), dataPoints);
				uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", dataPoints);
			} else {
				JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
				if (playerDataPoints == null){
					playersBlocks.put(e.getPlayer().getUniqueId(), new JSONArray());
					return;
				}
				playerDataPoints.add(obj);
				playersBlocks.replace(e.getPlayer().getUniqueId(), playerDataPoints);
				uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", playerDataPoints);
			}
		}
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
	public static Integer getMultiplier(Material material){
		if (material.toString().toLowerCase().endsWith("bed"))
			return 4;
		if (material.equals(Material.CHEST))
			return 3;
		if (material.equals(Material.TRAPPED_CHEST))
			return 3;
		if (material.equals(Material.FURNACE))
			return 4;
		if (material.equals(Material.BARREL))
			return 3;
		if (material.equals(Material.ENCHANTING_TABLE))
			return 5;
		if (material.equals(Material.BOOKSHELF))
			return 4;
		if (material.isAir())
			return 0;
		return 1;
	}
	private static Block mark = null;

	private static void playerUpdateTimer(UUID uuid){
		new BukkitRunnable(){
			@Override
			public void run() {
				updatePlayer(uuid);
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 0, 20);
	}
	private static void updatePlayer(UUID uuid){
		JSONArray array = playersBlocks.get(uuid);
		if (array==null)
			return;
		int i = 0;
		for (Object object : (JSONArray) array.clone()) {

			if (object == null) continue;

			JSONObject jsonObject = (JSONObject) object;

			Location location = new Location(VerificationSystem.normalWorld, Integer.parseInt(jsonObject.get("X").toString()), Integer.parseInt(jsonObject.get("Y").toString()), Integer.parseInt(jsonObject.get("Z").toString()));

			double reinforcement = Double.parseDouble(jsonObject.get("reinforcement").toString());
			double decay = Double.parseDouble(jsonObject.get("decay").toString());
			String type = jsonObject.get("type").toString();
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
					if (!type.equals("DISCONNECT")) {
						Block blockAtXYZ = location.getBlock();
						if (getMultiplier(blockAtXYZ.getType()) != Integer.parseInt(jsonObject.get("materialMultiplier").toString())) {
							array.remove(i);
							continue;
						}
					}
					player.sendMessage(player.getVelocity().toString());
					if (Math.abs(location.distance(player.getLocation())) > DECAY_RANGE) {
						// DECAY BLOCK
						if (reinforcement == 0) {

							decay = decay + DECAY_ADDER;

							if (decay >= BLOCK_LIFETIME) {
								array.remove(i);
								continue;
							}
						} else {
							if (reinforcement-REINFORCEMENT_DECAY <= 0){
								reinforcement = 0;
							} else {
								reinforcement -= REINFORCEMENT_DECAY;
							}
						}
					} else {
						// REGEN BLOCK
						// CHECK IF PLAYER AFK
						if (!isPlayerAFK(player)){
							if (Math.abs(location.distance(player.getLocation())) < REINFORCEMENT_RANGE){
								if (reinforcement + REINFORCEMENT_ADDER < REINFORCEMENT_CAP) {
									reinforcement += REINFORCEMENT_ADDER;
								}
							}
							if (decay-DECAY_REMOVER > 0){
								decay-=DECAY_REMOVER;
							} else {
								decay = 0;
							}
						}
					}
				}
			}

			jsonObject.replace("X", location.getBlockX());
			jsonObject.replace("Y", location.getBlockY());
			jsonObject.replace("Z", location.getBlockZ());
			jsonObject.replace("reinforcement", reinforcement);
			jsonObject.replace("type", type);
			jsonObject.replace("decay", decay);

			array.remove(i);
			array.add(i, jsonObject);
			i++;
		}

		playersBlocks.replace(uuid, array);
		uuidjsonObjectHashMap.get(uuid).changeData("dataPoints", array);
	}
	public static boolean isPlayerAFK(Player player){
		return true;
	}
	public static void addPlayer(UUID player){
		DataManager playerDataManager = new DataManager(dataFile.getAbsolutePath() + "/" + player + "_dataPoints.json");
		JSONObject object = new JSONObject();
		playerDataManager.setData(object);
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
	}
	public static void retrievePlayer(UUID player, File file){
		DataManager playerDataManager = new DataManager(file.getAbsolutePath());
		playerDataManager.loadDataFromFile();
		playersBlocks.put(player, (JSONArray) playerDataManager.getData().get("dataPoints"));
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
	}
}
