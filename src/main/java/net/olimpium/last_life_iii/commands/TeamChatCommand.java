package net.olimpium.last_life_iii.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
				((Player) commandSender).playSound(((Player) commandSender).getLocation(), Sound.BLOCK_LEVER_CLICK,1,2);
			}else{
				hasTeamChatToggled.replace(player.getName(),false);
				commandSender.sendMessage(ChatColor.GRAY+"Se ha desactivado el chat de equipo.");
				((Player) commandSender).playSound(((Player) commandSender).getLocation(), Sound.BLOCK_LEVER_CLICK,1,0);

			}
		}else {
			hasTeamChatToggled.put(player.getName(), true);
			commandSender.sendMessage(ChatColor.GREEN + "Se ha activado el chat de equipo.");
			((Player) commandSender).playSound(((Player) commandSender).getLocation(), Sound.BLOCK_LEVER_CLICK,1,2);

		}
		return true;
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if (!TeamsManager.isUserInATeam(e.getPlayer().getName())){
			e.getPlayer().sendMessage(ChatColor.RED+"Tienes que estar en un equipo.");
		}
		if(hasTeamChatToggled.get(e.getPlayer().getName())){
			for (String memberName : TeamsManager.getTeamByName(e.getPlayer().getName()).getMembers()){
				Player member = Bukkit.getPlayer(memberName);
				if (member == null) continue;
				member.sendMessage("["+ChatColor.GREEN+"Team"+ChatColor.RESET+"] "+e.getMessage());

			}
			e.setCancelled(true);
		}
	}
}
