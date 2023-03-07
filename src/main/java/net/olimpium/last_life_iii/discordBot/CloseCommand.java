package net.olimpium.last_life_iii.discordBot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class CloseCommand {
    @SubscribeEvent
    public void onCloseCommand(SlashCommandInteractionEvent event){
        if(!event.getName().equals("mineban")) return;
    }
}
