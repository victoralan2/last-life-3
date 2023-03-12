package net.olimpium.last_life_iii.Teams;

import net.olimpium.last_life_iii.Exceptions.MaxTeamMembersExceeded;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.advancements.AdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class LastLifeTeam implements Serializable {
    private String teamName;
    public static final int maxMembers = 3;
    private ArrayList<String> members;
    private ArrayList<Advancement> advancementsDone = new ArrayList<>();
    private ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> lastAdvancementDone = new ArrayList<>();
    private int teamLevel = 1;
    private int exp = 0;
    public static final int maxLevel = 10;
    public static final int upgradeMultiplier = 9;

    public Color color;
    private Inventory enderChest = Bukkit.createInventory(null, upgradeMultiplier);



    private Inventory trinketSack = Bukkit.createInventory(null, 9);

    public LastLifeTeam(@NotNull String name, Color color, @NotNull ArrayList<String> players) {
        this.teamName = name;
        this.color = color;

        if (players.size() > maxMembers) throw new RuntimeException(new MaxTeamMembersExceeded("Max member count excideed, expected less than " + maxMembers + " but got " + players.size()));
        this.members = players;
    }

    public LastLifeTeam(@NotNull String name, Color color, @NotNull String player) {
        this.teamName = name;
        this.color = color;

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(player);
        this.members = arrayList;
    }
    public void newAdvancement(Advancement advancement){
        advancementsDone.add(advancement);
    }
    public void newLastLifeAdvancement(com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement){
        lastAdvancementDone.add(advancement);
    }
    private LastLifeTeam(@NotNull String name, Color color, @NotNull ArrayList<String> players, int teamLevel, int exp, ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> advancementLast, ArrayList<Advancement> advancementsNoLast, Inventory enderChest, Inventory trinketSack) {
        this.teamName = name;
        this.color = color;
        if (players.size() > maxMembers) throw new RuntimeException(new MaxTeamMembersExceeded("Max member count excideed, expected less than " + maxMembers + " but got " + players.size()));
        this.members = players;
        this.teamLevel = teamLevel;
        this.exp = exp;
        this.advancementsDone = advancementsNoLast;
        this.lastAdvancementDone = advancementLast;
        this.enderChest = enderChest;
        this.trinketSack = trinketSack;
    }
    public void upgrade(){
        if (teamLevel < maxLevel) teamLevel++;
    }
    public Integer getLevel(){
        return teamLevel;
    }
    public void register(){
        if (TeamsManager.getTeamByName(teamName) == null){
            TeamsManager.registerTeam(this);
        }
    }
    public ArrayList<Advancement> getAdvancementsDone() {
        return advancementsDone;
    }

    public ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> getLastAdvancementDone() {
        return lastAdvancementDone;
    }
    public int getExp() {
        return exp;
    }

    public void add(int exp) {
        this.exp += exp;
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
    public void addMember(String memberName){
        this.members.add(memberName);
    }
    public void removeMember(String memberName){
        this.members.remove(memberName);
    }


    public Inventory getEnderChest() {
        return enderChest;
    }

    public void setEnderChest(Inventory enderChest) {
        this.enderChest = enderChest;
    }

    public static int getMaxMembers(){
        return maxMembers;
    }


    public List<String> getMembers(){

        return members;
    }
    public Inventory getTrinketSack() {
        return trinketSack;
    }

    public void setTrinketSack(Inventory trinketSack) {
        this.trinketSack = trinketSack;
    }
    public static LastLifeTeam fromFile(File file, Inventory inventory, Inventory trinketSack){
        try {
            Scanner scanner = new Scanner(file);
            String name = scanner.nextLine();
            Color color = new Color(Integer.parseInt(scanner.nextLine()));
            ArrayList<String> playersNames = new ArrayList<>();
            ArrayList<Advancement> advancementsNoLast = new ArrayList<>();
            ArrayList<com.fren_gor.ultimateAdvancementAPI.advancement.Advancement> advancementsLast = new ArrayList<>();
            String names = scanner.nextLine();

            Collections.addAll(playersNames, names.split("#"));

            String advancementsNoLastString = scanner.nextLine();
            for (String advancementStringNoLast : advancementsNoLastString.split("##///")) {
                Advancement ad = AdvancementManager.getAdvancementById(advancementStringNoLast.split("_-__")[0], advancementStringNoLast.split("_-__")[1]);
                if (ad == null){
                    System.out.println("KEY: " + advancementStringNoLast.split("_-__")[1] + ".");

                    System.out.println("Null! " + ad.getKey().getKey());
                    continue;
                }
                advancementsNoLast.add(ad);
            }
            String advancementsLastString = scanner.nextLine();
            for (String advancementLast : advancementsLastString.split("##///")){
                AdvancementManager.advancementList.forEach(adv -> System.out.println(adv.getKey().getKey()));
                System.out.println(AdvancementManager.advancementList.stream().anyMatch(filterAdvancement -> filterAdvancement.getKey().getKey().equals(advancementLast.split("_-__")[1])) + " : " + Arrays.toString(advancementLast.split("_-__")));

                if (AdvancementManager.advancementList.stream().anyMatch(filterAdvancement -> filterAdvancement.getKey().getKey().equals(advancementLast.split("_-__")[1]))){
                    com.fren_gor.ultimateAdvancementAPI.advancement.Advancement ad = AdvancementManager.advancementList.stream().filter(filterAdvancement -> filterAdvancement.getKey().getNamespace().equals(advancementLast.split("_-__")[0]) && filterAdvancement.getKey().getKey().equals(advancementLast.split("_-__")[1])).toList().get(0);
                    if (ad == null){
                        System.out.println("Null! " + ad.getKey().getKey());
                        continue;
                    }
                    advancementsLast.add(ad);
                }
            }

            int level = Integer.parseInt(scanner.nextLine());
            int exp = Integer.parseInt(scanner.nextLine());

           return new LastLifeTeam(name, color, playersNames, level, exp, advancementsLast, advancementsNoLast, inventory, trinketSack);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void toFile(File file, LastLifeTeam team){
        try {
            if (!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(team.getName() + "\n");

            fileWriter.write(team.color.getRGB() + "\n");
            for (String player : team.getMembers()){
                fileWriter.write(player + "#");
            }
            fileWriter.write("\n");

            for (Advancement advancement : team.getAdvancementsDone()){
                if (advancement.getKey().getKey().contains("recipes/")) continue;

                fileWriter.write(advancement.getKey().getNamespace() + "_-__" + advancement.getKey().getKey()  + "##///");

            }
            fileWriter.write("\n");

            for (com.fren_gor.ultimateAdvancementAPI.advancement.Advancement advancement : team.getLastAdvancementDone()){
                fileWriter.write(advancement.getKey().getNamespace() + "_-__" + advancement.getKey().getKey()  + "##///");
            }
            fileWriter.write("\n");

            fileWriter.write(team.getLevel().toString() + "\n");
            fileWriter.write(team.getExp() + "\n");
            fileWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
