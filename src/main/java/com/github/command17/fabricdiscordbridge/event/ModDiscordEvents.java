package com.github.command17.fabricdiscordbridge.event;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.util.TextPlaceholderHelper;
import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.parsers.TagLikeParser;
import eu.pb4.placeholders.api.parsers.TagParser;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.minecraft.network.chat.Component;

public final class ModDiscordEvents extends ListenerAdapter {
    private static final TextPlaceholderHelper placeholderHelper = new TextPlaceholderHelper(TagLikeParser.PLACEHOLDER);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        if (!FabricDiscordBridge.CONFIG.dcToMcEnabled.get()
                || event.getChannel().getIdLong() != FabricDiscordBridge.CONFIG.channelId.get()
                || user.isBot()) {
            return;
        }

        String msg = MarkdownSanitizer.sanitize(message.getContentDisplay());
        if (msg.isBlank()) {
            return;
        }

        int contentLength = msg.length();
        boolean isTooBig = contentLength > FabricDiscordBridge.CONFIG.dcToMcMaxMsgLength.get();
        if (isTooBig) {
            msg = "[Message too long to be displayed]";
            message.reply("Your message is too long to be displayed in Minecraft!").queue();
        } else if (message.isVoiceMessage()) {
            msg = "[Voice message]";
        }

        final String finalMsg = msg;
        FabricDiscordBridge.getMinecraftServer().ifPresent((server) -> {
            placeholderHelper.setPlaceholderValue("user", Component.literal(user.getName()));
            placeholderHelper.setPlaceholderValue("displayUser", Component.literal(user.getEffectiveName()));
            placeholderHelper.setPlaceholderValue("message", Component.literal(finalMsg));
            Component replacedMsg = placeholderHelper.getParser().parseText(FabricDiscordBridge.CONFIG.dcToMcMsg.get(), ParserContext.of());
            server.getPlayerList().broadcastSystemMessage(
                    TagParser.QUICK_TEXT.parseText(TextNode.convert(replacedMsg), ParserContext.of()),
                    false
            );
        });
    }
}
