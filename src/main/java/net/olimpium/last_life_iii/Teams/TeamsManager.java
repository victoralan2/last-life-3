package net.olimpium.last_life_iii.Teams;


import com.fren_gor.ultimateAdvancementAPI.nms.wrappers.advancement.AdvancementFrameTypeWrapper;
import me.croabeast.advancementinfo.AdvancementInfo;
import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.chat.ComponentSerializer;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.discordBot.TeamCommand;
import net.olimpium.last_life_iii.utils.BukkitSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
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
		sacksTeamsFile.delete();
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
	@Deprecated
	public static int challangesOfA(LastLifeTeam team, boolean unique){

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
	@Deprecated
	public static int advancementsNoChallengeOfA(LastLifeTeam team, boolean unique){

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
	@Deprecated
	public static int totalAdvancementsOfA(LastLifeTeam team, boolean unique){
		return advancementsNoChallengeOf(team, unique) + challengesOf(team, unique);
	}
	public static int totalAdvancementsOf(LastLifeTeam team, boolean unique){
		return advancementsNoChallengeOf(team, unique) + challengesOf(team, unique);
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
	public static int challengesOf(LastLifeTeam team, boolean unique){
		if (!unique) return team.getAdvancementsDone().stream().filter(advancement -> {
			AdvancementInfo info = new AdvancementInfo(advancement);
			if (advancement.getKey().getKey().contains("recipes/")) return false;
			if (info.getFrameType().equals("unknown")) return false;
			if (!info.getFrameType().equals("challenge")) return false;
			return true;
		}).toList().size()
				+ team.getLastAdvancementDone().stream().filter(advancement -> advancement.getNMSWrapper().getDisplay().getAdvancementFrameType().getFrameType().equals(AdvancementFrameTypeWrapper.FrameType.CHALLENGE)).toList().size();
		return team.getAdvancementsDone().stream().distinct().toList().size() + team.getLastAdvancementDone().stream().distinct().toList().size();
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
			saveInventoryToFile(team.getEnderChest(), enderChestFile);
			saveInventoryToFile(team.getTrinketSack(), trinketSackFile);

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
	public static void saveInventoryToFile(Inventory inventory, File file) {
		String invString = BukkitSerializer.inventoryToBase64(inventory);
		try {
			if (!file.exists()) file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(invString);
			fileWriter.close();
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public static Inventory loadInventoryFromFile(File file) {
		Inventory inventory = null;
		try {
			Scanner scanner = new Scanner(file);
			String string = "";
			while (scanner.hasNextLine()){
				string+=scanner.nextLine() + "\n";
			}
			inventory = BukkitSerializer.fromBase64(string);

		} catch (Exception e){
			e.printStackTrace();
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