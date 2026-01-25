package com.jume.aquacommands.utils;

import java.awt.Color;
import com.hypixel.hytale.server.core.Message;

public class AquaColors {
    // Standard Minecraft Blue (&9)
    public static final Color BLUE = new Color(85, 85, 255);

    public static Message url(String url) {
        return Message.raw(url).link(url).color(BLUE);
    }
}
