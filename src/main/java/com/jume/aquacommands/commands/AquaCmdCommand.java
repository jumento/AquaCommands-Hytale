package com.jume.aquacommands.commands;

import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.ui.CommandEditorPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

/**
 * Main command for AquaCommands management
 * Opens the command editor UI where players can create, edit, and manage custom
 * commands
 */
public class AquaCmdCommand extends AbstractPlayerCommand {

    public AquaCmdCommand() {
        super("aquacmd", "Manage custom commands with an intuitive UI");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        // Open command editor UI
        CommandEditorPage editorPage = new CommandEditorPage(
                playerRef,
                AquaCommands.getInstance().getCommandManager());
        player.getPageManager().openCustomPage(ref, store, editorPage);
    }
}
