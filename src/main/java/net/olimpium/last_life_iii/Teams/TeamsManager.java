package net.olimpium.last_life_iii.Teams;


import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamsManager {
	public static File teamsDir = new File(Last_life_III.getPlugin().getDataFolder() + "/teams/");


	private static List<LastLifeTeam> teamList = new ArrayList<>();

	public static LastLifeTeam getTeamByName(String name){
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
			teamFile.createNewFile();
			enderChestFile.createNewFile();
			LastLifeTeam.toFile(teamFile, team);
			saveInventoryToFile(team.getEnderChest(), enderChestFile);


		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void load(){
		if (teamsDir.listFiles() == null) return;
		for (File file : teamsDir.listFiles()){
			if (file.isDirectory()) continue;
			LastLifeTeam team = LastLifeTeam.fromFile(file, loadInventoryFromFile(new File(teamsDir + "/inv/inv_" + file.getName())));
			Bukkit.broadcastMessage("NAME:" + team.getName());
			teamList.add(team);
		}
	}
	private static void saveInventoryToFile(Inventory inventory, File file){
		try {
			YamlConfiguration configuration = new YamlConfiguration();

			configuration.set("inventory", inventory);
			configuration.save(file);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	private static Inventory loadInventoryFromFile(File file) {
		try {
			Bukkit.getPlayer("TheDracon_").sendMessage(file.getName());
			YamlConfiguration configuration = new YamlConfiguration();
			configuration.load(file);
			Inventory inv = (Inventory) configuration.get("inventory");
			Bukkit.getPlayer("TheDracon_").sendMessage(Arrays.toString(inv.getContents()));
			return inv;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
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