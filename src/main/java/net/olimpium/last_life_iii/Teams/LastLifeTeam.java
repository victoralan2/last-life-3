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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LastLifeTeam implements Serializable {

    private String teamName;
    private final int maxMembers = 3;
    private List<UUID> members;
    private int upgradeAmmount = 0;
    private Inventory enderChest = Bukkit.createInventory(null, 9*4);
    public LastLifeTeam(@NotNull String name,@NotNull List<UUID> players) throws MaxTeamMembersExceeded, IllegalArgumentException, NullPointerException {
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam   (name) != null) throw new IllegalArgumentException();
        if (players.size() > maxMembers) throw new MaxTeamMembersExceeded("Max team amount of members is" + maxMembers);
        members = players;
        teamName = name;
        Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        try {Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name).unregister();} catch (NullPointerException ignore){}
        TeamsManager.registerTeam(this);
        for (int i = 0; i < players.size(); i++) {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name).addEntry(Bukkit.getPlayer(players.get(i)).getName());
        }
    }

    public void upgradeEnderChest(){

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
    public boolean addMember(Player player){
        this.members.add(player.getUniqueId());
        return false;
    }
    public boolean addMember(UUID uuid){
        this.members.add(uuid);
        return false;
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

    /**
     * Essentially deletes the team
     */
    public void unRegisterTeam(){
        Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(teamName).unregister();
        TeamsManager.unRegisterTeam(this);
    }


}
