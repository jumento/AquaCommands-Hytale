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

public class CommandRemovePage extends InteractiveCustomUIPage<CommandRemovePage.RemoveEventData> {

    private final CommandManager commandManager;

    public static class RemoveEventData {
        public String action;
        public String commandName;

        public static final BuilderCodec<RemoveEventData> CODEC = BuilderCodec
                .builder(RemoveEventData.class, RemoveEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (RemoveEventData o, String v) -> o.action = v,
                        (RemoveEventData o) -> o.action)
                .add()
                .append(
                        new KeyedCodec<>("@CommandName", Codec.STRING),
                        (RemoveEventData o, String v) -> o.commandName = v,
                        (RemoveEventData o) -> o.commandName)
                .add()
                .build();
    }

    public CommandRemovePage(@Nonnull PlayerRef playerRef, @Nonnull CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, RemoveEventData.CODEC);
        this.commandManager = commandManager;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/AquaCommandRemove.ui");

        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#DeleteButton",
                new EventData()
                        .append("Action", "delete")
                        .append("@CommandName", "#CommandNameInput.Value"));

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
            @Nonnull RemoveEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if ("delete".equals(data.action)) {
            String cmd = data.commandName;
            if (cmd != null && !cmd.isEmpty()) {
                if (commandManager.hasCommand(cmd)) {
                    commandManager.removeCommand(cmd);
                    playerRef.sendMessage(Message.raw("Command '" + cmd + "' has been removed."));
                    playerRef.sendMessage(Message.raw("Please restart server to apply changes fully."));
                } else {
                    playerRef.sendMessage(Message.raw("Error: Command '" + cmd + "' does not exist."));
                }
            } else {
                playerRef.sendMessage(Message.raw("Please enter a command name."));
                return; // Keep UI open
            }
        } else {
            playerRef.sendMessage(Message.raw("Deletion cancelled."));
        }

        player.getPageManager().setPage(ref, store, Page.None);
    }
}
