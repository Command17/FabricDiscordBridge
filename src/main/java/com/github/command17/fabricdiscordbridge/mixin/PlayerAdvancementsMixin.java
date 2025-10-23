package com.github.command17.fabricdiscordbridge.mixin;

import com.github.command17.fabricdiscordbridge.DiscordBot;
import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.util.TextPlaceholderHelper;
import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.parsers.TagLikeParser;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Unique
    private static final TextPlaceholderHelper placeholderHelper = new TextPlaceholderHelper(TagLikeParser.PLACEHOLDER);

    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"))
    public void fabricdiscordbridge$onRewarded(AdvancementHolder advancementHolder, String string, CallbackInfoReturnable<Boolean> cir) {
        DisplayInfo displayInfo = advancementHolder.value().display().orElse(null);
        if (displayInfo == null) {
            return;
        }

        if (displayInfo.shouldAnnounceChat()
                && this.player.level().getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)
                && FabricDiscordBridge.CONFIG.discordPlayerRewardedAdvancementMsgEnabled.get()) {
            FabricDiscordBridge.withDiscordBot((bot) -> {
                placeholderHelper.setPlaceholderValue("advancementTitle", displayInfo.getTitle());
                placeholderHelper.setPlaceholderValue("advancementDescription", displayInfo.getDescription());
                placeholderHelper.setPlaceholderValue("advancementType", displayInfo.getType().getDisplayName());
                TextPlaceholderHelper.setPlayerPlaceholderValues(placeholderHelper, player);
                Component msg = placeholderHelper.getParser().parseText(
                        FabricDiscordBridge.CONFIG.discordPlayerRewardedAdvancementMsg.get(),
                        ParserContext.of()
                );

                bot.sendEmbed(DiscordBot.createSimpleColoredEmbed(
                        msg.getString(),
                        FabricDiscordBridge.CONFIG.discordPlayerRewardedAdvancementMsgColor.get()
                ));

                placeholderHelper.clearPlaceholderValues();
            });
        }
    }
}
