package com.github.command17.fabricdiscordbridge.config;

import com.github.command17.fabricdiscordbridge.config.entry.*;
import net.dv8tion.jda.api.entities.Activity;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModConfig {
    private final ConfigBuilder builder = new ConfigBuilder("fabricdiscordbridge");

    public final BoolConfigEntry enabled = builder.defineBool("enabled", true);

    public final EnumConfigEntry<BotActivityType> activityType = builder.defineEnum("discord.activityType", BotActivityType.NONE);
    public final StringConfigEntry activityName = builder.defineString("discord.activityName");

    public final LongConfigEntry channelId = builder.defineLong("discord.channelId", 0);

    public final BoolConfigEntry discordPlayerJoinMsgEnabled = builder.defineBool("discord.playerJoinMsg.enabled", true);
    public final IntConfigEntry discordPlayerJoinMsgColor = builder.defineInt("discord.playerJoinMsg.colorInt", 16777045);
    public final StringConfigEntry discordPlayerJoinMsg = builder.defineString("discord.playerJoinMsg", "**%player% has joined the server!**");

    public final BoolConfigEntry discordPlayerLeaveMsgEnabled = builder.defineBool("discord.playerLeaveMsg.enabled", true);
    public final IntConfigEntry discordPlayerLeaveMsgColor = builder.defineInt("discord.playerLeaveMsg.colorInt", 16777045);
    public final StringConfigEntry discordPlayerLeaveMsg = builder.defineString("discord.playerLeaveMsg", "**%player% has left the server!**");

    public final BoolConfigEntry discordPlayerRewardedAdvancementMsgEnabled = builder.defineBool("discord.playerRewardedAdvancementMsg.enabled", true);
    public final IntConfigEntry discordPlayerRewardedAdvancementMsgColor = builder.defineInt("discord.playerRewardedAdvancementMsg.colorInt", 5635925);
    public final StringConfigEntry discordPlayerRewardedAdvancementMsg = builder.defineString("discord.playerRewardedAdvancementMsg", "**%player%** completed **%advancementTitle%**!");

    public final BoolConfigEntry discordPlayerDiedMsgEnabled = builder.defineBool("discord.playerDiedMsg.enabled", true);
    public final IntConfigEntry discordPlayerDiedMsgColor = builder.defineInt("discord.playerDiedMsg.colorInt", 16733525);
    public final StringConfigEntry discordPlayerDiedMsg = builder.defineString("discord.playerDiedMsg", "**%message%!**");

    public final BoolConfigEntry mcToDcEnabled = builder.defineBool("minecraftToDiscordChat.enabled", true);
    public final BoolConfigEntry mcToDcSendAsEmbed = builder.defineBool("minecraftToDiscordChat.sendAsEmbed", false);
    public final BoolConfigEntry mcToDcCommandOnly = builder.defineBool("minecraftToDiscordChat.sendWithCommandOnly", false);
    public final StringConfigEntry mcToDcCommandMsg = builder.defineString("minecraftToDiscordChat.commandMsg", "<color gray>[%player% -> DISCORD]: %message%</color>");
    public final StringConfigEntry mcToDcMsg = builder.defineString("minecraftToDiscordChat.msg", "**%player%:** %message%");

    public final BoolConfigEntry dcToMcEnabled = builder.defineBool("discordToMinecraftChat.enabled", true);
    public final IntConfigEntry dcToMcMaxMsgLength = builder.defineInt("discordToMinecraftChat.maxMsgLength", 256, 0, 4000);
    public final StringConfigEntry dcToMcMsg = builder.defineString("discordToMinecraftChat.msg", "<color aqua>[DISCORD]</color> <color blue><%displayUser%> %message%</color>");

    public ConfigDefinition build() {
        return builder.build();
    }

    public enum BotActivityType implements StringRepresentable {
        NONE("none", (s) -> null),
        LISTENING("listening", Activity::listening),
        PLAYING("playing", Activity::playing),
        COMPETING("competing", Activity::competing),
        WATCHING("watching", Activity::watching),
        CUSTOM("custom", Activity::customStatus);

        private final String name;
        private final Function<String, @Nullable Activity> activityFunction;

        BotActivityType(@NotNull String name, Function<String, @Nullable Activity> activityFunction) {
            this.name = name;
            this.activityFunction = activityFunction;
        }

        @Nullable
        public Activity createActivity(@NotNull String activityName) {
            return this.activityFunction.apply(activityName);
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
