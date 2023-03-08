package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.Teams.TeamsManager;

import java.awt.*;

public class TeamCommand {
	@SubscribeEvent
	public void onTeamCommand(SlashCommandInteractionEvent event){
		if(!event.getName().equals("team")) return;
		switch (event.getSubcommandName()){
			case "create":
				if (isUserInATeam(event.getMember().getEffectiveName())){
					event.reply("Ya estas en un equipo, salte de el antes de crear otro").queue();
					return;
				}

				String teamName = event.getOption("nombre").getAsString();
				LastLifeTeam newTeam = new LastLifeTeam(teamName, event.getMember().getEffectiveName());
				newTeam.register();
				RoleAction role = createRole();
				role.se
				break;
			case "invite":

				break;

			case "remove":

				break;
			case "kick":

				break;
			case "leave":
				// Borrar el team si no tiene mas miembros
				break;
		}
	}

	private boolean isUserInATeam(String name){
		for (LastLifeTeam team : TeamsManager.getTeamList()){
			if (team.getMembers().contains(name)) return true;
		}
		return false;
	}
	private LastLifeTeam getTeamOfUser(String name){
		for (LastLifeTeam team : TeamsManager.getTeamList()){
			if (team.getMembers().contains(name)) return team;
		}
		return null;
	}

	private RoleAction createRole(String teamName, Color color){
		RoleAction role = Bot.bot.getGuildById(Bot.LastLifeGuild).createCopyOfRole();
	}
}
