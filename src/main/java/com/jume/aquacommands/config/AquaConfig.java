package com.jume.aquacommands.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the configuration structure with versioning support.
 * This ensures future updates can add settings without breaking existing user
 * data.
 */
public class AquaConfig {
    public static final int CURRENT_VERSION = 1;

    // Config version for migration checks
    public int version = CURRENT_VERSION;

    // The actual commands storage
    public Map<String, String> commands = new HashMap<>();

    // Future settings can be added here, e.g.:
    // public boolean enableLogging = true;
    // public String prefix = "&9[AquaCmd]&r ";
}
