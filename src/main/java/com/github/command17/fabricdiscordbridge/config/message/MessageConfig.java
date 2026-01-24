package com.github.command17.fabricdiscordbridge.config.message;

import com.github.command17.fabricdiscordbridge.DiscordBot;
import com.github.command17.fabricdiscordbridge.util.StringPlaceholder;
import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;
import de.maxhenkel.configbuilder.entry.EnumConfigEntry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class MessageConfig {
    public final EnumConfigEntry<MessageType> type;
    public final ConfigEntry<Integer> embedColor;
    public final ConfigEntry<String> embedTitle;
    public final ConfigEntry<String> message;

    public MessageConfig(MessageType messageType, int embedColor, @Nullable String embedTitle, String message, @Nullable String comment, ConfigBuilder builder) {
        if (comment != null) {
            builder.header(comment);
        }

        this.type = builder.enumEntry("type", messageType,
                "The type of message to be sent. It can either be an embed or a text message. (Allowed Values: EMBED, TEXT)");
        this.embedColor = builder.integerEntry("embed.color", embedColor,
                "Color of the embed in integer format. Set to any number smaller than 0 to use the default color.");
        this.embedTitle = builder.stringEntry("embed.title", embedTitle != null ? embedTitle : "",
                "Title of the embed. Set to nothing to not show the title. (Can support placeholders)");
        this.message = builder.stringEntry("message", message,
                "Message of the embed or text message. (Can support placeholders)");
    }

    public void send(DiscordBot bot, @Nullable Map<String, String> placeholders) {
        String msg = StringPlaceholder.replace(this.message.get(), placeholders);
        if (this.type.get() == MessageType.EMBED) {
            int color = this.embedColor.get();
            String titleMsg = StringPlaceholder.replace(this.embedTitle.get(), placeholders);
            MessageEmbed embed = DiscordBot.createSimpleColoredEmbed(titleMsg.isBlank() ? null : titleMsg, msg, color < 0 ? Role.DEFAULT_COLOR_RAW : color);
            bot.sendEmbed(embed);
        } else {
            bot.sendMessage(msg);
        }
    }

    public enum MessageType {
        EMBED,
        TEXT
    }
}
