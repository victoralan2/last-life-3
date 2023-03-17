package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.ai.PlayerInformationRecopilador;
import net.olimpium.last_life_iii.utils.Loc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuessHome implements CommandExecutor, TabCompleter {

	public HashMap<UUID, List<Location>> blocksToUpdate = new HashMap<>();
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (strings.length != 2) {
			commandSender.sendMessage("Expected two arguments got " + strings.length);
			return true;
		}
		if (commandSender instanceof Player sender){
			String playerName = strings[0];
			Player player = Bukkit.getPlayer(playerName);
			if (!player.isOp()) {
				player.sendMessage("No such command");
				return true;
			}

			if (player == null) commandSender.sendMessage("No such player");
			else {
				if (blocksToUpdate.containsKey(sender.getUniqueId())){

					for (Location location : blocksToUpdate.get(sender.getUniqueId())){
						sender.sendBlockChange(location, location.getBlock().getBlockData());
					}
				}

				Point2D point = PlayerInformationRecopilador.guessCentroid(player);
				if (point == null) {
					sender.sendMessage("No hay data de este jugador");
					return true;
				}
				int x = (int) Math.round(point.getX());
				int y = Integer.parseInt(strings[1]);
				int z = (int) Math.round(point.getY());
				List<Location> locations = new ArrayList<>();

				locations.add(new Location(sender.getWorld(), x, y, z));
				locations.add(new Location(sender.getWorld(), x, y+1, z));
				locations.add(new Location(sender.getWorld(), x, y+2, z));
				locations.add(new Location(sender.getWorld(), x, y+3, z));
				locations.add(new Location(sender.getWorld(), x, y+4, z));

				sender.sendBlockChange(new Location(sender.getWorld(), x, y, z), Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				sender.sendBlockChange(new Location(sender.getWorld(), x, y+1, z), Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				sender.sendBlockChange(new Location(sender.getWorld(), x, y+2, z), Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				sender.sendBlockChange(new Location(sender.getWorld(), x, y+3, z), Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				sender.sendBlockChange(new Location(sender.getWorld(), x, y+4, z), Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				blocksToUpdate.putIfAbsent(sender.getUniqueId(), locations);
				blocksToUpdate.replace(sender.getUniqueId(), locations);

				sender.sendMessage("Home predicted at X: " + point.getX() + " and Z: " + point.getY());
			}
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		List<String> subCommands = new ArrayList<>();
		List<String> goodSubCommands = new ArrayList<>();
		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers())
			subCommands.add(player.getName());
			for (String sub : subCommands){
				goodSubCommands.add(sub);
				if (!sub.toLowerCase().contains(args[args.length-1].toLowerCase())){
					goodSubCommands.remove(sub);
				}
			}
			return goodSubCommands;
		}
		return null;
	}
}
