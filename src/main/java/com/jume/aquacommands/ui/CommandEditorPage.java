package com.jume.aquacommands.ui;

import com.jume.aquacommands.config.CommandManager;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

/**
 * CommandEditorPage - Interactive UI for creating/editing custom commands
 */
public class CommandEditorPage extends InteractiveCustomUIPage<CommandEditorPage.CommandEventData> {

    private final CommandManager commandManager;

    /**
     * Event data structure for handling UI interactions
     */
    public static class CommandEventData {
        public String action; // "create" or "cancel"
        public String commandName;
        public String commandResponse;

        public static final BuilderCodec<CommandEventData> CODEC = BuilderCodec
                .builder(CommandEventData.class, CommandEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (CommandEventData o, String v) -> o.action = v,
                        (CommandEventData o) -> o.action)
                .add()
                .append(
                        new KeyedCodec<>("@CommandName", Codec.STRING),
                        (CommandEventData o, String v) -> o.commandName = v,
                        (CommandEventData o) -> o.commandName)
                .add()
                .append(
                        new KeyedCodec<>("@CommandResponse", Codec.STRING),
                        (CommandEventData o, String v) -> o.commandResponse = v,
                        (CommandEventData o) -> o.commandResponse)
                .add()
                .build();
    }

    public CommandEditorPage(@Nonnull PlayerRef playerRef, @Nonnull CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, CommandEventData.CODEC);
        this.commandManager = commandManager;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        // Load the UI definition file
        cmd.append("Pages/AquaCommandEditor.ui");

        // Bind the Create Command button
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CreateButton",
                new EventData()
                        .append("Action", "create")
                        .append("@CommandName", "#CommandNameInput.Value")
                        .append("@CommandResponse", "#CommandResponseInput.Value"));

        // Bind the Cancel button
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CancelButton",
                new EventData()
                        .append("Action", "cancel"));
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        switch (data.action) {
            case "create":
                handleCreate(ref, store, player, data);
                break;

            case "cancel":
                handleCancel(ref, store, player);
                break;
        }
    }

    private void handleCreate(Ref<EntityStore> ref, Store<EntityStore> store, Player player, CommandEventData data) {
        try {
            // Validate command name
            if (data.commandName == null || data.commandName.trim().isEmpty()) {
                playerRef.sendMessage(Message.raw("Error: Please enter a command name!"));
                player.getPageManager().setPage(ref, store, Page.None);
                return;
            }

            // Validate command response
            if (data.commandResponse == null || data.commandResponse.trim().isEmpty()) {
                playerRef.sendMessage(Message.raw("Error: Please enter a command response!"));
                player.getPageManager().setPage(ref, store, Page.None);
                return;
            }

            String cmdName = data.commandName.trim().toLowerCase();
            String response = data.commandResponse.trim();

            // Save command
            commandManager.setCommand(cmdName, response);

            // Success message
            playerRef.sendMessage(Message.raw("Command created successfully!"));
            playerRef.sendMessage(Message.raw("/" + cmdName + " -> " + response));
            playerRef.sendMessage(Message.raw("Restart the server to activate the command."));

            // Close UI
            player.getPageManager().setPage(ref, store, Page.None);

        } catch (Exception e) {
            playerRef.sendMessage(Message.raw("Error creating command: " + e.getMessage()));
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }

    private void handleCancel(Ref<EntityStore> ref, Store<EntityStore> store, Player player) {
        playerRef.sendMessage(Message.raw("Command creation cancelled."));
        player.getPageManager().setPage(ref, store, Page.None);
    }
}
