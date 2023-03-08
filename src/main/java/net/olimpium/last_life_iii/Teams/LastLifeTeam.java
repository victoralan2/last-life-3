package net.olimpium.last_life_iii.Teams;

import net.olimpium.last_life_iii.Exceptions.MaxTeamMembersExceeded;
import net.olimpium.last_life_iii.Last_life_III;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class LastLifeTeam implements Serializable {
    private String teamName;
    private final int maxMembers = 3;
    private ArrayList<UUID> members;
    private int upgradeAmount = 0;
    private final int maxUpgradeAmount = 8;

    private Inventory enderChest = Bukkit.createInventory(null, 9*4);

    public LastLifeTeam(@NotNull String name, @NotNull ArrayList<UUID> players) {
        this.teamName = name;
        if (players.size() > maxMembers) throw new RuntimeException(new MaxTeamMembersExceeded("Max member count excideed, expected less than " + maxMembers + " but got " + players.size()));
        this.members = players;
    }
    private LastLifeTeam(@NotNull String name, @NotNull ArrayList<UUID> players, int updateAmmount, Inventory enderChest) {
        this.teamName = name;
        if (players.size() > maxMembers) throw new RuntimeException(new MaxTeamMembersExceeded("Max member count excideed, expected less than " + maxMembers + " but got " + players.size()));
        this.members = players;
        this.upgradeAmount = updateAmmount;
        this.enderChest = enderChest;
    }
    public void upgradeEnderChest(){
        if (upgradeAmount < maxUpgradeAmount) upgradeAmount++;
    }
    public Integer getUpgradeAmount(){
        return upgradeAmount;
    }
    public void register(){
        if (TeamsManager.getTeamByName(teamName) == null){
            TeamsManager.registerTeam(this);
        }
    }
    private void saveToFile() throws IOException {
        File f = new File(Last_life_III.getPlugin().getDataFolder().getAbsolutePath() + "/Teams",   teamName+".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("team",this);
        c.save(f);
    }
    /**
     * returns the name of the team
     **/
    public String getName(){
        return this.teamName;
    }

    /**
     * Adds a user to the team, returns false if the user was already there
     **/
    public void addMember(Player player){
        this.members.add(player.getUniqueId());
    }
    public void addMember(UUID uuid){
        if (members.size() >= 3 ) return;

        this.members.add(uuid);
    }

    public Inventory getEnderChest() {
        return enderChest;
    }

    public void setEnderChest(Inventory enderChest) {
        this.enderChest = enderChest;
    }

    public int getMaxMembers(){
        return this.maxMembers;
    }
    public List<Player> getMembers(){
        List<Player> playerList = new ArrayList<>();
        for (UUID uuid : members){
            playerList.add(Bukkit.getPlayer(uuid));
        }
        return playerList;
    }

    public static LastLifeTeam fromFile(File file, Inventory inventory){
        try {
            Scanner scanner = new Scanner(file);
            String name = scanner.nextLine();
            ArrayList<UUID> playersUUIDs = new ArrayList<>();
            String uuids = scanner.nextLine();

            for (String uuid : uuids.split("__")){
                Last_life_III.theDracon_.sendMessage("UUID: " + uuid);

                playersUUIDs.add(UUID.fromString(uuid));
            }
            int upgradeAmmount = Integer.parseInt(scanner.nextLine());
           return new LastLifeTeam(name, playersUUIDs, upgradeAmmount, inventory);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void toFile(File file, LastLifeTeam team){
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(team.getName() + "\n");
            boolean isFirst = true;
            for (Player uuid : team.getMembers()){
                if (isFirst){
                    fileWriter.write(uuid.getUniqueId().toString());
                    isFirst = false;
                } else{
                    fileWriter.write(uuid.getUniqueId() + "__");
                }
            }
            fileWriter.write("\n");
            System.out.println("UPGRADE AMMOUNT: " + team.getUpgradeAmount());
            fileWriter.write(team.getUpgradeAmount().toString() + "\n");
            fileWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
