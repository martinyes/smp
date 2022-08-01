package com.github.martinyes.smp.bot.listeners;

import com.github.martinyes.smp.Main;
import com.github.martinyes.smp.bot.BotApplication;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WhitelistListener extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        TextChannel channel = event.getChannel();

        if (!channel.getName().equalsIgnoreCase("\uD83D\uDCDC｜smp-whitelist") ||
                !event.getReactionEmote().getAsCodepoints().equals("U+2705")) {
            System.out.println("Wrong channel or reaction emote.");
            return;
        }

        if (!BotApplication.getInstance().hasPermission(event.getMember())) {
            System.out.println(event.getMember().getNickname() + " does not have permission to auto whitelist people.");
            return;
        }

        channel.retrieveMessageById(event.getMessageId()).queue(message -> {
            if (!Main.getInstance().whitelist(message.getContentDisplay())) {
                channel.sendMessage("An error has occurred: **invalid player '" + message.getContentDisplay() + "'**. Please contact the developer.").queue();
            } else {
                channel.sendMessage("Whitelisted " + message.getContentDisplay() + ".").queue();
            }
        });
    }
}