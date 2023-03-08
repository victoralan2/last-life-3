package net.olimpium.last_life_iii.Teams;


import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamsManager {
	public static File teamsDir;


	private static List<LastLifeTeam> teamList = new ArrayList<>();

	public static LastLifeTeam getTeamByName(String name){
		ArrayList<String> players = new ArrayList<>();

		for (LastLifeTeam team : teamList){
			if (team.getName().equals(name)){
				return team;
			}
		}
		return null;
	}
	public static void unRegisterTeam(LastLifeTeam team){
		teamList.remove(team);
		File teamsFile = new File(teamsDir + "/" + team.getName() + ".yml");
		teamsFile.delete();
		File inventoryTeamsFile = new File(teamsDir + "/inv/inv_" + team.getName() + ".yml");
		inventoryTeamsFile.delete();
	}
	public static void registerTeam(LastLifeTeam team){
		saveTeam(team);
		teamList.add(team);
	}
	public static void saveAll()  {
		for (LastLifeTeam team : teamList){
			saveTeam(team);
		}
	}
	public static void saveTeam(LastLifeTeam team){
		try {
			File enderChestFile = new File(teamsDir + "/inv/inv_" + team.getName() + ".yml");
			File teamFile = new File(teamsDir + "/" + team.getName() + ".yml");
			if (!teamFile.exists())
				teamFile.createNewFile();
			if (!enderChestFile.exists())
				enderChestFile.createNewFile();
			LastLifeTeam.toFile(teamFile, team);
			saveInventoryToFile(team.getEnderChest(), enderChestFile);


		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void load(){
		teamsDir = new File(Last_life_III.getPlugin().getDataFolder() + "/teams/");
		if (teamsDir.listFiles() == null) return;
		for (File file : teamsDir.listFiles()){
			if (file.isDirectory()) continue;
			LastLifeTeam team = LastLifeTeam.fromFile(file, loadInventoryFromFile(new File(teamsDir + "/inv/inv_" + file.getName())));

			teamList.add(team);
		}
	}
	public static void saveInventoryToFile(Inventory inventory, File file) {
		Yaml yaml = createYaml();

		Map<String, Object> data = new HashMap<>();
		data.put("size", inventory.getSize());
		//TODO make title work
		data.put("title", "Title");

		List<Map<String, Object>> items = new ArrayList<>();
		for (int i = 0; i < inventory.getSize(); i++) {
			ItemStack item = inventory.getItem(i);
			if (item != null) {
				items.add(item.serialize());
			} else {
				items.add(null);
			}
		}
		data.put("items", items);

		String yamlData = yaml.dump(data);

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(yamlData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Yaml createYaml() {
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return new Yaml(options);
	}
	public static Inventory loadInventoryFromFile(File file) {
		Yaml yaml = createYaml();

		String yamlData;
		try {
			yamlData = Files.readString(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Map<String, Object> data = yaml.load(yamlData);

		int size = (int) data.get("size");
		String title = (String) data.get("title");

		Inventory inventory = Bukkit.getServer().createInventory(null, size, title);

		List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
		for (int i = 0; i < items.size(); i++) {
			Map<String, Object> itemData = items.get(i);
			if (itemData != null) {
				ItemStack item = ItemStack.deserialize(itemData);
				inventory.setItem(i, item);
			}
		}

		return inventory;
	}

	public static List<LastLifeTeam> getTeamList() {
		return teamList;
	}

	public static void setTeamList(List<LastLifeTeam> teamList) {
		TeamsManager.teamList = teamList;

		for (LastLifeTeam team : teamList){
			saveTeam(team);
		}
	}
	public static void changeTeam(LastLifeTeam team){
		for (LastLifeTeam team1 : teamList){
			if (team1 == team){
				teamList.set(teamList.indexOf(team1), team);
				break;
			}
		}
		saveTeam(team);

	}

	public static void autoSave(){
		new BukkitRunnable(){
			@Override
			public void run() {
				for (LastLifeTeam team : teamList){
					if (team == null) continue;
					saveTeam(team);
				}
			}
		}.runTaskTimer(Last_life_III.getPlugin(), 0, 20);
	}
}