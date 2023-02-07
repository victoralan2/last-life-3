package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.olimpium.last_life_iii.Last_life_III;
import org.apache.commons.lang.RandomStringUtils;

import java.awt.*;
import java.util.LinkedList;
import java.util.Map;

public class VerifyCommand{
    @SubscribeEvent
    public void onVerifyCommand(SlashCommandInteractionEvent event)
    {
        try {
            //checks if command is verify
            if (!event.getName().equals("verify")) return;
            InteractionHook mess = event.deferReply(true).complete();


            //checks if is in the verification channel
            if (!event.getChannel().getId().equals("1009101893852741702")) {
                mess.editOriginal("Este commando solo se puede utilizar en " + event.getJDA().getChannelById(TextChannel.class, "1009101893852741702").getAsMention()).queue();
                return;
            }
            //generates random 5 letter token
            String token = RandomStringUtils.randomAlphabetic(5).toUpperCase();

            //checks if the tokenMap contains the username
            if (!Bot.tokenMap.containsValue(event.getUser().getId())) {

                //sends private embed with instructions
                event.getUser().openPrivateChannel().queue((channel) ->
                {
                    EmbedBuilder VerifyEmbed = new EmbedBuilder();
                    VerifyEmbed.setTitle("Para seguir con la verificación sigua los siguientes pasos:", null);
                    VerifyEmbed.addField("Paso 1:", "Entre al servidor con la siguiente IP: "+ Last_life_III.getIp() +"", false);
                    VerifyEmbed.addField("Paso 2:", "Ejecute el siguiente comando para finalizar con la verificación: ||**/link " + token + "**||", false);
                    VerifyEmbed.addField("**Importante:**", "Recuerde **NO** enviar su comando a otras personas. Unas de las funciones de este comando es la whitelist, si no se ha verificado correctamente contacte con TheDracon_#4244 , Mario_55#6461 o Cargandoc_p#1300, gracias.", false);
                    VerifyEmbed.setColor(Color.YELLOW);
                    channel.sendMessageEmbeds(VerifyEmbed.build()).queue();

                });

                //puts token + username on map
                Bot.tokenMap.putIfAbsent(token, event.getUser().getId());
                Bot.mapToken.putIfAbsent(event.getUser().getId(), token);
                //replys to the command
                mess.editOriginal("Se ha enviado el MD con los siguientes pasos satisfactoriamente. **Deje de compartir pantalla/no comparta el contenido**.").queue();

            } else {

                //replys if the token was alredy sent
                mess.editOriginal("Ya se le envió un MD, reenviando...").queue();
                event.getUser().openPrivateChannel().queue((channel) ->
                {
                    EmbedBuilder VerifyEmbed = new EmbedBuilder();
                    VerifyEmbed.setTitle("Para seguir con la verificación sigua los siguientes pasos:", null);
                    VerifyEmbed.addField("Paso 1:", "Entre al servidor con la siguiente IP: "+ Last_life_III.getIp() +"", false);
                    VerifyEmbed.addField("Paso 2:", "Ejecute el siguiente comando para finalizar con la verificación: ||**/link " + Bot.mapToken.get(event.getUser().getId()) + "**||", false);
                    VerifyEmbed.addField("**Importante:**", "Recuerde **NO** enviar su comando a otras personas. Unas de las funciones de este comando es la whitelist, si no se ha verificado correctamente contacte con TheDracon_#4244 , Mario_55#6461 o Cargandoc_p#1300, gracias.", false);
                    VerifyEmbed.setColor(Color.YELLOW);
                    channel.sendMessageEmbeds(VerifyEmbed.build()).queue();

                });
            }

        }catch (Exception ignore){}
    }
}
