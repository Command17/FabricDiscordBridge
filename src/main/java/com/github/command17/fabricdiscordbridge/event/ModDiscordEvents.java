package com.github.command17.fabricdiscordbridge.event;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.util.ChatFormatSanitizer;
import com.github.command17.fabricdiscordbridge.util.StringPlaceholder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.minecraft.network.chat.Component;

import java.util.Map;

public final class ModDiscordEvents extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        if (!FabricDiscordBridge.CONFIG.d2mEnabled.get()
                || event.getChannel().getIdLong() != FabricDiscordBridge.BOT_CONFIG.channelId.get()
                || user.isBot()) {
            return;
        }

        String markdownSanitized = MarkdownSanitizer.sanitize(message.getContentDisplay());
        String chatSanitized = ChatFormatSanitizer.sanitize(markdownSanitized);
        StringBuilder messageBuilder = new StringBuilder(chatSanitized);
        if (message.isVoiceMessage()) {
            messageBuilder.append(" [Voice Message]");
        } else if (!message.getAttachments().isEmpty()) {
            messageBuilder.append(" [Attachment]");
        }

        int contentLength = messageBuilder.length();
        boolean isTooBig = contentLength > FabricDiscordBridge.CONFIG.d2mMaxMsgLength.get();
        if (isTooBig) {
            messageBuilder = new StringBuilder();
            messageBuilder.append("[Message too long to be displayed]");
            message.reply("Your message is too long to be displayed in Minecraft!").queue();
        }

        final String finalMsg = messageBuilder.toString().trim();

        // Don't send blank messages! It looks ugly
        if (finalMsg.isBlank()) {
            return;
        }

        FabricDiscordBridge.getMinecraftServer().ifPresent((server) -> {
            StringPlaceholder discordUserPlaceholder = StringPlaceholder.discordUser(user.getName());
            StringPlaceholder discordDisplayUserPlaceholder = StringPlaceholder.discordUserDisplayName(user.getEffectiveName());
            StringPlaceholder messagePlaceholder = StringPlaceholder.message(finalMsg);
            Map<String, String> placeholders = StringPlaceholder.combine(discordUserPlaceholder, discordDisplayUserPlaceholder, messagePlaceholder);
            String replacedMsg = StringPlaceholder.replace(FabricDiscordBridge.CONFIG.d2mMsg.get(), placeholders);
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal(replacedMsg),
                    false
            );
        });
    }
}
