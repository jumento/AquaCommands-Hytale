package com.jume.aquacommands.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.jume.aquacommands.config.CommandManager;
import com.jume.aquacommands.utils.AquaColors;
import javax.annotation.Nonnull;
import java.util.Map;

public class CommandListPage extends InteractiveCustomUIPage<CommandListPage.ListEventData> {

    private final CommandManager commandManager;

    public static class ListEventData {
        public String action;

        public static final BuilderCodec<ListEventData> CODEC = BuilderCodec
                .builder(ListEventData.class, ListEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, v) -> o.action = v, o -> o.action)
                .add()
                .build();
    }

    public CommandListPage(@Nonnull PlayerRef playerRef, CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ListEventData.CODEC);
        this.commandManager = commandManager;

        // Still send to chat as fallback with proper colors/links
        playerRef.sendMessage(AquaColors.translate("&e[AquaCommands] Displaying list in UI..."));
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/AquaCommandList.ui");

        // Populate the list in the UI
        StringBuilder sb = new StringBuilder();
        Map<String, String> cmds = commandManager.getAllCommands();
        if (cmds.isEmpty()) {
            sb.append("No commands created yet.");
        } else {
            for (Map.Entry<String, String> entry : cmds.entrySet()) {
                sb.append("/").append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
            }
        }
        cmd.set("#CommandListText.Text", sb.toString());

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton",
                new EventData().append("Action", "close"));
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store,
            @Nonnull ListEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());
        if ("close".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }
}
