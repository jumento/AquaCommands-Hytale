package com.jume.aquacommands;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.logger.HytaleLogger;
import com.jume.aquacommands.config.CommandManager;
import com.jume.aquacommands.commands.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import java.util.logging.Level;

public class AquaCommands extends JavaPlugin {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static AquaCommands instance;
    private CommandManager commandManager;

    public AquaCommands(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        super.setup();
        LOGGER.at(Level.INFO).log("AquaCommands initializing...");

        commandManager = new CommandManager(this);
        commandManager.loadCommands(); // Load first so dynamic commands are known

        registerCommands();
        commandManager.registerDynamicCommands(this.getCommandRegistry());

        LOGGER.at(Level.INFO).log("AquaCommands setup complete.");
    }

    private void registerCommands() {
        this.getCommandRegistry().registerCommand(new AquaCmdCommand(this));
        this.getCommandRegistry().registerCommand(new ListCommandsCommand(this));
        this.getCommandRegistry().registerCommand(new RemoveCommandCommand(this));
        this.getCommandRegistry().registerCommand(new ReloadCommandsCommand(this));
    }

    public void reload() {
        commandManager.loadCommands();
        commandManager.registerDynamicCommands(this.getCommandRegistry());
        LOGGER.at(Level.INFO).log("Reloaded configuration via command.");
    }

    public static AquaCommands get() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
