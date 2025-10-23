package com.github.command17.fabricdiscordbridge.event;

import com.github.command17.fabricdiscordbridge.DiscordBot;
import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.util.TextPlaceholderHelper;
import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.parsers.TagLikeParser;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class ModEvents {
    private static final TextPlaceholderHelper placeholderHelper = new TextPlaceholderHelper(TagLikeParser.PLACEHOLDER);

    private static void onServerStarting(MinecraftServer server) {
        if (!FabricDiscordBridge.BOT_CONFIG.autoStart.get()) {
            return;
        }

        FabricDiscordBridge.createDiscordBot();
    }

    private static void onServerStopped(MinecraftServer server) {
        FabricDiscordBridge.shutdownDiscordBot();
    }

    private static void onPlayerJoin(ServerPlayer player) {
        if (!FabricDiscordBridge.CONFIG.discordPlayerJoinMsgEnabled.get()) {
            return;
        }

        FabricDiscordBridge.withDiscordBot((bot) -> {
            TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
            Component msg = placeholderHelper.getParser().parseText(
                    FabricDiscordBridge.CONFIG.discordPlayerJoinMsg.get(),
                    ParserContext.of()
            );

            bot.sendEmbed(
                    DiscordBot.createSimpleColoredEmbed(
                            msg.getString(),
                            FabricDiscordBridge.CONFIG.discordPlayerJoinMsgColor.get()
                    )
            );

            placeholderHelper.clearPlaceholderValues();
        });
    }

    private static void onPlayerLeave(ServerPlayer player) {
        if (!FabricDiscordBridge.CONFIG.discordPlayerLeaveMsgEnabled.get()) {
            return;
        }

        FabricDiscordBridge.withDiscordBot((bot) -> {
            TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
            Component msg = placeholderHelper.getParser().parseText(
                    FabricDiscordBridge.CONFIG.discordPlayerLeaveMsg.get(),
                    ParserContext.of()
            );

            bot.sendEmbed(
                    DiscordBot.createSimpleColoredEmbed(
                            msg.getString(),
                            FabricDiscordBridge.CONFIG.discordPlayerLeaveMsgColor.get()
                    )
            );

            placeholderHelper.clearPlaceholderValues();
        });
    }

    private static void onChat(PlayerChatMessage chatMessage, ServerPlayer player, ChatType.Bound bound) {
        if (!FabricDiscordBridge.CONFIG.mcToDcEnabled.get() || FabricDiscordBridge.CONFIG.mcToDcCommandOnly.get()) {
            return;
        }

        FabricDiscordBridge.getDiscordBot().ifPresent((bot) -> {
            placeholderHelper.setPlaceholderValue("message", chatMessage.decoratedContent());
            TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
            Component msg = placeholderHelper.getParser().parseText(
                    FabricDiscordBridge.CONFIG.mcToDcMsg.get(),
                    ParserContext.of()
            );

            if (FabricDiscordBridge.CONFIG.mcToDcSendAsEmbed.get()) {
                bot.sendEmbed(DiscordBot.createSimpleEmbed(player.getPlainTextName(), msg.getString()));
            } else {
                bot.sendComponent(msg);
            }

            placeholderHelper.clearPlaceholderValues();
        });
    }

    private static void onLivingEntityDied(LivingEntity entity, DamageSource damageSource) {
        if (!FabricDiscordBridge.CONFIG.discordPlayerDiedMsgEnabled.get()) {
            return;
        }

        if (entity instanceof Player player) {
            FabricDiscordBridge.withDiscordBot((bot) -> {
                placeholderHelper.setPlaceholderValue("message", damageSource.getLocalizedDeathMessage(player));
                TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
                Component msg = placeholderHelper.getParser().parseText(
                        FabricDiscordBridge.CONFIG.discordPlayerDiedMsg.get(),
                        ParserContext.of()
                );

                bot.sendEmbed(DiscordBot.createSimpleColoredEmbed(
                        msg.getString(),
                        FabricDiscordBridge.CONFIG.discordPlayerDiedMsgColor.get()
                ));

                placeholderHelper.clearPlaceholderValues();
            });
        }
    }

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(ModEvents::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(ModEvents::onServerStopped);

        ServerPlayerEvents.JOIN.register(ModEvents::onPlayerJoin);
        ServerPlayerEvents.LEAVE.register(ModEvents::onPlayerLeave);
        ServerLivingEntityEvents.AFTER_DEATH.register(ModEvents::onLivingEntityDied);

        ServerMessageEvents.CHAT_MESSAGE.register(ModEvents::onChat);
    }
}
