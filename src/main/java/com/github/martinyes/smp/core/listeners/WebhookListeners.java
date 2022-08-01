package com.github.martinyes.smp.core.listeners;

import com.github.martinyes.smp.Main;
import com.github.martinyes.smp.core.AchievementLoader;
import com.github.martinyes.smp.core.Webhook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class WebhookListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String data = "**%s** has joined the server (%d/%d).";
        Main.getInstance().log(Webhook.JOIN, String.format(data, player.getName(), Bukkit.getOnlinePlayers().size(),
                Bukkit.getMaxPlayers()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String data = "**%s** has left the server (%d/%d).";
        Main.getInstance().log(Webhook.LEAVE, String.format(data, player.getName(), Bukkit.getOnlinePlayers().size() - 1,
                Bukkit.getMaxPlayers()));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();

        String data = "**%s**: %s";
        Main.getInstance().log(Webhook.CHAT, String.format(data, player.getName(), msg));
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        String data = "**%s** has completed an achievement _(%s)_.";

        Optional<String> name = AchievementLoader.getName(event.getAdvancement());
        name.ifPresent(s -> Main.getInstance().log(Webhook.ACHIEVEMENT, String.format(data, player.getName(),
                s)));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String data = "**%s** killed **%s**.";

        Player entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer != null) {
            Main.getInstance().log(Webhook.DEATH, String.format(data, killer.getName(), entity.getName()));
            return;
        }

        Main.getInstance().log(Webhook.DEATH, event.getDeathMessage());
    }
}