package com.github.command17.fabricdiscordbridge.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;

public class ModConfig {
    public final ConfigEntry<Boolean> enabled;
    public final ConfigEntry<Boolean> serverStartMsgEnabled;
    public final ConfigEntry<Boolean> serverStopMsgEnabled;
    public final ConfigEntry<Boolean> playerJoinMsgEnabled;
    public final ConfigEntry<Boolean> playerLeaveMsgEnabled;
    public final ConfigEntry<Boolean> playerAwardAdvancementMsgEnabled;
    public final ConfigEntry<Boolean> playerDiedMsgEnabled;

    public final ConfigEntry<Boolean> m2dEnabled;
    public final ConfigEntry<Boolean> m2dSendCommandOnly;
    public final ConfigEntry<String> m2dCommandMsg;

    public final ConfigEntry<Boolean> d2mEnabled;
    public final ConfigEntry<Integer> d2mMaxMsgLength;
    public final ConfigEntry<String> d2mMsg;

    public ModConfig(ConfigBuilder builder) {
        this.enabled = builder.booleanEntry("enabled", true,
                "Enables the Discord bot.");
        this.serverStartMsgEnabled = builder.booleanEntry("discord.serverStartMsgEnabled", true,
                "Enables the server start message.");
        this.serverStopMsgEnabled = builder.booleanEntry("discord.serverStopMsgEnabled", true,
                "Enables the server stop message.");
        this.playerJoinMsgEnabled = builder.booleanEntry("discord.playerJoinMsgEnabled", true,
                "Enables the player join message.");
        this.playerLeaveMsgEnabled = builder.booleanEntry("discord.playerLeaveMsgEnabled", true,
                "Enables the player leave message.");
        this.playerAwardAdvancementMsgEnabled = builder.booleanEntry("discord.playerAwardAdvancementMsgEnabled", true,
                "Enables the player award advancement message.");
        this.playerDiedMsgEnabled = builder.booleanEntry("discord.playerDiedMsgEnabled", true,
                "Enables the player died message.");

        this.m2dEnabled = builder.booleanEntry("m2d.enabled", true,
                "Enables the Minecraft to Discord chat bridge.");
        this.m2dSendCommandOnly = builder.booleanEntry("m2d.sendCommandOnly", false,
                "If true, messages from chat will only be sent to Discord via a command.");
        this.m2dCommandMsg = builder.stringEntry("m2d.commandMsg", "§7[{player} -> DISCORD]: {message}",
                "Message to display in Minecraft when sending a message via command to Discord.\nSupported placeholders: player, playerDisplay, playerUuid, message");

        this.d2mEnabled = builder.booleanEntry("d2m.enabled", true,
                "Enables the Discord to Minecraft chat bridge.");
        this.d2mMaxMsgLength = builder.integerEntry("d2m.maxMsgLength", 256, 0, 4000,
                "Maximum Discord message length to be displayed in Minecraft.");
        this.d2mMsg = builder.stringEntry("d2m.msg", "§9[DISCORD] <{discordUserDisplay}>§r {message}",
                "Message to display in Minecraft when sending a message via Discord.\nSupported placeholders: discordUser, discordUserDisplay, message");
    }
}
