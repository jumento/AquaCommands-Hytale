package com.jume.aquacommands.commands;

import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.ui.CommandListPage;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

import javax.annotation.Nonnull;

public class ListCommandsCommand extends AbstractPlayerCommand {
    private final AquaCommands mod;

    public ListCommandsCommand(AquaCommands mod) {
        super("aqualist", "List custom commands", false);
        this.mod = mod;
        this.requirePermission("aquacommands.manage");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        Player player = store.getComponent(ref, Player.getComponentType());
        player.getPageManager().openCustomPage(ref, store, new CommandListPage(playerRef, mod.getCommandManager()));
    }
}
