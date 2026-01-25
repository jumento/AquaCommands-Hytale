package com.jume.aquacommands.commands;

import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.permissions.PermissionManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

/**
 * Reload command - reloads all custom commands from config
 * Usage: /aquareload
 * Permission: aquacommands.reload
 */
public class ReloadCommandsCommand extends AbstractPlayerCommand {

    public ReloadCommandsCommand() {
        super("aquareload", "Reload all custom commands from configuration");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Check permission
        if (!PermissionManager.getInstance().hasReloadPermission(playerRef)) {
            playerRef.sendMessage(Message.raw("You don't have permission to reload commands!"));
            return;
        }

        try {
            // Reload commands
            AquaCommands.getInstance().reloadCommands();

            int commandCount = AquaCommands.getInstance().getCommandManager().getAllCommands().size();
            playerRef.sendMessage(Message.raw("Successfully reloaded " + commandCount + " custom command(s)!"));

        } catch (Exception e) {
            playerRef.sendMessage(Message.raw("Error reloading commands: " + e.getMessage()));
        }
    }
}
