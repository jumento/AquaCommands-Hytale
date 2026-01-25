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
 * List command - shows all available custom commands in a UI
 * Usage: /aqualist
 * Permission: aquacommands.manage
 */
public class ListCommandsCommand extends AbstractPlayerCommand {

    public ListCommandsCommand() {
        super("aqualist", "List all custom commands");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Check permission
        if (!PermissionManager.getInstance().hasManagePermission(playerRef)) {
            playerRef.sendMessage(Message.raw("You don't have permission to list commands!"));
            return;
        }

        var commandManager = AquaCommands.getInstance().getCommandManager();
        var commands = commandManager.getAllCommands();

        if (commands.isEmpty()) {
            playerRef.sendMessage(Message.raw("No custom commands configured."));
        } else {
            playerRef.sendMessage(Message.raw("=== Custom Commands ==="));
            for (var entry : commands.entrySet()) {
                String response = entry.getValue();

                // Check if response contains a URL and make it clickeable
                if (isURL(response)) {
                    // Make the whole line clickable and styled
                    String styledText = "/" + entry.getKey() + " -> "
                            + com.jume.aquacommands.utils.ColorUtil.styleUrl(response);
                    playerRef.sendMessage(
                            Message.raw(styledText).link(response));
                } else {
                    playerRef.sendMessage(Message.raw("/" + entry.getKey() + " -> " + response));
                }
            }
            playerRef.sendMessage(Message.raw("======================="));
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
