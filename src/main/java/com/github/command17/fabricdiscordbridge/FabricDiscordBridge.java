package com.github.command17.fabricdiscordbridge;

import com.github.command17.fabricdiscordbridge.command.ModCommands;
import com.github.command17.fabricdiscordbridge.config.BotConfig;
import com.github.command17.fabricdiscordbridge.config.ModConfig;
import com.github.command17.fabricdiscordbridge.config.message.MessageConfigs;
import com.github.command17.fabricdiscordbridge.event.ModDiscordEvents;
import com.github.command17.fabricdiscordbridge.event.ModEvents;
import de.maxhenkel.configbuilder.ConfigBuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

public final class FabricDiscordBridge implements ModInitializer {
    public static final String MOD_ID = "fabricdiscordbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("fabricDiscordBridge").toAbsolutePath();
    public static final Path MESSAGE_CONFIG_FOLDER = CONFIG_FOLDER.resolve("message");

    public static final BotConfig BOT_CONFIG = ConfigBuilder.builder(BotConfig::new)
            .path(CONFIG_FOLDER.resolve("bot.properties"))
            .keepOrder(true)
            .saveAfterBuild(true)
            .build();

    public static final ModConfig CONFIG = ConfigBuilder.builder(ModConfig::new)
            .path(CONFIG_FOLDER.resolve("config.properties"))
            .keepOrder(true)
            .saveAfterBuild(true)
            .build();

    @Nullable
    private static DiscordBot discordBot;

    @Nullable
    private static MinecraftServer minecraftServer;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");

        MessageConfigs.init();
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

    private static void initDiscordBot(DiscordBot bot) {
        bot.setActivity(BOT_CONFIG.activityType.get().createActivity(BOT_CONFIG.activity.get()));
        bot.addEventListeners(new ModDiscordEvents());
        bot.setDefaultChannelId(BOT_CONFIG.channelId.get());
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
}
