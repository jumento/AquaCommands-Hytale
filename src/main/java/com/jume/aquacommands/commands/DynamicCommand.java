package com.jume.aquacommands.commands;

import com.jume.aquacommands.utils.AquaColors;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

import javax.annotation.Nonnull;

public class DynamicCommand extends AbstractPlayerCommand {
    private final String response;

    private final String color;

    public DynamicCommand(String name, String response, String color) {
        super(name, "Custom command", false);
        this.response = response;
        this.color = (color == null || color.isEmpty()) ? "#FFFFFF" : color;
        this.requirePermission("aquacommands." + name.toLowerCase());
    }

    // For legacy support or tests
    public DynamicCommand(String name, String response) {
        this(name, response, "#FFFFFF");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {

        playerRef.sendMessage(AquaColors.translate(response, color));
    }
}
