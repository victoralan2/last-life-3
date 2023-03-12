package net.olimpium.last_life_iii.gui.teams;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.Teams.TeamLevel;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.gui.Heads;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class TeamMenu extends Gui {
	public TeamMenu(Player player) {
		super(player, "team-gui", "Menu del Team", 3);
	}

	@Override
	public void onOpen(InventoryOpenEvent event) {

		if (!TeamsManager.isUserInATeam(event.getPlayer().getName())) {
			event.getPlayer().sendMessage(ChatColor.RED + "No puedes abrir este menu si no estas en un team, si quieres entrar a un team contacta con los administradores");
			event.setCancelled(true);
			return;
		}

		// CREATE ITEMS
		Icon nothing = new Icon(Material.BLACK_STAINED_GLASS_PANE);
		nothing.setName(" ");
		nothing.hideFlags(ItemFlag.HIDE_ATTRIBUTES);

		Icon enderChest = new Icon(Heads.ENDER_CHES.getItemStack());
		enderChest.setName(ChatColor.RESET.toString() + ChatColor.YELLOW + ChatColor.BOLD + "Ender Chest del team");
		enderChest.setLore(ChatColor.RESET.toString() + ChatColor.GRAY + " > Haz click para abrir");

		Icon stats = new Icon(Material.EXPERIENCE_BOTTLE);
		stats.setName((ChatColor.RESET.toString() + ChatColor.GOLD + ChatColor.BOLD + "Estadísticas extra"));
		// THIS.WORK();
		LastLifeTeam team = TeamsManager.getTeamOfUser(event.getPlayer().getName());
		List<String> members = team.getMembers();

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Nombre del equipo: " + ChatColor.DARK_RED + team.getName());
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Integrantes del equipo: ");

		members.forEach(member -> stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "     - " + member));

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Nivel del equipo: " + team.getLevel());
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "     " + team.getExp() + " / " + TeamsManager.getExpByLevel(team.getLevel()+1) + " para pasar al siguiente nivel");
		TeamLevel teamLevel = new TeamLevel(team.getLevel());

		ArrayList<Integer> levelStats = teamLevel.getStats();
		ArrayList<Integer> levelStatsNextLevel = teamLevel.getStats(team.getLevel() +1);

		int enderChestSize = levelStatsNextLevel.get(0) - levelStats.get(0);
		int amountOfTrinkets = levelStatsNextLevel.get(1) - levelStats.get(1);

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Siguiente nivel:");

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    + " + enderChestSize + " filas de ender chest");
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    + " + amountOfTrinkets + " espacio de trinkets");

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Logros totales:");
		int totalAdvancementsNoChallenges = TeamsManager.advancementsNoChallengeOf(team, false);
		int totalChallenges = TeamsManager.challangesOf(team, false);
		int uniqueAdvancements = TeamsManager.advancementsNoChallengeOf(team, true);
		int uniqueChallenges = TeamsManager.challangesOf(team, true);

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    " + totalAdvancementsNoChallenges + " - Logros");
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    " + totalChallenges + " - Desafíos");
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    " + uniqueAdvancements + " - Logros unicos");
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.YELLOW + "    " + uniqueChallenges + " - Desafíos unicos");

		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Nivel del ender chest (cada nivel + 1 fila): " + levelStats.get(0));
		stats.appendLore(ChatColor.RESET.toString() + ChatColor.WHITE + "Espacio de trinkets: " + levelStats.get(1));

		Icon sack = new Icon(Heads.SACK.getItemStack());
		sack.setName((ChatColor.RESET.toString() + ChatColor.YELLOW + ChatColor.BOLD + "Saco de trinkets"));
		sack.setLore(ChatColor.RESET.toString() + ChatColor.GRAY + " > Haz click para abrir");



		// ADD ITEMS TO GUI
		fillGui(nothing);
		addItem(10, enderChest);
		addItem(13, stats);

		addItem(16, sack);


		// ON CLICK EVENTS
		enderChest.onClick(clickEvent->
				clickEvent.getWhoClicked().openInventory(TeamsManager.getTeamOfUser(clickEvent.getWhoClicked().getName()).getEnderChest())
		);

		sack.onClick(clickEvent->{
			clickEvent.getWhoClicked().openInventory(TeamsManager.getTeamOfUser(clickEvent.getWhoClicked().getName()).getTrinketSack());

		});
	}

}
