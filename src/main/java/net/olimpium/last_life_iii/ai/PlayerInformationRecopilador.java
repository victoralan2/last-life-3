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
	public static double PHI = 0.1;


	// THE HIGHER THIS NUMBER THE HIGHER THE DECAY
	public static double DECAY_ADDER = 1;

	// THE RANGE WHERE DECAY STARTS (ANY LOWER AND DECAY WILL REGENERATE)
	public static int DECAY_RANGE = 16;


	// THE HIGHER THIS NUMBER THE HIGHER REINFORCEMENT WILL DECAY
	public static double REINFORCEMENT_DECAY = 1.5*TIME_FACTOR;
	// THE REINFORCEMENT CAP
	public static double REINFORCEMENT_CAP = 5;

	// THE AMOUNT OF SECONDS THAT IT REMOVES EACH SECOND
	public static double REINFORCEMENT_REMOVER = 1;

	// THE AMOUNT OF SECONDS IT ADDS EACH SECOND
	public static double REINFORCEMENT_ADDER = REINFORCEMENT_REMOVER/2;


	// THE RANGE WHERE REINFORCEMENT CAN GET UP
	public static int REINFORCEMENT_RANGE = 8;


	// THE AMOUNT IN SECONDS OF TIME THAT THE PLAYER HAS TO BE DISCONNECTED TO COUNT
	public static long MINIMUM_DISCONNECT_TIME = (long) (10*TIME_FACTOR);


	// THE CHANCE (1/UPDATE_CHANCE) OF A PLAYER WALKING TO UPDATE THERE BLOCKS
	public static int UPDATE_CHANCE = (int) (5*TIME_FACTOR);
	// THE CHANCE (1/BLOCK_CHANCE) OF A REGULAR BLOCK OF STORING
	public static int BLOCK_CHANCE = 50;

	public static File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata");
	public static HashMap<UUID, DataManager> uuidjsonObjectHashMap = new HashMap<>();
	public static void addPlayer(UUID player){
		DataManager playerDataManager = new DataManager(dataFile.getAbsolutePath() + "/" + player + "_dataPoints.json");
		JSONObject object = new JSONObject();
		playerDataManager.setData(object);
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
	}
	public static void retrievePlayer(UUID player, File file){
		System.out.println("UUID: " + player);
		DataManager playerDataManager = new DataManager(file.getAbsolutePath());
		playerDataManager.loadDataFromFile();
		playersBlocks.put(player, (JSONArray) playerDataManager.getData().get("dataPoints"));
		uuidjsonObjectHashMap.put(player, playerDataManager);
		Last_life_III.metaDataManager.addDataManager(playerDataManager);
		System.out.println("JSONSTRING: " + playerDataManager.getData().toJSONString());
	}
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
		obj.put("epoch-second", System.currentTimeMillis()/1000);
		obj.put("reinforcement", 1d);
		System.out.println(obj.toJSONString());
		if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
			addPlayer(e.getPlayer().getUniqueId());
			JSONArray dataPoints = new JSONArray();

			dataPoints.add(obj);
			playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), dataPoints);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", dataPoints);
		} else {
			JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
			if (playerDataPoints == null){
				System.out.println("IS NULL!!");
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
					jsonObject.replace("epoch-second", System.currentTimeMillis()/1000);
					if (e.getClickedBlock().getBlockData() instanceof Chest chest){
						if (!chest.getType().equals(Chest.Type.SINGLE)){
							jsonObject.replace("reinforcement", 2d);
						}
					}
/*					Double reinforcement =  Double.parseDouble(jsonObject.get("reinforcement").toString());
					if (Double.parseDouble(jsonObject.get("reinforcement").toString()) >= REINFORCEMENT_CAP){
						reinforcement = REINFORCEMENT_CAP;
						jsonObject.replace("reinforcement", reinforcement);
					} else
						jsonObject.replace("reinforcement", reinforcement + 0.05d);*/
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
				obj.put("epoch-second", System.currentTimeMillis()/1000);

				if (e.getClickedBlock().getBlockData() instanceof Chest chest){
					if (!chest.getType().equals(Chest.Type.SINGLE)){
						obj.put("reinforcement",  2d);
					} else {
						obj.put("reinforcement", 0.5d);
					}
				} else {
					obj.put("reinforcement", 0.5d);
				}
				System.out.println(obj.toJSONString());
				if (!uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())){
					addPlayer(e.getPlayer().getUniqueId());
					JSONArray dataPoints = new JSONArray();

					dataPoints.add(obj);
					playersBlocks.putIfAbsent(e.getPlayer().getUniqueId(), dataPoints);
					uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", dataPoints);
				} else {
					playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
					if (playerDataPoints == null){
						System.out.println("IS NULL!!");
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
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getTo().getWorld().getEnvironment()!= World.Environment.NORMAL) return;
		if (e.getFrom().getWorld().getEnvironment()!= World.Environment.NORMAL) return;

		Random random = new Random();
		if (random.nextInt(UPDATE_CHANCE) != 1) return;
		Vector velocity = new Vector(e.getFrom().getX() - e.getTo().getX(),e.getFrom().getY() - e.getTo().getY(), e.getFrom().getZ() - e.getTo().getZ());
		if (Math.abs(velocity.getX()) <= 0.15 && Math.abs(velocity.getZ()) <= 0.15) return;
		if (e.getTo() == null) return;
		if (!e.getPlayer().getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;

		if (!playersBlocks.containsKey(e.getPlayer().getUniqueId()) || !uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())) return;

		JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
		int i = 0;
		for (Object object : (JSONArray) playerDataPoints.clone()){
			if (object == null) {
				throw new RuntimeException("Object is null at i = " + i);
			}
			JSONObject jsonObject = (JSONObject) object;
			if (Math.abs(Integer.parseInt(jsonObject.get("X").toString())-e.getFrom().getX()) <= DECAY_RANGE
					&& Math.abs(Integer.parseInt(jsonObject.get("Y").toString())-e.getFrom().getY()) <= DECAY_RANGE
					&& Math.abs(Integer.parseInt(jsonObject.get("Z").toString())-e.getFrom().getZ()) <= DECAY_RANGE){
				jsonObject.replace("epoch-second", (Long.parseLong(jsonObject.get("epoch-second").toString()) + System.currentTimeMillis()/1000) / 2);

				// REMOVES BLOCKS THAT ARE NOT THERE ANYMORE
				if (!jsonObject.get("type").toString().equals("DISCONNECT")){
					int x = Integer.parseInt(jsonObject.get("X").toString());
					int y = Integer.parseInt(jsonObject.get("Y").toString());
					int z = Integer.parseInt(jsonObject.get("Z").toString());
					Block blockAtXYZ = new Location(e.getFrom().getWorld(), x, y, z).getBlock();
					if (getMultiplier(blockAtXYZ.getType()) != Integer.parseInt(jsonObject.get("materialMultiplier").toString())) {
						Bukkit.broadcastMessage("REMOVED BY NOT BEING THERE");
						playerDataPoints.remove(i);
						continue;
					}
				}
				if (Math.abs(Integer.parseInt(jsonObject.get("X").toString())-e.getFrom().getX()) <= REINFORCEMENT_RANGE
						&& Math.abs(Integer.parseInt(jsonObject.get("Y").toString())-e.getFrom().getY()) <= REINFORCEMENT_RANGE
						&& Math.abs(Integer.parseInt(jsonObject.get("Z").toString())-e.getFrom().getZ()) <= REINFORCEMENT_RANGE) {

					Double reinforcement = Double.parseDouble(jsonObject.get("reinforcement").toString());
					if (Double.parseDouble(jsonObject.get("reinforcement").toString()) >= REINFORCEMENT_CAP) {
						reinforcement = REINFORCEMENT_CAP;
						jsonObject.replace("reinforcement", reinforcement);
					} else
						jsonObject.replace("reinforcement", reinforcement + REINFORCEMENT_ADDER);
				}
				playerDataPoints.remove(i);
				playerDataPoints.add(i, jsonObject);
			}
			i++;
		}

		playersBlocks.replace(e.getPlayer().getUniqueId(), playerDataPoints);
		uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", playerDataPoints);
	}

	public static Point2D guessCentroid(Player player){
		JSONArray array = playersBlocks.get(player.getUniqueId());

		ArrayList<Point> points = new ArrayList<>();
		ArrayList<Double> multipliers = new ArrayList<>();
		if (array==null)
			return null;
		for (Object object : (JSONArray) array.clone()){
			if (object == null) continue;
			JSONObject jsonObject = (JSONObject) object;
			Point point = new Point(Integer.parseInt(jsonObject.get("X").toString()), Integer.parseInt(jsonObject.get("Z").toString()));
			Double reinforcement = Double.parseDouble(jsonObject.get("reinforcement").toString());

			// Decay of block to make older blocks less important
			double decay = calculateDecay(Long.parseLong(jsonObject.get("epoch-second").toString()), reinforcement);
			if (decay <= PHI){
				Bukkit.broadcastMessage("REMOVED!");
				array.remove(object);
				continue;
			}

			// materialMultiplier * ( decay / reinforcement )
			Double multiplier = Integer.parseInt(jsonObject.get("materialMultiplier").toString()) * decay;

			multipliers.add(multiplier);
			points.add(point);
		}
		Point2D centroid = getCentroid(points, multipliers);
		return centroid;
	}
//	@EventHandler
//	public void recalculateReinforcementEvent(PlayerMoveEvent e){
//		Random random = new Random();
//		if (random.nextInt(UPDATE_CHANCE) != 1) return;
//		Vector velocity = new Vector(e.getFrom().getX() - e.getTo().getX(),e.getFrom().getY() - e.getTo().getY(), e.getFrom().getZ() - e.getTo().getZ());
//		if (Math.abs(velocity.getX()) <= 0.15 && Math.abs(velocity.getZ()) <= 0.15) return;
//		if (e.getTo() == null) return;
//
//		new BukkitRunnable(){
//			@Override
//			public void run(){
//				recalculateReinforcement(e.getPlayer());
//
//			}
//		}.runTask(Last_life_III.getPlugin());
//	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		System.out.println("PLAYER LEAVE");
		uuidjsonObjectHashMap.get(uuid).changeData("lastDisconnect", System.currentTimeMillis()/1000);

	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		UUID uuid = e.getPlayer().getUniqueId();
		JSONArray jsonArray = (JSONArray) uuidjsonObjectHashMap.get(uuid).getData().get("dataPoints");
		long lastTimeDisconnected = Long.parseLong(uuidjsonObjectHashMap.get(uuid).getData().get("lastDisconnect").toString());
		long timeDisconnected = System.currentTimeMillis()/1000-lastTimeDisconnected;

		int i = 0;
		for (Object object : (JSONArray) jsonArray.clone()){
			if (object == null) continue;
			JSONObject jsonObject = (JSONObject) object;
			long epochSecond = Long.parseLong(jsonObject.get("epoch-second").toString());
			jsonObject.replace("epoch-second", epochSecond+timeDisconnected);
			jsonArray.remove(i);
			jsonArray.add(i, jsonObject);
			i++;
		}
		playersBlocks.replace(e.getPlayer().getUniqueId(), jsonArray);
		uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", jsonArray);
		// CHECK IF HE DISCONNECTED LESS THAN x TIME AGO
		if (timeDisconnected > MINIMUM_DISCONNECT_TIME){
			JSONObject obj = new JSONObject();
			obj.put("type", "DISCONNECT");
			obj.put("X", e.getPlayer().getLocation().getBlockX());
			obj.put("Y", e.getPlayer().getLocation().getBlockY());
			obj.put("Z", e.getPlayer().getLocation().getBlockZ());
			obj.put("materialMultiplier", 100);
			obj.put("epoch-second", System.currentTimeMillis()/1000);
			obj.put("reinforcement", 0.5d);
			System.out.println(obj.toJSONString());
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
	public static void recalculateReinforcement(Player player){
		if (!player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;
		if (!playersBlocks.containsKey(player.getUniqueId()) || !uuidjsonObjectHashMap.containsKey(player.getUniqueId())) return;



		JSONArray playerDataPoints = playersBlocks.get(player.getUniqueId());
		int i = 0;
		for (Object object : (JSONArray) playerDataPoints.clone()){
			if (object == null) {
				throw new RuntimeException("Object is null at i = " + i);
			}
			JSONObject jsonObject = (JSONObject) object;
			if (Math.abs(Integer.parseInt(jsonObject.get("X").toString())-player.getLocation().getX()) > 10
					&& Math.abs(Integer.parseInt(jsonObject.get("Z").toString())-player.getLocation().getZ()) > 10) {
				if (Double.parseDouble(jsonObject.get("reinforcement").toString()) == 1) continue;
				Double newReinforcement;
				if (Double.parseDouble(jsonObject.get("reinforcement").toString()) >= REINFORCEMENT_CAP)
					newReinforcement = REINFORCEMENT_CAP;
				else
					newReinforcement = (Double.parseDouble(jsonObject.get("reinforcement").toString()) - 1) / REINFORCEMENT_DECAY + 1;


				jsonObject.replace("reinforcement", newReinforcement);
				playerDataPoints.remove(i);
				playerDataPoints.add(i, jsonObject);
			}
			i++;
		}

		playersBlocks.replace(player.getUniqueId(), playerDataPoints);
		uuidjsonObjectHashMap.get(player.getUniqueId()).changeData("dataPoints", playerDataPoints);
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
	public static Double calculateDecay(long epochSecond, double reinforcement){
		long currentEpochSecond = System.currentTimeMillis()/1000;
		return 1d/((currentEpochSecond/DECAY_ADDER-epochSecond+reinforcement/DECAY_ADDER + 1d));
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

		ArrayList<Point> points = new ArrayList<>();
		ArrayList<Double> multipliers = new ArrayList<>();
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
				if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)){
					if (!type.equals("DISCONNECT")){
						Block blockAtXYZ = location.getBlock();
						if (getMultiplier(blockAtXYZ.getType()) != Integer.parseInt(jsonObject.get("materialMultiplier").toString())) {
							Bukkit.broadcastMessage("REMOVED BY NOT BEING THERE");
							array.remove(i);
							continue;
						}
					}
					if (Math.abs(location.distance(player.getLocation())) < DECAY_RANGE) {
						// DECAY BLOCK
						if (reinforcement == 0) {
							decay = decay + DECAY_ADDER;
							if (decay >= PHI) {
								array.remove(object);
								Bukkit.broadcastMessage("REMOVED BY DECAY");
								continue;
							}
						} else {
							reinforcement -= REINFORCEMENT_ADDER;
						}
					} else {
						// REGEN BLOCK
						if (reinforcement+REINFORCEMENT_ADDER < REINFORCEMENT_CAP){

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
		}

		playersBlocks.replace(uuid, array);
		uuidjsonObjectHashMap.get(uuid).changeData("dataPoints", array);
	}
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
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 1, 5);
	}
}
