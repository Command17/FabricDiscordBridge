package com.github.command17.fabricdiscordbridge.command;

import com.github.command17.fabricdiscordbridge.DiscordBot;
import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.config.message.MessageConfigs;
import com.github.command17.fabricdiscordbridge.util.StringPlaceholder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.Map;

public class DiscordBridgeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("discordbridge")
                        .then(
                                Commands.literal("bot")
                                        .requires((source) -> source.permissions().hasPermission(Permissions.COMMANDS_OWNER))
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
        if (!FabricDiscordBridge.CONFIG.m2dEnabled.get() || bot == null || player == null) {
            source.sendFailure(Component.literal("Discord bot is not running or this feature is not enabled!"));
            return 1;
        }

        StringPlaceholder messagePlaceholder = StringPlaceholder.message(sanitizedPlayerMsg);
        Map<String, String> placeholders = StringPlaceholder.combineWithPlayerPlaceholders(player, messagePlaceholder);
        String replacedFeedbackMsg = StringPlaceholder.replace(
                FabricDiscordBridge.CONFIG.m2dCommandMsg.get(),
                placeholders
        );

        source.getServer().getPlayerList().broadcastSystemMessage(
                Component.literal(replacedFeedbackMsg),
                false
        );

        MessageConfigs.PLAYER_CHAT.send(bot, placeholders);
        return 0;
    }
}
