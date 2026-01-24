package com.github.command17.fabricdiscordbridge.config.message;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import de.maxhenkel.configbuilder.ConfigBuilder;
import org.jspecify.annotations.Nullable;

public final class MessageConfigs {
    public static final MessageConfig PLAYER_JOIN = createConfig(
            "player_join",
            MessageConfig.MessageType.EMBED,
            16777045,
            null,
            "**{player} has joined the game!**",
            "Sent when a player joins the game.\nSupported placeholders: player, playerDisplay, playerUuid"
    );

    public static final MessageConfig PLAYER_LEAVE = createConfig(
            "player_leave",
            MessageConfig.MessageType.EMBED,
            16777045,
            null,
            "**{player} has left the game!**",
            "Sent when a player leaves the game.\nSupported placeholders: player, playerDisplay, playerUuid"
    );

    public static final MessageConfig PLAYER_CHAT = createConfig(
            "player_chat",
            MessageConfig.MessageType.TEXT,
            -1,
            null,
            "**{player}:** {message}",
            "Sent when a player sends a chat message.\nSupported placeholders: player, playerDisplay, playerUuid, message"
    );

    public static final MessageConfig PLAYER_AWARD_ADVANCEMENT = createConfig(
            "player_award_advancement",
            MessageConfig.MessageType.EMBED,
            5635925,
            null,
            "**{player} completed {advancementTitle}!**",
            "Sent when a player receives an advancement.\nSupported placeholders: player, playerDisplay, playerUuid, advancementTitle, advancementDesc, advancementType, advancementDisplay"
    );

    public static final MessageConfig PLAYER_DEATH = createConfig(
            "player_death",
            MessageConfig.MessageType.EMBED,
            16733525,
            null,
            "**{message}!**",
            "Sent when a player dies.\nSupported placeholders: player, playerDisplay, playerUuid, message"
    );

    public static final MessageConfig SERVER_START = createConfig(
            "server_start",
            MessageConfig.MessageType.EMBED,
            -1,
            null,
            "**Server started!**",
            "Sent when the server is starting."
    );

    public static final MessageConfig SERVER_STOP = createConfig(
            "server_stop",
            MessageConfig.MessageType.EMBED,
            -1,
            null,
            "**Server stopped!**",
            "Sent when the server is stopping."
    );

    private static MessageConfig createConfig(String configName, MessageConfig.MessageType messageType, int embedColor, @Nullable String embedTitle, String message, @Nullable String comment) {
        return ConfigBuilder.builder((builder) -> new MessageConfig(
                messageType,
                embedColor,
                embedTitle,
                message,
                comment,
                builder
        ))
                .saveAfterBuild(true)
                .keepOrder(true)
                .path(FabricDiscordBridge.MESSAGE_CONFIG_FOLDER.resolve(configName + ".properties"))
                .build();
    }

    public static void init() {}
}
