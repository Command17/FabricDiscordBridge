package com.github.command17.fabricdiscordbridge.command;

import com.github.command17.fabricdiscordbridge.DiscordBot;
import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.util.TextPlaceholderHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.parsers.TagLikeParser;
import eu.pb4.placeholders.api.parsers.TagParser;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DiscordBridgeCommand {
    private static final TextPlaceholderHelper placeholderHelper = new TextPlaceholderHelper(TagLikeParser.PLACEHOLDER);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("discordbridge")
                        .then(
                                Commands.literal("config")
                                        .requires((source) -> source.hasPermission(4))
                                        .then(Commands.literal("reload").executes(DiscordBridgeCommand::reloadConfig))
                                        .then(Commands.literal("save").executes(DiscordBridgeCommand::saveConfig))
                                        .then(Commands.literal("reset").executes(DiscordBridgeCommand::resetConfig))
                        )
                        .then(
                                Commands.literal("bot")
                                        .requires((source) -> source.hasPermission(4))
                                        .then(Commands.literal("start").executes(DiscordBridgeCommand::startBot))
                                        .then(Commands.literal("stop").executes(DiscordBridgeCommand::stopBot))
                        )
                        .then(
                                Commands.literal("send")
                                        .then(
                                                Commands.argument("message", StringArgumentType.greedyString())
                                                .executes(DiscordBridgeCommand::sendMsg)
                                        )
                        )
        );
    }

    private static int resetConfig(CommandContext<CommandSourceStack> context) {
        FabricDiscordBridge.CONFIG_DEF.resetToDefault();
        context.getSource().sendSuccess(() -> Component.literal("Config reset, but is not saved yet! Type '/discordbridge config save' to save.").withStyle(ChatFormatting.GREEN), false);
        return 0;
    }

    private static int saveConfig(CommandContext<CommandSourceStack> context) {
        FabricDiscordBridge.CONFIG_DEF.save();
        context.getSource().sendSuccess(() -> Component.literal("Config saved!").withStyle(ChatFormatting.GREEN), false);
        return 0;
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        FabricDiscordBridge.CONFIG_DEF.load();
        context.getSource().sendSuccess(() -> Component.literal("Config reloaded!").withStyle(ChatFormatting.GREEN), false);
        return 0;
    }

    private static int stopBot(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (FabricDiscordBridge.getDiscordBot().isPresent()) {
            FabricDiscordBridge.shutdownDiscordBot();
            source.sendSuccess(() -> Component.literal("Discord bot stopped!").withStyle(ChatFormatting.GREEN), false);
            return 0;
        }

        source.sendFailure(Component.literal("Discord bot is not running."));
        return 1;
    }

    private static int startBot(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (FabricDiscordBridge.getDiscordBot().isPresent()) {
            source.sendSystemMessage(Component.literal("Discord bot is already running, restarting...").withStyle(ChatFormatting.GRAY));
        }

        if (!FabricDiscordBridge.createDiscordBot()) {
            source.sendFailure(Component.literal("An error occurred while starting Discord bot! Please check the logs for more information."));
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Discord bot started!").withStyle(ChatFormatting.GREEN), false);
        return 0;
    }

    private static int sendMsg(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayer();
        String sanitizedPlayerMsg = MarkdownSanitizer.sanitize(StringArgumentType.getString(context, "message"));
        DiscordBot bot = FabricDiscordBridge.getDiscordBot().orElse(null);
        if (!FabricDiscordBridge.CONFIG.mcToDcEnabled.get() || bot == null || player == null) {
            source.sendFailure(Component.literal("Discord bot is not running or this feature is not enabled!"));
            return 1;
        }

        placeholderHelper.setPlaceholderValue("message", Component.literal(sanitizedPlayerMsg));
        TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
        Component replacedFeedbackMsg = placeholderHelper.getParser().parseText(
                FabricDiscordBridge.CONFIG.mcToDcCommandMsg.get(),
                ParserContext.of()
        );

        source.getServer().getPlayerList().broadcastSystemMessage(
                TagParser.QUICK_TEXT.parseText(TextNode.convert(replacedFeedbackMsg), ParserContext.of()),
                false
        );

        Component msg = placeholderHelper.getParser().parseText(
                FabricDiscordBridge.CONFIG.mcToDcMsg.get(),
                ParserContext.of()
        );

        if (FabricDiscordBridge.CONFIG.mcToDcSendAsEmbed.get()) {
            bot.sendEmbed(DiscordBot.createSimpleEmbed(player.getName().getString(), msg.getString()));
        } else {
            bot.sendComponent(msg);
        }

        placeholderHelper.clearPlaceholderValues();
        return 0;
    }
}
