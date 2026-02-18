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

public class CommandEditorPage extends InteractiveCustomUIPage<CommandEditorPage.CommandEventData> {

    private final CommandManager commandManager;

    public static class CommandEventData {
        public String action;
        public String cmdName;
        public String cmdContent;
        public String cmdColor;

        public static final BuilderCodec<CommandEventData> CODEC = BuilderCodec
                .builder(CommandEventData.class, CommandEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING), (o, v) -> o.action = v, o -> o.action)
                .add()
                .append(new KeyedCodec<>("@CmdName", Codec.STRING), (o, v) -> o.cmdName = v, o -> o.cmdName)
                .add()
                .append(new KeyedCodec<>("@CmdContent", Codec.STRING), (o, v) -> o.cmdContent = v, o -> o.cmdContent)
                .add()
                .append(new KeyedCodec<>("@CmdColor", Codec.STRING), (o, v) -> o.cmdColor = v, o -> o.cmdColor)
                .add()
                .build();
    }

    public CommandEditorPage(@Nonnull PlayerRef playerRef, CommandManager commandManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, CommandEventData.CODEC);
        this.commandManager = commandManager;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder cmd, @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/AquaCommandEditor.ui");
        cmd.set("#ColorInput.Color", "#FFFFFF");

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#CreateButton",
                new EventData().append("Action", "create")
                        .append("@CmdName", "#CommandNameInput.Value")
                        .append("@CmdContent", "#CommandResponseInput.Value")
                        .append("@CmdColor", "#ColorInput.Color"));

        evt.addEventBinding(CustomUIEventBindingType.Activating, "#CancelButton",
                new EventData().append("Action", "cancel"));
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store,
            @Nonnull CommandEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if ("create".equals(data.action)) {
            if (data.cmdName != null && !data.cmdName.trim().isEmpty() && data.cmdContent != null
                    && !data.cmdContent.trim().isEmpty()) {
                String name = data.cmdName.trim().replaceAll("\\s+", "");
                String color = (data.cmdColor != null && !data.cmdColor.isEmpty()) ? data.cmdColor : "#FFFFFF";

                commandManager.addCommand(name, data.cmdContent, color);
                commandManager.saveCommands(); // Usually auto-reload isn't instant but next restart or manual reload.
                // We should auto-register? Dynamic registration at runtime might work.

                playerRef.sendMessage(
                        AquaColors.translate("&aCommand &e/" + name + " &acreated! Run &b/aquareload &ato apply."));
                player.getPageManager().setPage(ref, store, Page.None);
            } else {
                playerRef.sendMessage(AquaColors.translate("&cName and content cannot be empty."));
            }
        } else if ("cancel".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }
}
