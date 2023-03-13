package net.olimpium.last_life_iii.ai;

import com.comphenix.net.sf.cglib.core.Block;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class PlayerInformationRecopilador implements Listener {
	public static HashMap<Player, List<Block>> playersBlocks = new HashMap<Player, List<Block>>();

	public static File dataFile = new File(Last_life_III.getPlugin().getDataFolder() + "/playerdata/");
	public static void init(){
	}
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e){
		if (!playersBlocks.containsKey(e.getPlayer())){
		}
	}
}
