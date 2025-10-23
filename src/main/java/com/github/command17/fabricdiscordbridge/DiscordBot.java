package com.github.command17.fabricdiscordbridge;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordBot {
    private final JDA jdaInstance;

    private long defaultChannelId = 0;

    public DiscordBot(String token, GatewayIntent intent, GatewayIntent... intents) {
        this.jdaInstance = JDABuilder.createLight(token, intent, intents).build();
        this.awaitReady();
    }

    public DiscordBot(String token) {
        this.jdaInstance = JDABuilder.createLight(token).build();
        this.awaitReady();
    }

    public static MessageEmbed createSimpleEmbed(String description) {
        return createSimpleEmbed(null, description);
    }

    public static MessageEmbed createSimpleEmbed(@Nullable String title, String description) {
        return new EmbedBuilder().setTitle(title).setDescription(description).build();
    }

    public static MessageEmbed createSimpleColoredEmbed(String description, int color) {
        return createSimpleColoredEmbed(null, description, color);
    }

    public static MessageEmbed createSimpleColoredEmbed(@Nullable String title, String description, int color) {
        return new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).build();
    }

    private void awaitReady() {
        try {
            this.jdaInstance.awaitReady();
        } catch (Exception e) {
            FabricDiscordBridge.LOGGER.error("An error occurred while starting Discord bot! Exception:", e);
        }
    }

    public void shutdown() {
        this.jdaInstance.shutdown();
    }

    public void setActivity(@Nullable Activity activity) {
        this.jdaInstance.getPresence().setActivity(activity);
    }

    public void setEventManager(IEventManager eventManager) {
        this.jdaInstance.setEventManager(eventManager);
    }

    public void addEventListeners(@NotNull Object... listeners) {
        this.jdaInstance.addEventListener(listeners);
    }

    public void removeEventListeners(@NotNull Object... listeners) {
        this.jdaInstance.removeEventListener(listeners);
    }

    public void sendEmbed(MessageEmbed embed) {
        this.sendEmbed(embed, this.defaultChannelId);
    }

    public void sendEmbed(MessageEmbed embed, long channelId) {
        TextChannel channel = this.jdaInstance.getTextChannelById(channelId);
        if (channel != null) {
            this.sendEmbed(embed, channel);
        } else {
            FabricDiscordBridge.LOGGER.warn("Ignoring request to send embed to channel '{}' because channel does not exist!", channelId);
        }
    }

    public void sendEmbed(MessageEmbed embed, TextChannel channel) {
        channel.sendMessageEmbeds(embed).queue();
    }

    public void sendComponent(Component msg) {
        this.sendMessage(msg.getString());
    }

    public void sendComponent(Component msg, long channelId) {
        this.sendMessage(msg.getString(), channelId);
    }

    public void sendComponent(Component msg, TextChannel channel) {
        this.sendMessage(msg.getString(), channel);
    }

    public void sendMessage(String msg) {
        this.sendMessage(msg, this.defaultChannelId);
    }

    public void sendMessage(String msg, long channelId) {
        TextChannel channel = this.jdaInstance.getTextChannelById(channelId);
        if (channel != null) {
            this.sendMessage(msg, channel);
        } else {
            FabricDiscordBridge.LOGGER.warn("Ignoring request to send message to channel '{}' because channel does not exist!", channelId);
        }
    }

    public void sendMessage(String msg, TextChannel channel) {
        channel.sendMessage(msg).queue();
    }

    public void setDefaultChannelId(long channelId) {
        this.defaultChannelId = channelId;
    }

    public long getDefaultChannelId() {
        return defaultChannelId;
    }
}
