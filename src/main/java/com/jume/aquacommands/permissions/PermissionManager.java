package com.jume.aquacommands.permissions;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Permission Manager delegating to Hytale Native Permissions.
 * Relies on LuckPerms (or other managers) intercepting the native checks.
 * 
 * @author jume, Antigravity
 */
public class PermissionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionManager.class);
    private static PermissionManager instance;

    private PermissionManager() {
    }

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    /**
     * Check permission using native Hytale Player entity.
     * This is the correct way to support LuckPerms interception.
     */
    public boolean hasPermission(@Nonnull Player player, @Nonnull String permission) {
        return player.hasPermission(permission);
    }

    // Compatibility wrappers
    public boolean hasPermission(@Nonnull PlayerRef playerRef, @Nonnull String permission) {
        LOGGER.warn("Permission check called with PlayerRef instead of Player Entity for: {}", permission);
        return false;
    }

    public boolean hasManagePermission(@Nonnull Player player) {
        return hasPermission(player, "aquacommands.manage");
    }

    public boolean hasReloadPermission(@Nonnull Player player) {
        return hasPermission(player, "aquacommands.reload");
    }

    public boolean hasCommandPermission(@Nonnull Player player, @Nonnull String commandName) {
        // Simplified node structure: aquacommands.<commandname>
        // Example: /test -> aquacommands.test
        String node = "aquacommands." + commandName.toLowerCase();

        // Strict check executed internally
        boolean allowed = hasPermission(player, node);

        if (!allowed) {
            // Log failure to assist admin configuration
            LOGGER.info("Access denied for command /{}. Required node: {}", commandName, node);
        }

        return allowed;
    }
}
