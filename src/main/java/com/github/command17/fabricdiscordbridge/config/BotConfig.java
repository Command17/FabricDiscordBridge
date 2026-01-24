package com.github.command17.fabricdiscordbridge.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.entry.ConfigEntry;
import de.maxhenkel.configbuilder.entry.EnumConfigEntry;
import net.dv8tion.jda.api.entities.Activity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class BotConfig {
    public final ConfigEntry<String> token;
    public final ConfigEntry<Boolean> autoStart;
    public final EnumConfigEntry<ActivityType> activityType;
    public final ConfigEntry<String> activity;
    public final ConfigEntry<Long> channelId;

    public BotConfig(ConfigBuilder builder) {
        this.token = builder.stringEntry("token", "",
                "Token of the Discord bot.");
        this.autoStart = builder.booleanEntry("autoStart", true,
                "Whether to automatically start the Discord bot on server start.");
        this.activityType = builder.enumEntry("activityType", ActivityType.PLAYING,
                "Type of activity the Discord bot should display. Allowed values: NONE, LISTENING, PLAYING, COMPETING, WATCHING, CUSTOM");
        this.activity = builder.stringEntry("activity", "Minecraft",
                "The activity the Discord bot should display.");
        this.channelId = builder.longEntry("channelId", 0L,
                "ID of the Discord channel the Discord bot should send its messages in.");
    }

    public enum ActivityType {
        NONE((s) -> null),
        LISTENING(Activity::listening),
        PLAYING(Activity::playing),
        COMPETING(Activity::competing),
        WATCHING(Activity::watching),
        CUSTOM(Activity::customStatus);

        private final Function<String, @Nullable Activity> activityFunction;

        ActivityType(Function<String, @Nullable Activity> activityFunction) {
            this.activityFunction = activityFunction;
        }

        @Nullable
        public Activity createActivity(@NonNull String activityName) {
            return this.activityFunction.apply(activityName);
        }
    }
}
