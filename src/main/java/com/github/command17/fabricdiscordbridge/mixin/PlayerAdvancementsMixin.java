package com.github.command17.fabricdiscordbridge.mixin;

import com.github.command17.fabricdiscordbridge.FabricDiscordBridge;
import com.github.command17.fabricdiscordbridge.config.message.MessageConfigs;
import com.github.command17.fabricdiscordbridge.util.StringPlaceholder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"))
    public void fabricdiscordbridge$onRewarded(AdvancementHolder advancementHolder, String string, CallbackInfoReturnable<Boolean> cir) {
        DisplayInfo displayInfo = advancementHolder.value().display().orElse(null);
        if (displayInfo == null) {
            return;
        }

        if (displayInfo.shouldAnnounceChat()
                && this.player.level().getGameRules().get(GameRules.SHOW_ADVANCEMENT_MESSAGES)
                && FabricDiscordBridge.CONFIG.playerAwardAdvancementMsgEnabled.get()) {
            FabricDiscordBridge.withDiscordBot((bot) -> {
                StringPlaceholder advancementTitlePlaceholder = new StringPlaceholder("advancementTitle", displayInfo.getTitle().getString());
                StringPlaceholder advancementDescPlaceholder = new StringPlaceholder("advancementDesc", displayInfo.getDescription().getString());
                StringPlaceholder advancementTypePlaceholder = new StringPlaceholder("advancementType", displayInfo.getType().getSerializedName());
                StringPlaceholder advancementTypeCapitalPlaceholder = new StringPlaceholder("advancementDisplay", displayInfo.getType().getDisplayName().getString());
                Map<String, String> placeholders = StringPlaceholder.combineWithPlayerPlaceholders(
                        player,
                        advancementTitlePlaceholder,
                        advancementDescPlaceholder,
                        advancementTypePlaceholder,
                        advancementTypeCapitalPlaceholder
                );

                MessageConfigs.PLAYER_AWARD_ADVANCEMENT.send(bot, placeholders);
            });
        }
    }
}
