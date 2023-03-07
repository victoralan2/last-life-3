package net.olimpium.last_life_iii.Teams;


import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TeamsManager {

	public static ArrayList<LastLifeTeams> teamList = new ArrayList<>();


	public static LastLifeTeams getTeamByName(String name){
		for (LastLifeTeams team : teamList){
			if (team.getName().equals(name)){
				return team;
			}
		}
		return null;
	}

	public void save(){
		for (LastLifeTeams team : teamList){
			saveInventoryToFile(team.getEnderChest(), null);
		}
	}

	public static void load(){
		for (LastLifeTeams team : teamList){
			saveInventoryToFile(team.getEnderChest(), null);
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
	public void registerTeam(){

	}

}