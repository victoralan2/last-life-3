package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.Teams.TeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.HashMap;

public class TeamChatCommand implements CommandExecutor, Listener {
	public static HashMap<String, Boolean> hasTeamChatToggled = new HashMap<>();
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		if(!(commandSender instanceof Player)){System.out.println("Comando solo para jugadores, jaja no puede /tc");return true;}
		Player player = (Player) commandSender;
		if(hasTeamChatToggled.containsKey(player.getName())){
			Boolean value = hasTeamChatToggled.get(player.getName());
			if(!value){
				hasTeamChatToggled.replace(player.getName(),true);
				commandSender.sendMessage(ChatColor.GREEN+"Se ha activado el chat de equipo.");
			}else{
				hasTeamChatToggled.replace(player.getName(),false);
				commandSender.sendMessage(ChatColor.GRAY+"Se ha desactivado el chat de equipo.");
			}
		}else {
			hasTeamChatToggled.put(player.getName(), true);
			commandSender.sendMessage(ChatColor.GREEN + "Se ha activado el chat de equipo.");
		}
		return true;
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if (!TeamsManager.isUserInATeam(e.getPlayer().getName())) return;
		if(hasTeamChatToggled.get(e.getPlayer().getName()).equals(true)){
			e.setCancelled(true);
			for (String memberName : TeamsManager.getTeamByName(e.getPlayer().getName()).getMembers()){
				Player member = Bukkit.getPlayer(memberName);
				if (member == null) continue;
				Bukkit.getPlayer(memberName).sendMessage("["+ChatColor.GREEN+"Team"+ChatColor.RESET+"] "+e.getMessage());

			}
		}
	}
}
