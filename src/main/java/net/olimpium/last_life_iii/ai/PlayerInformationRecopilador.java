package net.olimpium.last_life_iii.ai;

import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.DataManager;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerInformationRecopilador implements Listener {
	public static HashMap<UUID, JSONArray> playersBlocks = new HashMap<>();
	// THE HIGHER THIS NUMBER THE HIGHER THE REMOVAL OF A BLOCK WITH LITTLE DECAY
	public static final double PHI = 0.1;
	// THE HIGHER THIS NUMBER THE LOWER THE DECAY
	public static final double DECAY_MULTIPLIER = 10;
	// THE HIGHER THIS NUMBER THE HIGHER REINFORCEMENT WILL DECAY
	public static final double REINFORCEMENT_DECAY = 1.5;
	// THE REINFORCEMENT CAP
	public static final double REINFORCEMENT_CAP = 2;
	public static final File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata");
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
		e.getPlayer().sendMessage("A");

		if (e.getClickedBlock().getType().equals(Material.CHEST)
				|| e.getClickedBlock().getType().equals(Material.BARREL)
				|| e.getClickedBlock().getType().equals(Material.FURNACE)
				|| e.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)
				|| e.getClickedBlock().getType().toString().endsWith("_BED")) {
			e.getPlayer().sendMessage("B");

			if (!playersBlocks.containsKey(e.getPlayer().getUniqueId()) || !uuidjsonObjectHashMap.containsKey(e.getPlayer().getUniqueId())) return;
			e.getPlayer().sendMessage("Clicked");

			JSONArray playerDataPoints = playersBlocks.get(e.getPlayer().getUniqueId());
			int i = 0;
			for (Object object : (JSONArray) playerDataPoints.clone()){
				if (object == null) {
					throw new RuntimeException("Object is null at i = " + i);
				}
				JSONObject jsonObject = (JSONObject) object;
				if (Integer.parseInt(jsonObject.get("X").toString()) == e.getClickedBlock().getX()
						&& Integer.parseInt(jsonObject.get("Y").toString()) == e.getClickedBlock().getY()
						&& Integer.parseInt(jsonObject.get("Z").toString()) == e.getClickedBlock().getZ()){
					jsonObject.replace("epoch-second", System.currentTimeMillis()/1000);
					jsonObject.replace("reinforcement", Double.parseDouble(jsonObject.get("reinforcement").toString()) + 0.1d);
					System.out.println((Long.parseLong(jsonObject.get("epoch-second").toString()) == System.currentTimeMillis()/1000) + " ");
					playerDataPoints.remove(i);
					playerDataPoints.add(i, jsonObject);
				}
				i++;
			}

			playersBlocks.replace(e.getPlayer().getUniqueId(), playerDataPoints);
			uuidjsonObjectHashMap.get(e.getPlayer().getUniqueId()).changeData("dataPoints", playerDataPoints);
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {


		Random random = new Random();
		if (random.nextInt(10) != 1) return;
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
			if (Math.abs(Integer.parseInt(jsonObject.get("X").toString())-e.getFrom().getX()) <= 10
					&& Math.abs(Integer.parseInt(jsonObject.get("Y").toString())-e.getFrom().getY()) <= 10
					&& Math.abs(Integer.parseInt(jsonObject.get("Z").toString())-e.getFrom().getZ()) <= 10){
				jsonObject.replace("epoch-second", (Integer.parseInt(jsonObject.get("epoch-second").toString()) + System.currentTimeMillis()/1000) / 2);
				jsonObject.replace("reinforcement", Double.parseDouble(jsonObject.get("reinforcement").toString()) + 0.05d);

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
		for (Object object : (JSONArray) array.clone()){
			if (object == null) continue;
			JSONObject jsonObject = (JSONObject) object;
			Point point = new Point(Integer.parseInt(jsonObject.get("X").toString()), Integer.parseInt(jsonObject.get("Z").toString()));
			Double reinforcement = Double.parseDouble(jsonObject.get("reinforcement").toString());

			// Decay of block to make older blocks less important
			double decay = calculateDecay(Integer.parseInt(jsonObject.get("epoch-second").toString()), reinforcement);
			if (decay<= PHI){
				Bukkit.broadcastMessage("REMOVED!");
				array.remove(object);
				continue;
			}

			// materialMultiplier * ( decay / reinforcement )

			Double multiplier = Integer.parseInt(jsonObject.get("materialMultiplier").toString()) * decay;
			Bukkit.broadcastMessage("reinforcement to " + reinforcement);
			Bukkit.broadcastMessage("decay to " + decay);
			Bukkit.broadcastMessage("totalMultiplier to " + multiplier);

			multipliers.add(multiplier);
			points.add(point);
		}
		Point2D centroid = getCentroid(points, multipliers);
		return centroid;
	}
	@EventHandler
	public void recalculateReinforcementEvent(PlayerMoveEvent e){
		Random random = new Random();
		if (random.nextInt(10) != 1) return;
		Vector velocity = new Vector(e.getFrom().getX() - e.getTo().getX(),e.getFrom().getY() - e.getTo().getY(), e.getFrom().getZ() - e.getTo().getZ());
		if (Math.abs(velocity.getX()) <= 0.15 && Math.abs(velocity.getZ()) <= 0.15) return;
		if (e.getTo() == null) return;

		new BukkitRunnable(){
			@Override
			public void run(){
				recalculateReinforcement(e.getPlayer());

			}
		}.runTask(Last_life_III.getPlugin());
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
				if (Double.parseDouble(jsonObject.get("reinforcement").toString()) >= REINFORCEMENT_CAP) {
					Double newReinforcement = REINFORCEMENT_CAP;
				} else {
					Double newReinforcement = (Double.parseDouble(jsonObject.get("reinforcement").toString()) - 1) / REINFORCEMENT_DECAY + 1;

				}

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
	public static Double calculateDecay(int epochSecond, double reinforcement){
		long currentEpochSecond = System.currentTimeMillis()/1000;
		return 1d/((currentEpochSecond/DECAY_MULTIPLIER-epochSecond/DECAY_MULTIPLIER + 1d)/reinforcement);
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
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 1, 5);
	}
}
