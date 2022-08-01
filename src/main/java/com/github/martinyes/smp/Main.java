package com.github.martinyes.smp;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookCluster;
import club.minnced.discord.webhook.send.AllowedMentions;
import com.github.martinyes.smp.bot.BotApplication;
import com.github.martinyes.smp.core.AchievementLoader;
import com.github.martinyes.smp.core.Webhook;
import com.github.martinyes.smp.core.listeners.WebhookListeners;
import com.github.martinyes.smp.core.utils.GeneralUtils;
import com.google.gson.Gson;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The main class is making sure that everything is started and registered.
 *
 * @author martin
 */
public class Main extends JavaPlugin {

    @Getter private static Main instance;
    @Getter private static final Gson gson = new Gson();

    private final WebhookCluster cluster = new WebhookCluster(10);
    private BotApplication botApp;

    /**
     * Fired once the server is loading.
     */
    @Override
    public void onEnable() {
        instance = this;

        try {
            botApp = new BotApplication();
            botApp.init();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        try {
            cluster.setDefaultHttpClient(new OkHttpClient());
            cluster.setAllowedMentions(AllowedMentions.all());
            cluster.setDefaultDaemon(true);

            AchievementLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        Arrays.stream(Webhook.values()).forEach(w -> Arrays.stream(w.getClients()).forEach(cluster::addWebhooks));

        Bukkit.getPluginManager().registerEvents(new WebhookListeners(), this);
    }

    /**
     * Fired when the server is shutting down.
     */
    @Override
    public void onDisable() {
        instance = null;

        botApp.shutdown();
    }

    /**
     * Whitelists the player provided by the discord bot.
     *
     * @param name - player username
     * @param ex   - a consumer for handling errors
     */
    public boolean whitelist(String name) {
        Optional<UUID> uuid = GeneralUtils.getPlayerUUID(name);

        if (!uuid.isPresent()) {
            System.out.println("UUID does not present.");
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid.get());
        player.setWhitelisted(true);
        return true;
    }

    /**
     * Sends a message to the appropriate webhook with the provided message.
     *
     * @param webhook - a webhook type
     * @param data    - the message to be send
     */
    public void log(Webhook webhook, String data) {
        CompletableFuture.runAsync(() -> {
            for (WebhookClient client : webhook.getClients()) {
                client.send(data);
            }
        });
    }
}