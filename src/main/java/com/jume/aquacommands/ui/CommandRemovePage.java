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

public class CommandRemovePage extends InteractiveCustomUIPage<CommandRemovePage.RemoveEventData> {

    private final CommandManager commandManager;

    public static class RemoveEventData {
        public String action;
        public String cmdName;

        public static final BuilderCodec<RemoveEventData> CODEC = BuilderCodec
                .builder(RemoveEventData.class, RemoveEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, v) -> o.action = v, o -> o.action)
                .add()
                .append(new KeyedCodec<>("@CmdName", Codec.STRING), (o, v) -> o.cmdName = v, o -> o.cmdName)
                .add()
                .build();
    }

    public CommandRemovePage(@Nonnull PlayerRef playerRef, CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, RemoveEventData.CODEC);
        this.commandManager = commandManager;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/AquaCommandRemove.ui");

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#DeleteButton",
                new EventData().append("Action", "delete")
                        .append("@CmdName", "#CommandNameInput.Value"));

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#CancelButton",
                new EventData().append("Action", "cancel"));
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store,
            @Nonnull RemoveEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if ("delete".equals(data.action)) {
            if (data.cmdName != null && !data.cmdName.trim().isEmpty()) {
                String cmd = data.cmdName.trim();
                if (commandManager.hasCommand(cmd)) {
                    commandManager.removeCommand(cmd);
                    commandManager.saveCommands();
                    playerRef.sendMessage(
                            AquaColors.translate("&aCommand &e/" + cmd + " &aremoved. Run &b/aquareload &ato apply."));
                    player.getPageManager().setPage(ref, store, Page.None);
                } else {
                    playerRef.sendMessage(AquaColors.translate("&cCommand not found: " + cmd));
                }
            }
        } else if ("cancel".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }
}
