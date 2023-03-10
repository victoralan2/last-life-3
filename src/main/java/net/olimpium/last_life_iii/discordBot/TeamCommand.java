package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.minecraft.server.v1_16_R3.TileInventory;
import net.minecraft.server.v1_16_R3.Tuple;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.utils.TimeSystem;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamCommand extends ListenerAdapter {

	public static HashMap<Member, Tuple<LastLifeTeam, Long>> invitesHashMap = new HashMap<>();

	@SubscribeEvent
	public void onTeamCommand(SlashCommandInteractionEvent event){
		//TODO QUE CUANDO LAST EMPIEIZE LOS TEAMS NO CAMBIEN

		if(!event.getName().equals("team")) return;
		if (TimeSystem.getFixedTime() == 0 && TimeSystem.getWeek() == 0){
			event.reply("No se pueden cambiar los equipos después del comienzo del survival").setEphemeral(true).queue();
			return;
		}
		switch (event.getSubcommandName()){
			case "create":
				if (isUserInATeam(event.getMember().getEffectiveName())){
					event.reply("Ya estas en un equipo, salte de el antes de crear otro").setEphemeral(true).queue();
					return;
				}
				System.out.println("Se esta creando el team");
				String teamName = event.getOption("nombre").getAsString();
				Color color = getColorOf(event.getOption("color").getAsString());
				if (TeamsManager.getTeamByName(teamName) != null){
					event.reply("Ya hay un equipo con ese nombre").setEphemeral(true).queue();
					System.out.println("Someone tried to create a team with an existing name");
					return;
				}
				LastLifeTeam newTeam = new LastLifeTeam(teamName, color, event.getMember().getEffectiveName());
				newTeam.register();
				Role role = createRole(teamName, color);

				event.getGuild().addRoleToMember(event.getMember(), role).queue();
				event.reply("El team se ha creado!").setEphemeral(true).queue();
				break;
			case "invite":

				if (!isUserInATeam(event.getMember().getEffectiveName())){
					event.reply("No estas actualmente en un team").setEphemeral(true).queue();
					return;
				}
				if (getTeamOfUser(event.getMember().getEffectiveName()).getMembers().size() == LastLifeTeam.getMaxMembers()){
					event.reply("Tu team ya tiene la cantidad maxima de miembros.").setEphemeral(true).queue();
					return;
				} else if (getTeamOfUser(event.getMember().getEffectiveName()).getMembers().size() > LastLifeTeam.getMaxMembers()){
					Last_life_III.theDracon_.sendMessage("UN TEAM TIENE MAS DE LOS MIEMBROS POSIBLES: " + getTeamOfUser(event.getMember().getEffectiveName()).getName());
				}
				try {
					if (event.getGuild().getMembersByEffectiveName(event.getOption("usuario").getAsMember().getEffectiveName(), false).get(0) == null) {
						event.reply("El usuario especificado ( " + event.getOption("usuario") + " ) no existe. Si cree que esto es un error, repórtelo").setEphemeral(true).queue();
						return;
					}
				} catch (NullPointerException | IndexOutOfBoundsException e){
					e.printStackTrace();
					event.reply("El usuario especificado ( " + event.getOption("usuario").getAsMember().getEffectiveName() + " ) no existe. Si cree que esto es un error, repórtelo").setEphemeral(true).queue();
					return;
				}

				LastLifeTeam team = getTeamOfUser(event.getMember().getEffectiveName());
				if (isUserInATeam(event.getGuild().getMembersByEffectiveName(event.getOption("usuario").getAsMember().getEffectiveName(), false).get(0).getEffectiveName())){
					event.reply("Esta persona ya esta en un team").setEphemeral(true).queue();
					return;
				}
				sendInviteToMember(event.getGuild().getMembersByEffectiveName(event.getOption("usuario").getAsMember().getEffectiveName(), false).get(0), team);

				event.reply("Se ha enviado una solicitud de invitación a " + event.getOption("usuario").getAsMember().getEffectiveName() + ". Tiene 5 minutos para aceptarla").setEphemeral(true).queue();
				break;

			case "remove":

				break;
			case "kick":
				//TODO hacer votación para hechar a alguiein del team
				break;
			case "leave":
				// Borrar el team si no tiene mas miembros
				break;
		}
	}
	private final String[] colors = new String[]{"white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "pink", "magenta"};
	public void sendInviteToMember(Member member, LastLifeTeam team){
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Invitación a un equipo");

		embedBuilder.addField(new MessageEmbed.Field("¡Has sido invitado a un equipo de Last Life!", "El equipo " + team.getName() + " te a invitado a unirte a el, la esta invitación expirará: <t:" + (System.currentTimeMillis() / 1000)  + 60 * 5 + ":R>", false));

		member.getUser().openPrivateChannel()
				.flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build()).addActionRow(Button.success("accept_invite", "Aceptar"), Button.danger("decline_invite", "Rechazar"))).queue();
		Tuple<LastLifeTeam, Long> tuple = new Tuple<>(team, System.currentTimeMillis());
		invitesHashMap.put(member, tuple);
	}


	@SubscribeEvent
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if (event.getComponentId().equals("accept_invite")) {
			User user = event.getUser();
			Guild lastGuild = Bot.LastLifeGuild;
			Member member = lastGuild.getMemberById(user.getId());
			if (!invitesHashMap.containsKey(member)) {
				event.reply("La invitación a expirado, el mensaje de invitación se borrara.").setEphemeral(true).queue();
				event.getMessage().delete().queue();
				return;
			}
			Tuple<LastLifeTeam, Long> tuple = invitesHashMap.get(member);
			if (tuple.b() + 1000 * 60 * 5 < System.currentTimeMillis()){
				event.reply("La invitación a expirado, el mensaje de invitación se borrara.").setEphemeral(true).queue();
				event.getMessage().delete().queue();
				return;
			}
			if (isUserInATeam(member.getEffectiveName())){
				event.reply("Ya estas en un team, abandonalo para entrar a otro.").setEphemeral(true).queue();
				return;
			}
			for (Role teamRole : Bot.LastLifeGuild.getRolesByName(tuple.a().getName(), false)){
				member.getGuild().addRoleToMember(member, teamRole).queue();
			}
			event.reply("Has entrado a el team \"" + tuple.a().getName() +"\" correctamente, el mensaje de invitación se borrara.").setEphemeral(true).queue();
			event.getMessage().delete().queue();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Nuevo integrante del team");
			builder.addField(new MessageEmbed.Field("Usuario " + member.getEffectiveName() + " ha entrado al team", member.getEffectiveName() + " ha aceptado la invitación para entrar a tu team", true));

			for (String memberName : tuple.a().getMembers()){
				for (Member member1 : lastGuild.getMembersByEffectiveName(memberName, false)){
					member1.getUser().openPrivateChannel().
							flatMap(channel -> channel.sendMessageEmbeds(builder.build())).queue();
				}
			}
			tuple.a().addMember(member.getEffectiveName());

			invitesHashMap.remove(member);

		} else if (event.getComponentId().equals("decline_invite")) {
			User user = event.getUser();
			Guild lastGuild = Bot.LastLifeGuild;
			Member member = lastGuild.getMemberById(user.getId());
			if (!invitesHashMap.containsKey(member)){
				event.reply("Esta invitación ya ha expirado").setEphemeral(true).queue();
				return;
			}

			Tuple<LastLifeTeam, Long> tuple = invitesHashMap.get(member);
			event.reply("Has rechazado la invitación al team \"" + tuple.a().getName() +"\", el mensaje de invitación se borrara.").setEphemeral(true).queue();
			event.getMessage().delete().queue();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Rechazo de invitación del team");
			builder.addField(new MessageEmbed.Field("Invitation a team rechazada por " + member.getEffectiveName(), member.getEffectiveName() + " ha rechazado la invitación para entrar a tu team", true));
			for (String memberName : tuple.a().getMembers()){
				for (Member member1 : lastGuild.getMembersByEffectiveName(memberName, false)){
					member1.getUser().openPrivateChannel().
							flatMap(channel -> channel.sendMessageEmbeds(builder.build())).queue();
				}
			}
			invitesHashMap.remove(member);
		}
	}
	@SubscribeEvent
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
		if (event.getName().equals("team") && event.getFocusedOption().getName().equals("color")) {

			List<Command.Choice> options = Stream.of(colors)
					.filter(color -> color.contains(event.getFocusedOption().getValue().toLowerCase()))// only display words that start with the user's current input
					.map(color -> new Command.Choice(color, color)) // map the words to choices
					.collect(Collectors.toList());
			event.replyChoices(options).queue();
		}
	}
	public static void removeRoleOf(LastLifeTeam team){
		for (Role role : Bot.LastLifeGuild.getRolesByName(team.getName(), false)){
			if (role == null) continue;
			role.delete().queue();
		}
	}
	private boolean isUserInATeam(String name){
		try {
			for (LastLifeTeam team : TeamsManager.getTeamList()){

				if (team.getMembers().contains(name)) return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}
	private LastLifeTeam getTeamOfUser(String name){
		for (LastLifeTeam team : TeamsManager.getTeamList()){
			if (team.getMembers().contains(name)) return team;
		}
		return null;
	}

	private Color getColorOf(String color){
		color = color.toUpperCase();
		switch (color){
			case "WHITE":
				return Color.WHITE;
			case "LIGHT_GRAY":
				return Color.LIGHT_GRAY;
			case "GRAY":
				return Color.GRAY;
			case "BLACK":
				return Color.BLACK;
			case "RED":
				return Color.RED;
			case "ORANGE":
				return Color.ORANGE;
			case "YELLOW":
				return Color.YELLOW;
			case "LIME":
				return Color.GREEN;
			case "GREEN":
				return Color.getHSBColor(180, 50, 50.1f);
			case "CYAN":
				return Color.CYAN;
			case "LIGHT_BLUE":
				return Color.getHSBColor(217F, 73.7f, 100f);
			case "BLUE":
				return Color.blue;
			case "PURPLE":
				return Color.getHSBColor(291, 100, 100);
			case "PINK":
				return Color.PINK;
			case "MAGENTA":
				return Color.MAGENTA;
		}
		return Color.white;
	}
	private Role createRole(String teamName, Color color){
		return Bot.LastLifeGuild.createCopyOfRole(Bot.LastLifeGuild.getRoleById("1083109649345159319")).setColor(color).setName(teamName).complete();
	}
}
