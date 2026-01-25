package com.jume.aquacommands.commands;

import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.permissions.PermissionManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.Message;
import javax.annotation.Nonnull;

/**
 * Command to reload custom commands from config
 */
public class ReloadCommandsCommand extends AbstractPlayerCommand {

    public ReloadCommandsCommand() {
        super("aquareload", "Reload custom commands configuration");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        Player player = store.getComponent(ref, Player.getComponentType());

        if (!PermissionManager.getInstance().hasReloadPermission(player)) {
            playerRef.sendMessage(Message.raw("You don't have permission to reload commands!"));
            return;
        }

        AquaCommands.getInstance().reloadCommands();
        playerRef.sendMessage(Message.raw("Commands reloaded successfully!"));
    }
}
