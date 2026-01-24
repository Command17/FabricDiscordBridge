package com.github.command17.fabricdiscordbridge.util;

import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class StringPlaceholder extends ImmutablePair<String, String> {
    public static final String PLAYER_NAME_KEY = "player";
    public static final String PLAYER_DISPLAY_NAME_KEY = "playerDisplay";
    public static final String PLAYER_UUID_KEY = "playerUuid";
    public static final String MESSAGE_KEY = "message";
    public static final String DISCORD_USER_KEY = "discordUser";
    public static final String DISCORD_USER_DISPLAY_NAME_KEY = "discordUserDisplay";

    public StringPlaceholder(String key, String value) {
        super(key, value);
    }

    // Utility methods

    public static StringPlaceholder discordUser(String value) {
        return new StringPlaceholder(DISCORD_USER_KEY, value);
    }

    public static StringPlaceholder discordUserDisplayName(String value) {
        return new StringPlaceholder(DISCORD_USER_DISPLAY_NAME_KEY, value);
    }

    public static StringPlaceholder message(String value) {
        return new StringPlaceholder(MESSAGE_KEY, value);
    }

    public static StringPlaceholder playerName(String value) {
        return new StringPlaceholder(PLAYER_NAME_KEY, value);
    }

    public static StringPlaceholder playerDisplayName(String value) {
        return new StringPlaceholder(PLAYER_DISPLAY_NAME_KEY, value);
    }

    public static StringPlaceholder playerUUID(String value) {
        return new StringPlaceholder(PLAYER_UUID_KEY, value);
    }

    // End of utility methods

    public static Map<String, String> combinePlayerPlaceholders(Player player) {
        return combineWithPlayerPlaceholders(player);
    }

    public static Map<String, String> combineWithPlayerPlaceholders(Player player, StringPlaceholder... placeholders) {
        return combineWithPlayerPlaceholders(player, List.of(placeholders));
    }

    public static Map<String, String> combineWithPlayerPlaceholders(Player player, List<StringPlaceholder> placeholders) {
        ArrayList<StringPlaceholder> list = new ArrayList<>(placeholders);
        list.add(StringPlaceholder.playerName(player.getName().getString()));
        list.add(StringPlaceholder.playerDisplayName(player.getDisplayName().getString()));
        list.add(StringPlaceholder.playerUUID(player.getUUID().toString()));
        return combine(list);
    }

    public static Map<String, String> combine(StringPlaceholder... placeholders) {
        return combine(List.of(placeholders));
    }

    public static Map<String, String> combine(List<StringPlaceholder> placeholders) {
        HashMap<String, String> map = new HashMap<>(Map.of());
        for (var entry: placeholders) {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }

    public static String replace(String input, Map<String, String> placeholders) {
        int length = input.length();
        int cursor = 0;
        StringBuilder builder = new StringBuilder(length);
        StringBuilder placeholder = new StringBuilder();
        boolean placeholderStart = false;
        while (cursor < length) {
            char peek = input.charAt(cursor);

            // Escape character
            if (peek == '\\') {
                int nextCursor = cursor + 1;
                if (nextCursor < length) {
                    char escapedChar = input.charAt(nextCursor);

                    // Use correct StringBuilder
                    StringBuilder neededBuilder = builder;
                    if (placeholderStart) {
                        neededBuilder = placeholder;
                    }

                    switch (escapedChar) {
                        // Special cases
                        case 'n' -> neededBuilder.append("\n");
                        case 'r' -> neededBuilder.append("\r");
                        case 'f' -> neededBuilder.append("\f");
                        case 't' -> neededBuilder.append("\t");

                        // Normal case
                        default -> neededBuilder.append(escapedChar);
                    }

                    // Don't let the escaped character be processed
                    cursor = nextCursor + 1;
                    continue;
                }
            }

            // Placeholders
            if (placeholderStart) {
                if (peek == '}') {
                    placeholderStart = false;
                    String placeholderKey = placeholder.toString();
                    String placeholderContent = placeholders.get(placeholderKey);
                    if (placeholderContent != null) {
                        // replace placeholder with content
                        builder.append(placeholderContent);
                    } else {
                        // Append unknown placeholder key
                        builder.append("{").append(placeholderKey).append("}");
                    }

                    placeholder.delete(0, placeholder.capacity() - 1);
                } else {
                    placeholder.append(peek);
                }
            } else {
                if (peek == '{') {
                    placeholderStart = true;
                } else {
                    builder.append(peek);
                }
            }

            cursor++;
        }

        return builder.toString();
    }
}
