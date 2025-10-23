package com.github.command17.fabricdiscordbridge;

import com.github.command17.fabricdiscordbridge.command.ModCommands;
import com.github.command17.fabricdiscordbridge.config.BotConfig;
import com.github.command17.fabricdiscordbridge.config.ConfigDefinition;
import com.github.command17.fabricdiscordbridge.config.ModConfig;
import com.github.command17.fabricdiscordbridge.event.ModDiscordEvents;
import com.github.command17.fabricdiscordbridge.event.ModEvents;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

public final class FabricDiscordBridge implements ModInitializer {
    public static final String MOD_ID = "fabricdiscordbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final BotConfig BOT_CONFIG = new BotConfig();
    public static final ConfigDefinition BOT_CONFIG_DEF;

    public static final ModConfig CONFIG = new ModConfig();
    public static final ConfigDefinition CONFIG_DEF;

    @Nullable
    private static DiscordBot discordBot;

    @Nullable
    private static MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        BOT_CONFIG_DEF.createOrLoad();
        CONFIG_DEF.createOrLoad();

        ModCommands.register();
        ModEvents.register();

        ServerLifecycleEvents.SERVER_STARTING.register(FabricDiscordBridge::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(FabricDiscordBridge::onServerStopping);

        LOGGER.info("Initialized.");
    }

    private static void onServerStarting(MinecraftServer server) {
        minecraftServer = server;
    }

    private static void onServerStopping(MinecraftServer server) {
        minecraftServer = null;
    }

    public static void shutdownDiscordBot() {
        if (discordBot != null) {
            discordBot.shutdown();
            discordBot = null;
        }
    }

    public static boolean createDiscordBot() {
        try {
            shutdownDiscordBot();
            if (!CONFIG.enabled.get()) {
                return false;
            }

            discordBot = new DiscordBot(BOT_CONFIG.token.get(), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES);
            initDiscordBot(discordBot);
            return true;
        } catch (Exception e) {
            LOGGER.error("An error occurred while initializing Discord bot! Exception:", e);
        }

        return false;
    }

    public static void initDiscordBot(DiscordBot bot) {
        bot.setActivity(CONFIG.activityType.get().createActivity(CONFIG.activityName.get()));
        bot.addEventListeners(new ModDiscordEvents());
        bot.setDefaultChannelId(CONFIG.channelId.get());
    }

    public static Optional<DiscordBot> getDiscordBot() {
        return Optional.ofNullable(discordBot);
    }

    public static Optional<MinecraftServer> getMinecraftServer() {
        return Optional.ofNullable(minecraftServer);
    }

    public static void withDiscordBot(Consumer<DiscordBot> consumer) {
        getDiscordBot().ifPresent(consumer);
    }

    static {
        BOT_CONFIG_DEF = BOT_CONFIG.build();
        CONFIG_DEF = CONFIG.build();
    }
}
