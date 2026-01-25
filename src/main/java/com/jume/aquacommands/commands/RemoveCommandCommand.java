package com.jume.aquacommands.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.Message;
import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.config.CommandManager;
import com.jume.aquacommands.permissions.PermissionManager;
import com.jume.aquacommands.ui.CommandRemovePage;

import javax.annotation.Nonnull;

public class RemoveCommandCommand extends AbstractPlayerCommand {

    private final CommandManager commandManager;

    public RemoveCommandCommand(@Nonnull CommandManager commandManager) {
        super("aquacmdremove", "Remove a custom command via UI");
        this.commandManager = commandManager;
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        // Permission check using PlayerRef correctly
        if (!PermissionManager.getInstance().hasPermission(playerRef, "aquacommands.admin")) {
            return; // PermissionManager usually sends failure message, or we can send one if needed
        }

        Player player = store.getComponent(ref, Player.getComponentType());

        // Open remove UI
        CommandRemovePage page = new CommandRemovePage(playerRef, commandManager);
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
