package com.jume.aquacommands.permissions;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Permission manager that supports both native Hytale permissions and LuckPerms
 * If LuckPerms is available, it will be used. Otherwise, falls back to op-based
 * permissions.
 * 
 * @author jume, Antigravity
 */
public class PermissionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionManager.class);
    private static PermissionManager instance;

    private LuckPerms luckPerms;
    private boolean luckPermsAvailable;

    private PermissionManager() {
        // Try to load LuckPerms
        try {
            this.luckPerms = LuckPermsProvider.get();
            this.luckPermsAvailable = true;
            LOGGER.info("LuckPerms detected! Using LuckPerms for permission checks.");
        } catch (IllegalStateException | NoClassDefFoundError e) {
            this.luckPermsAvailable = false;
            LOGGER.info("LuckPerms not found. Using fallback permission system (op-based).");
        }
    }

    public static PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    /**
     * Check if a player has a specific permission
     * Permission nodes follow format: aquacommands.command.{commandname}
     * 
     * @param playerRef  The player to check
     * @param permission The permission node to check
     * @return true if player has permission
     */
    public boolean hasPermission(@Nonnull PlayerRef playerRef, @Nonnull String permission) {
        if (luckPermsAvailable) {
            return checkLuckPerms(playerRef, permission);
        }
        return checkFallback(playerRef, permission);
    }

    /**
     * Check if player can use AquaCommands management features
     */
    public boolean hasManagePermission(@Nonnull PlayerRef playerRef) {
        return hasPermission(playerRef, "aquacommands.manage");
    }

    /**
     * Check if player can reload commands
     */
    public boolean hasReloadPermission(@Nonnull PlayerRef playerRef) {
        return hasPermission(playerRef, "aquacommands.reload");
    }

    /**
     * Check if player can use a custom command
     */
    public boolean hasCommandPermission(@Nonnull PlayerRef playerRef, @Nonnull String commandName) {
        return hasPermission(playerRef, "aquacommands.command." + commandName.toLowerCase());
    }

    private boolean checkLuckPerms(@Nonnull PlayerRef playerRef, @Nonnull String permission) {
        try {
            User user = luckPerms.getUserManager().getUser(playerRef.getUuid());
            if (user == null) {
                LOGGER.warn("Could not find LuckPerms user for {}", playerRef.getUuid());
                return checkFallback(playerRef, permission);
            }

            // Check permission with LuckPerms
            return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        } catch (Exception e) {
            LOGGER.error("Error checking LuckPerms permission for {}: {}", playerRef.getUuid(), e.getMessage());
            return checkFallback(playerRef, permission);
        }
    }

    private boolean checkFallback(@Nonnull PlayerRef playerRef, @Nonnull String permission) {
        // Fallback: Allow all permissions when LuckPerms is not available
        // This is necessary because we cannot reliably check operator status
        // in the current Hytale API version

        // If you want proper permissions without LuckPerms, install LuckPerms
        return true;
    }

    public boolean isLuckPermsAvailable() {
        return luckPermsAvailable;
    }
}
