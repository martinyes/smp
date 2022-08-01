package com.github.martinyes.smp.core;

import club.minnced.discord.webhook.WebhookClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Stores all the webhook types and their clients.
 *
 * @author martin
 */
@AllArgsConstructor
public enum Webhook {

    JOIN(new WebhookClient[]{
            WebhookClient.withUrl(""),
            WebhookClient.withUrl("")
    }),
    LEAVE(new WebhookClient[]{
            WebhookClient.withUrl(""),
            WebhookClient.withUrl("")
    }),
    CHAT(new WebhookClient[]{
            WebhookClient.withUrl("")
    }),
    ACHIEVEMENT(new WebhookClient[]{
            WebhookClient.withUrl(""),
            WebhookClient.withUrl("")
    }),
    DEATH(new WebhookClient[]{
            WebhookClient.withUrl(""),
            WebhookClient.withUrl("")
    });

    @Getter private final WebhookClient[] clients;
}