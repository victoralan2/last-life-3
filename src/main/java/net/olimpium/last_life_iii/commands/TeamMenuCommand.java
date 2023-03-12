package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.gui.TeamMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamMenuCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (!(commandSender instanceof Player)) return true;
		Player player = (Player) commandSender;
		TeamMenu menu = new TeamMenu(player);
		menu.open();
//		menu.
		return true;
	}
}
