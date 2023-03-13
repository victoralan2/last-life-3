package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.Teams.LastLifeTeam;
import net.olimpium.last_life_iii.commands.TeamChatCommand;
import net.olimpium.last_life_iii.utils.TimeSystem;
import net.olimpium.last_life_iii.utils.VerificationSystem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Bot extends ListenerAdapter implements Listener {
    //bot object
    public static JDA bot;

    public static Guild LastLifeGuild;
    //map with all <Tokens, id>
    public static Map<String, String> tokenMap = new HashMap<>();
    public static Map<String, String> mapToken = new HashMap<>();
    //starts the bot
    public static void start() throws InterruptedException {

        //bot builder+ config
        bot = JDABuilder.createDefault(Last_life_III.getBotToken())
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new Bot())
                .addEventListeners(new TeamCommand())
                .addEventListeners(new VerifyCommand())
                .addEventListeners(new CloseCommand())
                .addEventListeners(new BanCommand())
                .setChunkingFilter(ChunkingFilter.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(EnumSet.allOf(CacheFlag.class))
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        //list of commands
        CommandListUpdateAction commands = bot.updateCommands();
        //TODO hacer que no se puedan cambiar ni borrar teams cuando la semana ya ha empezado
        //adds a command
        commands.addCommands(
                Commands.slash("verify", "Utiliza este comando para comenzar la verificación.")
                        .setGuildOnly(true),
                Commands.slash("mineban", "Banea a un usuario.")
                        .addOption(OptionType.USER,"usuario","Bloquea al usuario selecionado en MINECRAFT y DISCORD",true)
                        .addOption(OptionType.STRING,"razón","Razón del bloqueo",true)
                        .addOption(OptionType.INTEGER,"tiempo","Tiempo del bloqueo", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                        .setGuildOnly(true),
                Commands.slash("maintenance","Pone o quita el mantenimiento el servidor.")
                        .addOption(OptionType.INTEGER, "tiempo","Tiempo aproximado de mantenimiento (en minutos)")
                        .setGuildOnly(true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("team","Crea un equipo, para last life (máximo número de miembros "+ LastLifeTeam.maxMembers+")")
                        .addSubcommands(
                                new SubcommandData("create","Crea un nuevo team.")
                                        .addOption(OptionType.STRING, "nombre", "El nombre del team.", true)
                                        .addOption(OptionType.STRING, "color", "El color **EN HEXADECIMAL**", true),
                                new SubcommandData("invite","Crea un nuevo team.")
                                        .addOption(OptionType.USER,"usuario","Invita un usuario a tu team.", true),
                                new SubcommandData("remove", "Borra el team actual."),
                                new SubcommandData("kick","Expulsa a un usuario del team.")
                                        .addOption(OptionType.USER,"usuario","El usuario que será expulsado del team.",true),
                                new SubcommandData("leave","Salirte del team actual"),
                                new SubcommandData("list","Lista de los teams (Solo para admins)")

                        )
                        .setGuildOnly(true),
                Commands.context(Command.Type.USER,"Banear de Last Life")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                        .setGuildOnly(true)
        ).queue();

        //queue the commands
        commands.queue();

        //waits until bot ready
        bot.awaitReady();

        //sets the global guild
        LastLifeGuild = bot.getGuildById("913493619875385425");


        System.out.println(ChatColor.GREEN+"Successfully joined as:");
        System.out.println(ChatColor.GOLD+"Last Life III"+ChatColor.YELLOW+"#6835");
    }



    //shutdown's the bot
    public static void shutDown(){
        bot.shutdown();
    }
    //when the player is registered
    public static Boolean playerRegistered(String token, Player player){
        //checks if it has a token
        if (tokenMap.containsKey(token)){
            for (Member member : LastLifeGuild.getMembersByEffectiveName(player.getName(), false)){
                if (VerificationSystem.isVerified(member.getEffectiveName())){
                    return null;
                }
            }
            Member member = LastLifeGuild.retrieveMemberById(tokenMap.get(token)).complete();

            //adds roles
            member.getGuild().addRoleToMember(member, member.getGuild().getRoleById("1009077402896433183")).queue();
            member.getGuild().addRoleToMember(member, member.getGuild().getRoleById("913493620210925568")).queue();
            if (!member.getEffectiveName().equals(player.getName())){
                //modify name
                member.modifyNickname(player.getName()).queue();
            }
            //removes token from list
            tokenMap.remove(token);
            member.getUser().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder verifyEmbed = new EmbedBuilder();
                verifyEmbed.setTitle(":white_check_mark: Se ha verificado correctamente como: "+player.getName()+" :white_check_mark: ", null);
                verifyEmbed.setColor(Color.GREEN);
                channel.sendMessageEmbeds(verifyEmbed.build()).queue();

            });
            return true;
        }
        return false;

    }

    //have to make a method that gives the 7th role
    public static void addWeekRoleToMembers(){
        for (Member member : LastLifeGuild.getMembers()){
            if (member.getRoles().contains(LastLifeGuild.getRoleById("913493620210925568"))){
                if (TimeSystem.getWeek() == 2) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030496583185350750")).queue();
                if (TimeSystem.getWeek() == 3) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1009092210513420288")).queue();
                if (TimeSystem.getWeek() == 4) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030497341347733625")).queue();
                if (TimeSystem.getWeek() == 5) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030497458951835659")).queue();
                if (TimeSystem.getWeek() == 6) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030497469982834711")).queue();
                if (TimeSystem.getWeek() == 7) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030497473191485522")).queue();
                if (TimeSystem.getWeek() == 8) LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("1030497476437868544")).queue();
            }
        }
    }
    public static void playerDeath(Player player, String reason) {
        try{
            //Embed builderr (eb)
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("*" + player.getName() + " ha muerto PARA SIEMPRE*", null);
            eb.setColor(Color.RED);
            eb.setDescription(reason);

            //sends embed
            bot.getChannelById(TextChannel.class, "1008716703636652083").sendMessageEmbeds(eb.build()).queue();

            //@everyone message
            Message DecoyMessage =  bot.getChannelById(TextChannel.class, "1008716703636652083").sendMessage("@everyone " + player.getName() + " ha muerto").complete();


            //delete message after 0.05 seconds
            DecoyMessage.delete().queueAfter(50, TimeUnit.MICROSECONDS);

            // TO DO
            for (Member member : LastLifeGuild.getMembers()){
                if (member.getEffectiveName().equals(player.getName())){
                    LastLifeGuild.removeRoleFromMember(member,LastLifeGuild.getRoleById("913493620210925568")).queue();
                    LastLifeGuild.addRoleToMember(member,LastLifeGuild.getRoleById("913493619875385431")).queue();
                }
            }

        } catch (Exception exception){
            exception.printStackTrace();
        }

    }
    public void onBanCommand(SlashCommandInteractionEvent event){
        if (!event.getName().equals("ban")) return;
        event.deferReply(true);
            //if()
    }

    /**
     * @param closed if it closes
     * @param time The time minutes
     */
    public static void serverMaintenance(boolean closed,  @Nullable Integer time){
        if (!closed){
            EmbedBuilder baseEmbed = new EmbedBuilder();
            baseEmbed.setTitle("**Servidor abierto**");
            baseEmbed.setColor(Color.GREEN);

            bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessageEmbeds(baseEmbed.build()).queue();
            Message message = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessage("@everyone Servidor abierto").complete();
            message.delete().queue();
        } else {
            EmbedBuilder baseEmbed = new EmbedBuilder();
            baseEmbed.setTitle("**Servidor en mantenimiento**");
            baseEmbed.setColor(Color.RED);

            if (time != null){
                Message trueMessage = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessageEmbeds(baseEmbed.setDescription("Tiempo esperado: **"+ time + " minutos**").build()).complete();
                trueMessage.addReaction(Emoji.fromUnicode("U+1F480")).queue();
                Message message = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessage("@everyone" + "Servidor en mantenimiento").complete();
                message.delete().queue();
            } else {
                Message trueMessage = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessageEmbeds(baseEmbed.build()).complete();
                trueMessage.addReaction(Emoji.fromUnicode("U+1F480")).queue();
                Message message = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessage("@everyone" + "Servidor en mantenimiento").complete();
                message.delete().queue();
            }
        }
    }

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getMessage().getAuthor().getId().equals(bot.getSelfUser().getId()) && event.getMessage().getChannel().getId().equals("1036742648758800424")){

            Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"[DISCORD]"+ChatColor.RESET+" <"+event.getMessage().getMember().getEffectiveName()+"> "+event.getMessage().getContentDisplay());
            for (Member member : event.getMessage().getMentions().getMembers()){
                Bukkit.getPlayer(member.getEffectiveName()).playSound(Bukkit.getPlayer(member.getEffectiveName()).getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 2);
            }
        }
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e){
        //if (!e.getMessage().startsWith("!")) return;
        //String messageWithoutPrefix = Arrays.stream(e.getMessage().split("!")).toList().remove(0);
        //e.setMessage(messageWithoutPrefix);
        if(!e.getMessage().contains("@") && !e.getMessage().equalsIgnoreCase("Boop")) {
            TeamChatCommand.hasTeamChatToggled.putIfAbsent(e.getPlayer().getName(), false);
            if (TeamChatCommand.hasTeamChatToggled.get(e.getPlayer().getName()).equals(false)) {
                TextChannel CommunicationChannel = bot.getChannelById(TextChannel.class, "1036742648758800424");
                CommunicationChannel.sendMessage("**<" + e.getPlayer().getName() + ">** " + e.getMessage()).queue();
            }
            } else if (e.getMessage().equalsIgnoreCase("Boop")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.DARK_PURPLE + ChatColor.MAGIC.toString() + "A" + ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + "Boop" + ChatColor.RESET + ChatColor.DARK_PURPLE.toString() + ChatColor.MAGIC + "A");
            } else {
                e.getPlayer().sendMessage(ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + "No puedes mencionar a usuarios desde aquí");
            }
    }
}