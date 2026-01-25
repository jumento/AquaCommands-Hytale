package com.jume.aquacommands.ui;

import com.jume.aquacommands.config.CommandManager;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Command List Page - Displays all custom commands
 * (Simple version - just displays text info)
 */
public class CommandListPage extends InteractiveCustomUIPage<CommandListPage.ListEventData> {

    private final CommandManager commandManager;

    public static class ListEventData {
        public String action;

        public static final BuilderCodec<ListEventData> CODEC = BuilderCodec
                .builder(ListEventData.class, ListEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (ListEventData o, String v) -> o.action = v,
                        (ListEventData o) -> o.action)
                .add()
                .build();
    }

    public CommandListPage(@Nonnull PlayerRef playerRef, @Nonnull CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ListEventData.CODEC);
        this.commandManager = commandManager;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        // For now, send to chat instead of using UI
        // TODO: Create AquaCommandList.ui file for proper UI

        Map<String, String> commands = commandManager.getAllCommands();

        if (commands.isEmpty()) {
            playerRef.sendMessage(Message.raw("No custom commands configured."));
        } else {
            playerRef.sendMessage(Message.raw("=== Custom Commands ==="));
            for (Map.Entry<String, String> entry : commands.entrySet()) {
                playerRef.sendMessage(Message.raw("/" + entry.getKey() + " -> " + entry.getValue()));
            }
            playerRef.sendMessage(Message.raw("====================="));
        }

        // Close the page immediately since we just sent chat messages
        Player player = store.getComponent(ref, Player.getComponentType());
        player.getPageManager().setPage(ref, store, Page.None);
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull ListEventData data) {
        // Not used for now
    }
}
