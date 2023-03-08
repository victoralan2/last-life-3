package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.utils.TimeSystem;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class SubCommands {
    public static void CommandTeam(String[] args, Player player) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
            player.sendMessage(ChatColor.GREEN + "use </game help>");
            player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
        }
        if (args[1].equalsIgnoreCase("create")) {
            try {
                ArrayList<String> members = new ArrayList<>();
                for (int i = 0; i < args.length - 3; i++) {
                    members.add(args[i + 3]);
                }
                Bukkit.broadcastMessage(args[2]);
                LastLifeTeam team = new LastLifeTeam(args[2], members);
                team.register();
                Bukkit.broadcastMessage("created");

            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        } else if (args[1].equalsIgnoreCase("players")) {
            if (args.length <= 4) {
                LastLifeTeam team = TeamsManager.getTeamByName(args[2]);
                if (team !=null){
                    if (args[3].equalsIgnoreCase("add")){
                        if (Bukkit.getPlayer(args[4]) != null){
                            team.addMember(Bukkit.getPlayer(args[4]));
                        } else {
                            player.sendMessage("No such player");
                        }
                    }  else if (args[3].equalsIgnoreCase("list")) {
                        player.sendMessage(Arrays.toString(team.getMembers().toArray()));
                    }
                } else {
                    player.sendMessage("No such team");
                }
            }
            } else if (args[1].equalsIgnoreCase("list")) {
                for (LastLifeTeam team : TeamsManager.getTeamList()) {
                    if (team == null) continue;
                    player.sendMessage(team.getName());
                }
            } else if (args[1].equalsIgnoreCase("inventory")){
                LastLifeTeam team = TeamsManager.getTeamByName(args[2]);
                if (team !=null){
                    player.openInventory(team.getEnderChest());
                } else {
                    player.sendMessage("No such team");
                }
            }
        }

        public static void CommandStart (Player player){
            if (TimeSystem.Week == 0) {
                Bukkit.broadcastMessage(ChatColor.RED + "LAST LIFE HAS STARTED!");
                for (Player player2 : Bukkit.getOnlinePlayers()){
                    VerificationSystem.lastLifeStarted();
                }
                TimeSystem.startLastLife();
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Last life has already started");
            }
        }
    }
