package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
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
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.olimpium.last_life_iii.Last_life_III;
import net.olimpium.last_life_iii.utils.TimeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
                .addEventListeners(new VerifyCommand())
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

        //adds a command
        commands.addCommands(
                Commands.slash("verify", "Utiliza este comando para comenzar la verificación.")
                        .setGuildOnly(true),
                Commands.slash("mineban", "Banea a un usuario.")
                        .addOption(OptionType.USER,"usuario","Banea al usuario selecionado en MINECRAFT y DISCORD",true)
                        .addOption(OptionType.STRING,"razón","Razón del baneo",true)
                        .addOption(OptionType.INTEGER,"tiempo","tiempo del baneao", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
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
    public static boolean playerRegistered(String token, Player player){
        //checks if it has a token
        if (tokenMap.containsKey(token)){

            Member member = LastLifeGuild.retrieveMemberById(tokenMap.get(token)).complete();

            //adds roles
            member.getGuild().addRoleToMember(member, member.getGuild().getRoleById("1009077402896433183")).queue();
            member.getGuild().addRoleToMember(member, member.getGuild().getRoleById("913493620210925568")).queue();
            if (!member.getEffectiveName().equals(player.getName())){
                //modyfies name
                member.modifyNickname(player.getName()).queue();
            }
            //removes token from list
            tokenMap.remove(token);
            member.getUser().openPrivateChannel().queue((channel) ->
            {
                EmbedBuilder VerifyEmbed = new EmbedBuilder();
                VerifyEmbed.setTitle(":white_check_mark: Se ha verificado correctamente como: "+player.getName()+" :white_check_mark: ", null);
                VerifyEmbed.setColor(Color.GREEN);
                channel.sendMessageEmbeds(VerifyEmbed.build()).queue();

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

    public static void serverMaintenance(boolean closed, boolean verbosse, String MTime){
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
            if (verbosse){
                Message trueMessage = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessageEmbeds(baseEmbed.setDescription("Tiempo esperado: **"+MTime + " minutos**").build()).complete();
                trueMessage.addReaction(Emoji.fromUnicode("U+1F480")).queue();
                Message message = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessage("@everyone" + "Servidor en mantenimiento").complete();
                message.delete().queue();
            } else {
                Message trueMessage = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessageEmbeds(baseEmbed.build()).complete();
                trueMessage.addReaction(Emoji.fromUnicode("U+1F480")).queue();;
                Message message = bot.getChannelById(NewsChannel.class,"919552536850075658").sendMessage("@everyone" + "Servidor en mantenimiento").complete();
                message.delete().queue();
            }
        }
    }

    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (!event.getMessage().getAuthor().getId().equals(bot.getSelfUser().getId()) && event.getMessage().getChannel().getId().equals("1036742648758800424")){

            Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"[DISCORD]"+ChatColor.RESET+" <"+event.getMessage().getAuthor().getName()+"> "+event.getMessage().getContentDisplay());
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
        if(!e.getMessage().contains("@") && !e.getMessage().equalsIgnoreCase("Boop")){
            TextChannel CommunicationChannel = bot.getChannelById(TextChannel.class, "1036742648758800424");
            CommunicationChannel.sendMessage("**<"+e.getPlayer().getName()+">** "+e.getMessage()).queue();
        } else if (e.getMessage().equalsIgnoreCase("Boop")){
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.DARK_PURPLE + ChatColor.MAGIC.toString() +"A" +ChatColor.LIGHT_PURPLE+ ChatColor.ITALIC+ "Boop" + ChatColor.RESET +  ChatColor.DARK_PURPLE.toString() +  ChatColor.MAGIC+"A");
        } else {
            e.getPlayer().sendMessage(ChatColor.DARK_GRAY + ChatColor.ITALIC.toString()+"No puedes mencionar a usuarios desde aquí");
        }

    }
}