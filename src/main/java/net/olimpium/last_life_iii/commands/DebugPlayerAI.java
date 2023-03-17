package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.ai.PlayerInformationRecopilador;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DebugPlayerAI implements CommandExecutor, TabCompleter {


	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


		if (strings.length != 2) {
			commandSender.sendMessage("Expected 2 arguments but got " + strings.length);
			return true;
		}

		String toChange = strings[0];
		switch (toChange.toUpperCase()){
			case "TIME_FACTOR": {
				PlayerInformationRecopilador.TIME_FACTOR = Double.parseDouble(strings[1]);
			}
			case "PHI": {
				PlayerInformationRecopilador.PHI = Double.parseDouble(strings[1]);
			}
			case "DECAY_MULTIPLIER": {
				PlayerInformationRecopilador.DECAY_MULTIPLIER = Double.parseDouble(strings[1]);
			}
			case "DECAY_RANGE": {
				PlayerInformationRecopilador.DECAY_RANGE = Integer.parseInt(strings[1]);
			}
			case "REINFORCEMENT_DECAY": {
				PlayerInformationRecopilador.REINFORCEMENT_DECAY = Double.parseDouble(strings[1]);
			}
			case "REINFORCEMENT_CAP": {
				PlayerInformationRecopilador.REINFORCEMENT_CAP = Double.parseDouble(strings[1]);
			}
			case "REINFORCEMENT_RANGE": {
				PlayerInformationRecopilador.REINFORCEMENT_RANGE = Integer.parseInt(strings[1]);
			}
			case "MINIMUM_DISCONNECT_TIME": {
				PlayerInformationRecopilador.MINIMUM_DISCONNECT_TIME = Integer.parseInt(strings[1]);
			}
			case "UPDATE_CHANCE": {
				PlayerInformationRecopilador.UPDATE_CHANCE = Integer.parseInt(strings[1]);
			}
			case "BLOCK_CHANCE": {
				PlayerInformationRecopilador.BLOCK_CHANCE = Integer.parseInt(strings[1]);
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		List<String> subCommands = new ArrayList<>();
		List<String> goodSubCommands = new ArrayList<>();
		if (args.length == 1) {
			subCommands.add("TIME_FACTOR");
			subCommands.add("PHI");
			subCommands.add("DECAY_MULTIPLIER");
			subCommands.add("REINFORCEMENT_DECAY");
			subCommands.add("REINFORCEMENT_CAP");
			subCommands.add("REINFORCEMENT_RANGE");
			subCommands.add("MINIMUM_DISCONNECT_TIME");
			subCommands.add("UPDATE_CHANCE");
			subCommands.add("BLOCK_CHANCE");

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
