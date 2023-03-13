package net.olimpium.last_life_iii.discordBot;

import com.fren_gor.ultimateAdvancementAPI.events.team.TeamLoadEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.minecraft.server.v1_16_R3.TileInventory;
import net.minecraft.server.v1_16_R3.Tuple;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.utils.TimeSystem;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamCommand extends ListenerAdapter {

	public static HashMap<Member, Tuple<LastLifeTeam, Long>> invitesHashMap = new HashMap<>();

	@SubscribeEvent
	public void onTeamCommand(SlashCommandInteractionEvent event){

		if(!event.getName().equals("team")) return;
		if (TimeSystem.getFixedTime() != 0 && TimeSystem.getWeek() != 0){
			event.reply("No se pueden cambiar los equipos después del comienzo del survival").setEphemeral(true).queue();
			System.out.println("EPOCH: " + TimeSystem.getFixedTime());
			System.out.println("Weed: " + TimeSystem.getWeek());

			return;
		}
		try {
			switch (event.getSubcommandName()){
				case "create":
					if (TeamsManager.isUserInATeam(event.getMember().getEffectiveName())){
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
					if (event.getOption("nombre").getAsString().length() >= 16){
						event.reply("El tamaño maxim del nombre del team es de 16 caracteres").setEphemeral(true).queue();
						return;
					}
					LastLifeTeam newTeam = new LastLifeTeam(teamName, color, event.getMember().getEffectiveName());
					newTeam.register();
					Role role = createRole(teamName, color);

					event.getGuild().addRoleToMember(event.getMember(), role).queue();
					event.reply("El team se ha creado!").setEphemeral(true).queue();
					break;
				case "invite":

					if (!TeamsManager.isUserInATeam(event.getMember().getEffectiveName())){
						event.reply("No estas actualmente en un team").setEphemeral(true).queue();
						return;
					}
					if (TeamsManager.getTeamOfUser(event.getMember().getEffectiveName()).getMembers().size() == LastLifeTeam.getMaxMembers()){
						event.reply("Tu team ya tiene la cantidad maxima de miembros.").setEphemeral(true).queue();
						return;
					} else if (TeamsManager.getTeamOfUser(event.getMember().getEffectiveName()).getMembers().size() > LastLifeTeam.getMaxMembers()){
						Last_life_III.theDracon_.sendMessage("UN TEAM TIENE MAS DE LOS MIEMBROS POSIBLES: " + TeamsManager.getTeamOfUser(event.getMember().getEffectiveName()).getName());
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

					LastLifeTeam team = TeamsManager.getTeamOfUser(event.getMember().getEffectiveName());
					if (TeamsManager.isUserInATeam(event.getGuild().getMembersByEffectiveName(event.getOption("usuario").getAsMember().getEffectiveName(), false).get(0).getEffectiveName())){
						event.reply("Esta persona ya esta en un team").setEphemeral(true).queue();
						return;
					}
					sendInviteToMember(event.getGuild().getMembersByEffectiveName(event.getOption("usuario").getAsMember().getEffectiveName(), false).get(0), team);

					event.reply("Se ha enviado una solicitud de invitación a " + event.getOption("usuario").getAsMember().getEffectiveName() + " que expirará <t:" + (Instant.now().getEpochSecond() + 300) + ":R>.").setEphemeral(true).queue();
					break;

				case "remove":
					if (TeamsManager.isUserInATeam(event.getMember().getEffectiveName())){
						getRoleOf(TeamsManager.getTeamOfUser(event.getMember().getEffectiveName())).delete().queue();
						TeamsManager.unregisterTeam(TeamsManager.getTeamOfUser(event.getMember().getEffectiveName()));
						event.reply("El team ha sido borrado!").setEphemeral(true).queue();
					}
					break;
				case "kick":
					if (TeamsManager.isUserInATeam(event.getMember().getEffectiveName())) {
						Member toKick = event.getOption("usuario").getAsMember();
						if (toKick.equals(event.getMember())){
							event.reply("No te puedes echar a tí mismo del team. Sí quieres hacer esto usa /team leave.").setEphemeral(true).queue();
							return;
						}
						LastLifeTeam team2 = TeamsManager.getTeamOfUser(event.getMember().getEffectiveName());
						if (team2.getMembers().contains(toKick.getEffectiveName())) {
							team2.removeMember(toKick.getEffectiveName());
							event.getGuild().removeRoleFromMember(toKick, getRoleOf(team2)).queue();



							EmbedBuilder builder = new EmbedBuilder();
							builder.setTitle("Integrante echado de tu equipo");
							builder.addField(new MessageEmbed.Field("Un integrante ha sido echado de tu equipo de Last Life", "El usuario " + toKick.getEffectiveName() + " ha sido echado de tu equipo (" + team2.getName() + ").", true));


							EmbedBuilder builder2 = new EmbedBuilder();
							builder2.setTitle("Has sido echado de tu equipo");
							builder2.addField(new MessageEmbed.Field("Has sido echado de equipo de Last Life", "El usuario " + event.getMember().getEffectiveName() + " te ha echado del tu antiguo equipo (" + team2.getName() + ").", true));

							for (String memberName : team2.getMembers()){
								for (Member member : event.getGuild().getMembersByEffectiveName(memberName, false)){
									if (member == event.getMember()) continue;
									member.getUser().openPrivateChannel().
											flatMap(channel -> channel.sendMessageEmbeds(builder.build())).queue();
								}
							}
							toKick.getUser().openPrivateChannel().
									flatMap(privateChannel -> privateChannel.sendMessageEmbeds(builder2.build())).queue();
							event.reply("Se ha echado al usuario del team").setEphemeral(true).queue();

						} else {
							event.reply("No puedes echar usuarios que no estén en tu team.").setEphemeral(true).queue();
							return;
						}
					}
					break;
				case "leave":
					if(TeamsManager.isUserInATeam(event.getMember().getEffectiveName())) {
						String memberName1 = event.getMember().getEffectiveName();
						LastLifeTeam team3 = TeamsManager.getTeamOfUser(event.getMember().getEffectiveName());
						Role teamRole = getRoleOf(team3);
						team3.removeMember(event.getMember().getEffectiveName());
						event.getGuild().removeRoleFromMember(event.getMember(), teamRole).queue();
						if (team3.getMembers().size() != 0) {
							event.reply("Te has ido del equipo satisfactoriamente").setEphemeral(true).queue();


							EmbedBuilder builder = new EmbedBuilder();
							builder.setTitle("Integrante se ha ido de tu equipo");
							builder.addField(new MessageEmbed.Field("Un integrante se ha ido de tu equipo de Last Life", "El usuario "+memberName1+" se ha ido de tu equipo ("+team3.getName()+").", true));

							for (String memberName : team3.getMembers()){
								for (Member member1 : event.getGuild().getMembersByEffectiveName(memberName, false)){
									member1.getUser().openPrivateChannel().
											flatMap(channel -> channel.sendMessageEmbeds(builder.build())).queue();
								}
							}
						} else {
							TeamsManager.unregisterTeam(team3);
							event.reply("Te has ido del equipo y se ha borrado el equipo satisfactoriamente.").setEphemeral(true).queue();
						}
					}
					break;
				case "list":
					// SOLO PARA ADMINS
					if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("Lista de teams");
						List<LastLifeTeam> teamList = TeamsManager.getTeamList();
						List<String> teamsName = new ArrayList<>();
						teamList.forEach(everyTeam -> teamsName.add(everyTeam.getName()));

						builder.addField(new MessageEmbed.Field(teamsName.toString(), "", true));
						event.replyEmbeds(builder.build()).setEphemeral(true).queue();
					} else {
						event.reply("Este comando es solo para administradores").setEphemeral(true).queue();
					}
			}
		} catch (Exception e){
			e.printStackTrace();
			event.reply("Ha ocurrido un error, por favor, reporte esto a los administradores del servidor").setEphemeral(true).queue();
		}

	}
//	private final String[] colors = new String[]{"white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "pink", "magenta"};
//
//	@SubscribeEvent
//	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
//		if (event.getName().equals("team") && event.getFocusedOption().getName().equals("color")) {
//
//			List<Command.Choice> options = Stream.of(colors)
//					.filter(color -> color.contains(event.getFocusedOption().getValue().toLowerCase()))// only display words that start with the user's current input
//					.map(color -> new Command.Choice(color, color)) // map the words to choices
//					.collect(Collectors.toList());
//			event.replyChoices(options).queue();
//		}
//	}
	public void sendInviteToMember(Member member, LastLifeTeam team){
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setTitle("Invitación a un equipo");

		embedBuilder.addField(new MessageEmbed.Field("¡Has sido invitado a un equipo de Last Life!", "El equipo " + team.getName() + " te a invitado a unirte a el, la esta invitación expirará: <t:" + (Instant.now().getEpochSecond()  + 300) + ":R>", false));

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
			if (TeamsManager.isUserInATeam(member.getEffectiveName())){
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
				event.getMessage().delete().queue();
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

	public static Role getRoleOf(LastLifeTeam team){
		for (Role role : Bot.LastLifeGuild.getRolesByName(team.getName(), false)){
			if (role == null) continue;
			return role;
		}
		return null;
	}



	private Color getColorOf(String hexColor){
		hexColor = hexColor.toUpperCase();
		try {
			return Color.decode(hexColor);
		} catch (NumberFormatException e){
			return Color.white;
		}
 	}
	private Role createRole(String teamName, Color color){
		return Bot.LastLifeGuild.createCopyOfRole(Bot.LastLifeGuild.getRoleById("1083109649345159319")).setColor(color).setName(teamName).complete();
	}
}
