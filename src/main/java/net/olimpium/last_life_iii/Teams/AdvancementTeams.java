package net.olimpium.last_life_iii.Teams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementTeams implements Listener {
	@EventHandler
	public void onPlayerGetAdvancement(PlayerAdvancementDoneEvent event){
		Player player = event.getPlayer();
		if (TeamsManager.isUserInATeam(player.getName())){
			LastLifeTeam teamOfPlayer = TeamsManager.getTeamOfUser(player.getName());
			System.out.println("New advancement done!");
			teamOfPlayer.newAdvancement(event.getAdvancement());
		}
	}
}
