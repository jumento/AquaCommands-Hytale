package com.jume.aquacommands.utils;

public class ColorUtil {
    public static final String BLUE = "\u00A79";
    public static final String UNDERLINE = "\u00A7n";
    public static final String RESET = "\u00A7r";
    public static final String GRAY = "\u00A77";
    public static final String WHITE = "\u00A7f";

    /**
     * Styles a URL to be blue and underlined
     */
    public static String styleUrl(String url) {
        return BLUE + UNDERLINE + url + RESET;
    }

    /**
     * Translates & color codes to section symbol codes
     */
    public static String colorize(String text) {
        if (text == null)
            return null;
        return text.replace("&", "\u00A7");
    }
}
