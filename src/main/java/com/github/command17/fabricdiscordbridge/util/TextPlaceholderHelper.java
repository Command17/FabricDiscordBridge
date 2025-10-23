package com.github.command17.fabricdiscordbridge.util;

import eu.pb4.placeholders.api.parsers.TagLikeParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class TextPlaceholderHelper {
    private final HashMap<String, Component> placeholders = new HashMap<>();
    private final TagLikeParser parser;

    public TextPlaceholderHelper(TagLikeParser.Format format) {
        this.parser = TagLikeParser.placeholderText(format, this::getPlaceholderValue);
    }

    public static void setPlayerPlaceholderValues(TextPlaceholderHelper placeholderHelper, Player player) {
        placeholderHelper.setPlaceholderValue("player", player.getName());
        placeholderHelper.setPlaceholderValue("playerDisplay", player.getDisplayName());
        placeholderHelper.setPlaceholderValue("playerUuid", Component.literal(player.getStringUUID()));
    }

    public void setPlaceholderValue(String placeholder, Component value) {
        this.placeholders.put(placeholder, value);
    }

    @Nullable
    public Component getPlaceholderValue(String placeholder) {
        return this.placeholders.get(placeholder);
    }

    public void clearPlaceholderValues() {
        this.placeholders.clear();
    }

    public TagLikeParser getParser() {
        return parser;
    }
}
