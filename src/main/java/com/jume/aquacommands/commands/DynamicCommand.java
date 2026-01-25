package com.jume.aquacommands.commands;

import com.jume.aquacommands.permissions.PermissionManager;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.entity.entities.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;

/**
 * A command that is dynamically created by the user
 * 
 * @author jume, Antigravity
 */
public class DynamicCommand extends AbstractPlayerCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicCommand.class);
    private final String response;
    private final String commandName;

    public DynamicCommand(String name, String response) {
        super(name, "Custom command: /" + name);
        this.commandName = name;
        this.response = response;
    }

    // Try to disable built-in permission checks
    public boolean requiresPermission() {
        return false;
    }

    public String getPermission() {
        return null;
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        LOGGER.info("DEBUG: Executing custom command /{} for {}", commandName, playerRef.toString());

        Player player = store.getComponent(ref, Player.getComponentType());

        // Check permission using native Player entity
        // Note: For customs, PermissionManager defaults to TRUE if unconfigured in
        // Hytale,
        // or uses LuckPerms logic if installed.
        if (!PermissionManager.getInstance().hasCommandPermission(player, commandName)) {
            LOGGER.warn("DEBUG: Permission denied internal check for /{}", commandName);
            playerRef.sendMessage(Message.raw("You don't have permission to use this command!"));
            return;
        }

        LOGGER.info("DEBUG: Internal permission check passed for /{}", commandName);

        // Send the pre-configured response to the player
        if (isURL(response)) {
            playerRef.sendMessage(com.jume.aquacommands.utils.AquaColors.url(response));
        } else {
            playerRef.sendMessage(Message.raw(response));
        }
    }

    private boolean isURL(String text) {
        if (text == null)
            return false;
        String lower = text.toLowerCase().trim();
        return lower.startsWith("http://") || lower.startsWith("https://") ||
                lower.startsWith("www.") || lower.contains("://");
    }
}
