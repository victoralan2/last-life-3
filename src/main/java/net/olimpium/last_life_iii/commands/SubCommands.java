package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.Exceptions.MaxTeamMembersExceeded;
import net.olimpium.last_life_iii.Teams.LLTCopy;
import net.olimpium.last_life_iii.Teams.LastLifeTeams;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.utils.TimeSystem;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubCommands {
    public static void CommandTeam(String[] args, Player player) {
        if (args.length == 1) {
            player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
            player.sendMessage(ChatColor.GREEN + "use </game help>");
            player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
        }
        if (args[1].equalsIgnoreCase("create")) {
            Bukkit.broadcastMessage("created");
            try {
                List<UUID> members = new ArrayList<>();
                for (int i = 0; i < args.length - 3; i++) {
                    members.add(Bukkit.getPlayer(args[i + 3]).getUniqueId());
                }
                Bukkit.broadcastMessage(args[2]);
                new LLTCopy(args[2], members);
            } catch (MaxTeamMembersExceeded | NullPointerException | IllegalArgumentException ignore) {
                ignore.printStackTrace();
            }

        } else if (args[1].equalsIgnoreCase("players")) {
            if (args.length <= 4) {
                LastLifeTeams team = TeamsManager.getTeamByName(args[2]);
                if (team !=null){
                    if (args[3].equalsIgnoreCase("add")){
                        if (Bukkit.getPlayer(args[4]) != null){
                            team.addMember(Bukkit.getPlayer(args[4]));
                        } else {
                            player.sendMessage("No such player");
                        }
                    }
                }
            }
            } else if (args[1].equalsIgnoreCase("list")) {
                for (LastLifeTeams team : TeamsManager.teamList) {
                    player.sendMessage(team.getName());
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
