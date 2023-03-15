package net.olimpium.last_life_iii.ai;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

public class PlayerInformationRecopilador implements Listener {
	public static HashMap<UUID, List<Block>> playersBlocks = new HashMap<>();

	public static File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata");
	public static HashMap<UUID, JSONObject> uuidjsonObjectHashMap = new HashMap<>();
	public static void init(){
			uuidjsonObjectHashMap.forEach((uuid, blockList) ->{
				if (uuidjsonObjectHashMap.get(uuid).get("blocks") == null){
					uuidjsonObjectHashMap.get(uuid).put("blocks", Arrays.asList(blockList));
				} else {
					uuidjsonObjectHashMap.get(uuid).replace("blocks", Arrays.asList(blockList));
				}
				Last_life_III.dataManager.addData(uuidjsonObjectHashMap.get(uuid), dataFile);
			});
	}

	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e){
		System.out.println("LOC: " + e.getBlock().getLocation());

		init();
		System.out.println("LOC: " + e.getBlock().getLocation());

		if (!playersBlocks.containsKey(e.getPlayer())){
			ArrayList<Block> b = new ArrayList<>();
			b.add(e.getBlock());
			//playersBlocks.put(e.getPlayer(), b);
			return;
		}
		playersBlocks.get(e.getPlayer()).add(e.getBlock());
	}
}
