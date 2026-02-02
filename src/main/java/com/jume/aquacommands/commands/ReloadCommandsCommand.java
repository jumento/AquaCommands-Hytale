package com.jume.aquacommands.commands;

import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.utils.AquaColors;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

import javax.annotation.Nonnull;

public class ReloadCommandsCommand extends AbstractPlayerCommand {
    private final AquaCommands mod;

    public ReloadCommandsCommand(AquaCommands mod) {
        super("aquareload", "Reload commands configuration", false);
        this.mod = mod;
        this.requirePermission("aquacommands.reload");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        mod.reload();
        playerRef.sendMessage(AquaColors.translate("&aAquaCommands configuration reloaded!"));
    }
}
