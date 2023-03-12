package net.olimpium.last_life_iii.utils;


import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.discordBot.Bot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeSystem {

    public static int Week = 0;
    public static long FixedTime = 0;
    private static boolean inMaintenance = false;

    public static File file = new File(Last_life_III.getPlugin().getDataFolder() + "\\Time.json");

    public static void startLastLife(){


        try {
            //creating file
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            }
            //getting file writer
            FileWriter writer = new FileWriter(file);


            //writes the json
            JSONObject FileInfo = new JSONObject();
            FileInfo.put("week", 0);
            FileInfo.put("fixedTime", 0);
            FileInfo.put("maintentanceSec", 0);

            writer.write(FileInfo.toJSONString());
            writer.flush();
            writer.close();

            //sets the week
            TimeSystem.setWeek(1);

            //creates an instant of the current time
            Instant instant = Instant.from(ZonedDateTime.now(ZoneId.of("Europe/Paris")));
            setFixedTime(instant.getEpochSecond());



        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //awesome animation
        for (Player player : Bukkit.getOnlinePlayers()){
            player.sendTitle(ChatColor.GOLD + "LAST LIFE III", ChatColor.YELLOW + "HA EMPEZADO", 10,50, 5);
            //player.
        }
        WeekScheduler();
    }
    public static void WeekScheduler(){
        //each second
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Week < 8){
                    if (!inMaintenance){
                        //test if the week has passed
                        //TODO MAKE ACTUALLY PASS A WEEK
                        if (Instant.from(ZonedDateTime.now(ZoneId.of("Europe/Paris"))).getEpochSecond()  >= TimeSystem.FixedTime + 7/*(86400*7)*/){
                            Bukkit.broadcastMessage("NEXT WEEK");
                            setFixedTime(FixedTime + 7 /*(86400*7)*/);
                            nextWeek();
                        }
                        //this.cancel();
                    }
                }
            }
        }.runTaskTimer(Last_life_III.getPlugin(), 0, 20);
    }

    private static void nextWeek(){
        TimeSystem.setWeek(TimeSystem.getWeek()+1);
        Bot.addWeekRoleToMembers();
    }

    public static int getWeek(){

        int value = 0;
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file.getPath());
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            value = Integer.parseInt(jsonObject.get("week").toString());
        }catch (Exception e){
                    e.printStackTrace();
        }
        return value;
    }
    public static long getFixedTime(){
        long value = 0;
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file.getPath());
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            value = Long.parseLong(jsonObject.get("fixedTime").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    public static boolean getIsInMaintenance(){
        boolean value = false;

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file.getPath());
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            value = Boolean.parseBoolean(jsonObject.get("isInMaintenance").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
    public static void setWeek(int newValue){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("week",newValue);
            jsonObject.put("fixedTime",getFixedTime());
            jsonObject.put("isInMaintenance",getIsInMaintenance());
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
            TimeSystem.Week = newValue;
        } catch (Exception ignored){

        }
    }
    public static void setFixedTime(long newValue){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("week",getWeek());
            jsonObject.put("fixedTime",newValue);
            jsonObject.put("isInMaintenance",getIsInMaintenance());
            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
            TimeSystem.FixedTime = newValue;
        } catch (IOException ignored){

        }
    }
    public static void setIsInMaintenance(boolean newValue){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("week",getWeek());
            jsonObject.put("fixedTime",getFixedTime());
            jsonObject.put("isInMaintenance",newValue);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
            inMaintenance = newValue;
        } catch (IOException ignored){

        }
    }
}
