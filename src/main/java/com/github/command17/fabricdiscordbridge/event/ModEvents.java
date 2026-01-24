package com.github.command17.fabricdiscordbridge.event;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.config.message.MessageConfigs;
import com.github.command17.fabricdiscordbridge.util.StringPlaceholder;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public final class ModEvents {
    private static void onServerStarting(MinecraftServer server) {
        if (!FabricDiscordBridge.BOT_CONFIG.autoStart.get()) {
            return;
        }

        FabricDiscordBridge.createDiscordBot();

        if (FabricDiscordBridge.CONFIG.serverStartMsgEnabled.get()) {
            FabricDiscordBridge.withDiscordBot((bot) -> MessageConfigs.SERVER_START.send(bot, null));
        }
    }

    private static void onServerStopped(MinecraftServer server) {
        if (FabricDiscordBridge.CONFIG.serverStopMsgEnabled.get()) {
            FabricDiscordBridge.withDiscordBot((bot) -> MessageConfigs.SERVER_STOP.send(bot, null));
        }

        FabricDiscordBridge.shutdownDiscordBot();
    }

    private static void onPlayerJoin(ServerPlayer player) {
        if (!FabricDiscordBridge.CONFIG.playerJoinMsgEnabled.get()) {
            return;
        }

        FabricDiscordBridge.withDiscordBot((bot) -> {
            Map<String, String> placeholders = StringPlaceholder.combinePlayerPlaceholders(player);
            MessageConfigs.PLAYER_JOIN.send(bot, placeholders);
        });
    }

    private static void onPlayerLeave(ServerPlayer player) {
        if (!FabricDiscordBridge.CONFIG.playerLeaveMsgEnabled.get()) {
            return;
        }

        FabricDiscordBridge.withDiscordBot((bot) -> {
            Map<String, String> placeholders = StringPlaceholder.combinePlayerPlaceholders(player);
            MessageConfigs.PLAYER_LEAVE.send(bot, placeholders);
        });
    }

    private static void onChat(PlayerChatMessage chatMessage, ServerPlayer player, ChatType.Bound bound) {
        if (!FabricDiscordBridge.CONFIG.m2dEnabled.get() || FabricDiscordBridge.CONFIG.m2dSendCommandOnly.get()) {
            return;
        }

        FabricDiscordBridge.getDiscordBot().ifPresent((bot) -> {
            String sanitizedChatMessage = MarkdownSanitizer.sanitize(chatMessage.decoratedContent().getString());
            StringPlaceholder messagePlaceholder = StringPlaceholder.message(sanitizedChatMessage);
            Map<String, String> placeholders = StringPlaceholder.combineWithPlayerPlaceholders(player, messagePlaceholder);
            MessageConfigs.PLAYER_CHAT.send(bot, placeholders);
        });
    }

    private static void onLivingEntityDied(LivingEntity entity, DamageSource damageSource) {
        if (!FabricDiscordBridge.CONFIG.playerDiedMsgEnabled.get()) {
            return;
        }

        if (entity instanceof Player player) {
            FabricDiscordBridge.withDiscordBot((bot) -> {
                StringPlaceholder messagePlaceholder = StringPlaceholder.message(damageSource.getLocalizedDeathMessage(player).getString());
                Map<String, String> placeholders = StringPlaceholder.combineWithPlayerPlaceholders(player, messagePlaceholder);
                MessageConfigs.PLAYER_DEATH.send(bot, placeholders);
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
