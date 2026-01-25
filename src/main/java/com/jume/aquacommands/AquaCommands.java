package com.jume.aquacommands;

import com.jume.aquacommands.commands.AquaCmdCommand;
import com.jume.aquacommands.commands.DynamicCommand;
import com.jume.aquacommands.commands.ListCommandsCommand;
import com.jume.aquacommands.commands.ReloadCommandsCommand;
import com.jume.aquacommands.config.CommandManager;
import com.jume.aquacommands.permissions.PermissionManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * AquaCommands - Custom chat commands system
 * 
 * @author jume, Antigravity
 */
public class AquaCommands extends JavaPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(AquaCommands.class);
    private static AquaCommands instance;

    private CommandManager commandManager;
    private PermissionManager permissionManager;

    public AquaCommands(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void setup() {
        instance = this;
        LOGGER.info("=== AquaCommands v1.0.0 Starting ===");

        // 1. Initialize Permission Manager
        permissionManager = PermissionManager.getInstance();
        LOGGER.info("Permission system initialized (LuckPerms: {})",
                permissionManager.isLuckPermsAvailable() ? "enabled" : "disabled");

        // 2. Initialize CommandManager
        File configDir = new File("mods/AquaCommands");
        configDir.mkdirs();
        File commandsFile = new File(configDir, "commands.json");

        commandManager = new CommandManager(commandsFile);
        LOGGER.info("CommandManager initialized with {} commands", commandManager.getAllCommands().size());

        // 3. Register main command
        this.getCommandRegistry().registerCommand(new AquaCmdCommand());
        LOGGER.info("Registered command: /aquacmd");

        // 4. Register reload command
        this.getCommandRegistry().registerCommand(new ReloadCommandsCommand());
        LOGGER.info("Registered command: /aquareload");

        // 5. Register list command
        this.getCommandRegistry().registerCommand(new ListCommandsCommand());
        LOGGER.info("Registered command: /aqualist");

        // 6. Register all custom commands
        registerCustomCommands();

        LOGGER.info("=== AquaCommands v1.0.0 Enabled ===");
    }

    public void teardown() {
        LOGGER.info("=== AquaCommands v1.0.0 Disabled ===");
    }

    /**
     * Register all custom commands from config
     */
    public void registerCustomCommands() {
        LOGGER.info("Registering custom commands...");

        commandManager.getAllCommands().forEach((name, response) -> {
            try {
                this.getCommandRegistry().registerCommand(new DynamicCommand(name, response));
                LOGGER.info("Registered custom command: /{}", name);
            } catch (Exception e) {
                LOGGER.error("Failed to register custom command: /{}", name, e);
            }
        });

        LOGGER.info("Custom command registration complete.");
    }

    /**
     * Reload commands from config
     */
    public void reloadCommands() {
        commandManager.loadCommands();
        // Note: New commands will be registered, but old ones may persist
        // until server restart depending on Hytale's command registry behavior.
        registerCustomCommands();
        LOGGER.info("Reloaded commands");
    }

    public static AquaCommands getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}
