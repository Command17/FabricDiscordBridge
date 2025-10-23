package com.github.command17.fabricdiscordbridge.config;

import com.github.command17.fabricdiscordbridge.config.entry.BoolConfigEntry;
import com.github.command17.fabricdiscordbridge.config.entry.StringConfigEntry;
import net.fabricmc.loader.api.FabricLoader;

public class BotConfig {
    private final ConfigBuilder builder = new ConfigBuilder("bot")
            .setParentPath(FabricLoader.getInstance().getGameDir());

    public final StringConfigEntry token = builder.defineString("token");
    public final BoolConfigEntry autoStart = builder.defineBool("autoStart", true);

    public ConfigDefinition build() {
        return builder.build();
    }
}
