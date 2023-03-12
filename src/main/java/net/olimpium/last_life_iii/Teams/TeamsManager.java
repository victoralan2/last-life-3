package net.olimpium.last_life_iii.Teams;


import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import com.fren_gor.ultimateAdvancementAPI.database.TeamProgression;
import com.fren_gor.ultimateAdvancementAPI.nms.wrappers.advancement.AdvancementFrameTypeWrapper;
import me.croabeast.advancementinfo.AdvancementInfo;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.server.v1_16_R3.AdvancementFrameType;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.advancements.AdvancementManager;
import net.olimpium.last_life_iii.discordBot.TeamCommand;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class TeamsManager {
	public static File teamsDir;

	public static BukkitRunnable autoSaveRunnable;
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
	public static LastLifeTeam getTeamOfUser(String name){
		for (LastLifeTeam team : TeamsManager.getTeamList()){
			if (team.getMembers().contains(name)) return team;
		}
		return null;
	}
	public static void unregisterTeam(LastLifeTeam team){
		teamList.remove(team);
		File teamsFile = new File(teamsDir + "/" + team.getName() + ".yml");
		teamsFile.delete();
		File sacksTeamsFile = new File(teamsDir + "/inv/inv_" + team.getName() + ".yml");
		File enderChestTeamsFile = new File(teamsDir + "/inv/inv_" + team.getName() + ".yml");
		enderChestTeamsFile.delete();
		enderChestTeamsFile.delete();
		Role role = TeamCommand.getRoleOf(team);
		if (role != null){
			role.delete().queue();
		}
	}
	public static int getLevelByExp(int exp){

		return 0;
	}
	public static int getExpByLevel(int level){


		return 0;

	}
	public static int calculatePoints(LastLifeTeam team){
		return 0;

	}
	public static int challangesOf(LastLifeTeam team, boolean unique){


		ArrayList<Advancement> challenges = new ArrayList<>();
		ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> challenges2 = new ArrayList<>();

		for (String player : team.getMembers()){
			Player member = Bukkit.getPlayer(player);
			for (Advancement advancement : team.getAdvancementsDone()) {
				if (advancement.getKey().getKey().contains("recipes/")) continue;

				AdvancementInfo info = new AdvancementInfo(advancement);
				if (info.getFrameType().equals("unknown")) continue;
				if (info.getFrameType().equals("challenge")) {
					if (!unique) {
						challenges.add(advancement);
					} else if (!challenges.contains(advancement)) {
						challenges.add(advancement);
					}
				}
			}

			for (com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement : team.getLastAdvancementDone()) {
				System.out.println(member.getName());
				boolean isChallenge = advancement.getNMSWrapper().getDisplay().getAdvancementFrameType().getFrameType().equals(AdvancementFrameTypeWrapper.FrameType.CHALLENGE);
				if (isChallenge) {
					if (!unique) {
						challenges2.add(advancement);
					} else if (!challenges2.contains(advancement)) {
						challenges2.add(advancement);
					}
				}
			}
		}
		return challenges.size() + challenges2.size();
	}
	public static int advancementsNoChallengeOf(LastLifeTeam team, boolean unique){

		ArrayList<Advancement> challenges = new ArrayList<>();
		ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> challenges2 = new ArrayList<>();

		for (String player : team.getMembers()){
			Player member = Bukkit.getPlayer(player);
			for (Advancement advancement : team.getAdvancementsDone()) {
				if (advancement.getKey().getKey().contains("recipes/")) continue;

				AdvancementInfo info = new AdvancementInfo(advancement);
				if (info.getFrameType().equals("unknown")) continue;
				if (!info.getFrameType().equals("challenge")) {
					if (!unique) {
						challenges.add(advancement);
					} else if (!challenges.contains(advancement)) {
						challenges.add(advancement);
					}
				}
			}

			for (com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement : team.getLastAdvancementDone()) {
				System.out.println(member.getName());
				boolean isChallenge = advancement.getNMSWrapper().getDisplay().getAdvancementFrameType().getFrameType().equals(AdvancementFrameTypeWrapper.FrameType.CHALLENGE);
				if (!isChallenge) {
					if (!unique) {
						challenges2.add(advancement);
					} else if (!challenges2.contains(advancement)) {
						challenges2.add(advancement);
					}
				}
			}
		}
		return challenges.size() + challenges2.size();
	}
	public static int totalAdvancementsOf(LastLifeTeam team, boolean unique){
		return advancementsNoChallengeOf(team, unique) + challangesOf(team, unique);
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
	public static boolean isUserInATeam(String name){
		try {
			for (LastLifeTeam team : TeamsManager.getTeamList()){

				if (team.getMembers().contains(name)) return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}
	public static void saveTeam(LastLifeTeam team){
		try {
			File enderChestFile = new File(teamsDir + "/inv/inv_" + team.getName() + ".yml");
			File trinketSackFile = new File(teamsDir + "/inv/trinket_" + team.getName() + ".yml");

			File teamFile = new File(teamsDir + "/" + team.getName() + ".yml");
			if (!teamFile.exists())
				teamFile.createNewFile();
			if (!enderChestFile.exists())
				enderChestFile.createNewFile();
			if (!trinketSackFile.exists())
				trinketSackFile.createNewFile();
			LastLifeTeam.toFile(teamFile, team);
			saveInventoryToFile(team.getEnderChest(), enderChestFile, team.getName());
			saveInventoryToFile(team.getTrinketSack(), trinketSackFile, team.getName());

		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void load(){
		teamsDir = new File(Last_life_III.getPlugin().getDataFolder() + "/teams/");
		if (teamsDir.listFiles() == null) return;
		for (File file : teamsDir.listFiles()){
			if (file.isDirectory()) continue;
			LastLifeTeam team = LastLifeTeam.fromFile(file, loadInventoryFromFile(new File(teamsDir + "/inv/inv_" + file.getName())), loadInventoryFromFile(new File(teamsDir + "/inv/trinket_" + file.getName())));
			if (team == null) {
				System.out.println("team null" + file);
				continue;
			}
			teamList.add(team);
		}
	}
	public static void saveInventoryToFile(Inventory inventory, File file, String invTitle) {
		Yaml yaml = createYaml();

		Map<String, Object> data = new HashMap<>();
		data.put("size", inventory.getSize());
		//TODO make title work
		data.put("title", invTitle);

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
		autoSaveRunnable = new BukkitRunnable(){
			@Override
			public void run() {
				for (LastLifeTeam team : teamList){
					if (team == null) continue;
					saveTeam(team);
				}
			}
		};
		autoSaveRunnable.runTaskTimer(Last_life_III.getPlugin(), 200, 20);
	}
}