package com.github.command17.fabricdiscordbridge.util;

import java.util.Set;

public final class ChatFormatSanitizer {
    public static final Set<Character> FORBIDDEN_CODES = Set.of(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f',
            'k', 'l', 'm', 'n', 'o', 'r'
    );

    private ChatFormatSanitizer() {}

    public static String sanitize(String input) {
        return sanitize(input, FORBIDDEN_CODES);
    }

    // Removes the '§' and code character from any string
    public static String sanitize(String input, Set<Character> forbiddenCodes) {
        int length = input.length();
        int cursor = 0;
        StringBuilder builder = new StringBuilder(length);
        while (cursor < length) {
            char peek = input.charAt(cursor);
            if (peek == '§') {
                cursor++;
                char nextChar = input.charAt(cursor);
                if (forbiddenCodes.contains(nextChar)) {
                    cursor++;
                }

                continue;
            }

            builder.append(peek);
            cursor++;
        }

        return builder.toString();
    }
}
