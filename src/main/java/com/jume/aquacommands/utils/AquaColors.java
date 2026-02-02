package com.jume.aquacommands.utils;

import com.hypixel.hytale.server.core.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AquaColors {
    private static final Map<Character, String> COLOR_MAP = new HashMap<>();
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[\\w-]+(\\.[\\w-]+)+(/[\\w-./?%&=]*)?)");
    private static final String VIOLET_HEX = "#EE82EE";

    static {
        COLOR_MAP.put('0', "#000000"); // Black
        COLOR_MAP.put('1', "#0000AA"); // Dark Blue
        COLOR_MAP.put('2', "#00AA00"); // Dark Green
        COLOR_MAP.put('3', "#00AAAA"); // Dark Aqua
        COLOR_MAP.put('4', "#AA0000"); // Dark Red
        COLOR_MAP.put('5', "#AA00AA"); // Dark Purple
        COLOR_MAP.put('6', "#FFAA00"); // Gold
        COLOR_MAP.put('7', "#AAAAAA"); // Gray
        COLOR_MAP.put('8', "#555555"); // Dark Gray
        COLOR_MAP.put('9', "#5555FF"); // Blue
        COLOR_MAP.put('a', "#55FF55"); // Green
        COLOR_MAP.put('b', "#55FFFF"); // Aqua
        COLOR_MAP.put('c', "#FF5555"); // Red
        COLOR_MAP.put('d', "#FF55FF"); // Light Purple
        COLOR_MAP.put('e', "#FFFF55"); // Yellow
        COLOR_MAP.put('f', "#FFFFFF"); // White
    }

    /**
     * Translates a string with Minecraft-style color codes (&a, &b, etc.)
     * into a Hytale Message object with proper colors and clickable links.
     */
    public static Message translate(String text) {
        Message message = Message.empty();
        if (text == null || text.isEmpty())
            return message;

        // Split by color code, keeping the code at the start of each part
        String[] parts = text.split("(?=&[0-9a-fA-F])");
        String activeColor = null;

        for (String part : parts) {
            String content = part;
            if (part.startsWith("&") && part.length() >= 2) {
                char code = Character.toLowerCase(part.charAt(1));
                String newColor = COLOR_MAP.get(code);
                if (newColor != null) {
                    activeColor = newColor;
                    content = part.substring(2);
                }
            }

            if (!content.isEmpty()) {
                insertWithLinks(message, content, activeColor);
            }
        }
        return message;
    }

    private static void insertWithLinks(Message message, String text, String activeColor) {
        Matcher matcher = URL_PATTERN.matcher(text);
        int lastEnd = 0;

        while (matcher.find()) {
            // Text before URL
            String before = text.substring(lastEnd, matcher.start());
            if (!before.isEmpty()) {
                if (activeColor != null) {
                    message.insert(before).color(activeColor);
                } else {
                    message.insert(before);
                }
            }

            // URL
            String url = matcher.group();
            message.insert(url).link(url).color(VIOLET_HEX);

            lastEnd = matcher.end();
        }

        // Remaining text
        String remaining = text.substring(lastEnd);
        if (!remaining.isEmpty()) {
            if (activeColor != null) {
                message.insert(remaining).color(activeColor);
            } else {
                message.insert(remaining);
            }
        }
    }

    public static String strip(String text) {
        if (text == null)
            return "";
        return text.replaceAll("(?i)&[0-9a-f]", "");
    }
}
