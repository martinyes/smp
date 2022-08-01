package com.github.martinyes.smp.bot;

import com.github.martinyes.smp.bot.listeners.WhitelistListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

/**
 * Initializes the discord bot.
 *
 * @author martin
 */
public class BotApplication {

    public static final String VERSION = "1.0.7";

    @Getter private static BotApplication instance;
    @Getter private static JDA jda;

    /**
     * Initializes the discord bot and its listeners.
     *
     * @throws LoginException - if the provided token was wrong
     */
    public void init() throws LoginException {
        instance = this;

        jda = JDABuilder
                .createLight("", GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .setActivity(Activity.watching("SMP Version " + VERSION))
                .build();

        jda.addEventListener(new WhitelistListener());
    }

    /**
     * Shuts down the discord bot.
     */
    public void shutdown() {
        jda.shutdownNow();
    }

    /**
     * Checks whether the member has permission or not to do certain things.
     *
     * @param member - target discord member
     * @return - whether it has permission or not
     */
    public boolean hasPermission(Member member) {
        return member.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase("Executive") ||
                member.getId().equals("221300757096300544"));
    }
}
