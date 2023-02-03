package net.olimpium.last_life_iii.Teams;

import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TeamsManager {
    public static List<LastLifeTeam> teamList = new ArrayList<>();

    public static void saveTeams() throws IOException {
        for(LastLifeTeam team: teamList){
            File f = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Teams",   team.getName() + ".data");
            if (!f.exists()){
                f.createNewFile();

            }
            FileOutputStream file = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(team);
            out.close();
            file.close();
        }
    }
    public static LastLifeTeam getTeamByName(String name){
        for (LastLifeTeam team : teamList){
            if (team.getName().equals(name)){
                return team;
            }
        }
        return null;

    }
    public static void registerTeam(LastLifeTeam team){

        teamList.add(team);
        try {
            saveTeams();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void unRegisterTeam(LastLifeTeam team){
        teamList.remove(team);
        File file = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Teams/",  team.getName() + ".data");
        file.delete();
    }

    public static void loadTeams(){
        try {

            File directory = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Teams");
            File[] files = directory.listFiles();
            teamList.clear();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    File f = files[i];
                    FileInputStream file = new FileInputStream(f);
                    ObjectInputStream in = new ObjectInputStream(file);
                    LastLifeTeam team = (LastLifeTeam) in.readObject();
                    teamList.add(team);
                    System.out.println("MAX MEMBERS" + team.getMaxMembers());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}