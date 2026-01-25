package com.jume.aquacommands.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom commands stored in commands.json
 */
public class CommandManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File configFile;
    private Map<String, String> commands; // command -> response

    public CommandManager(File configFile) {
        this.configFile = configFile;
        this.commands = new HashMap<>();
        loadCommands();
    }

    /**
     * Load commands with smart migration support
     */
    public void loadCommands() {
        if (!configFile.exists()) {
            LOGGER.info("Config file not found, creating new one");
            saveCommands();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            com.google.gson.JsonElement root = com.google.gson.JsonParser.parseReader(reader);

            if (root.isJsonObject()) {
                com.google.gson.JsonObject obj = root.getAsJsonObject();
                if (obj.has("version") && obj.has("commands")) {
                    // New format: Load as AquaConfig
                    AquaConfig config = GSON.fromJson(root, AquaConfig.class);
                    this.commands = config.commands;
                    LOGGER.info("Loaded config v{} with {} custom commands", config.version, commands.size());
                } else {
                    // Legacy format: Root is the map itself
                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    Map<String, String> legacy = GSON.fromJson(root, type);
                    this.commands = legacy != null ? legacy : new HashMap<>();
                    LOGGER.info("Legacy config detected. Migrating {} commands to v{}",
                            commands.size(), AquaConfig.CURRENT_VERSION);
                    saveCommands(); // Automatically migrate to new format
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load commands", e);
            this.commands = new HashMap<>(); // Fail safe to avoid nulls
        }
    }

    /**
     * Save commands in the new AquaConfig format
     */
    public void saveCommands() {
        try {
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                AquaConfig config = new AquaConfig();
                config.commands = this.commands;
                // config.version is automatically set to CURRENT_VERSION
                GSON.toJson(config, writer);
                LOGGER.info("Saved {} custom commands (v{})", commands.size(), config.version);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save commands", e);
        }
    }

    /**
     * Add or update a command
     */
    public void setCommand(String command, String response) {
        commands.put(command.toLowerCase(), response);
        saveCommands();
    }

    /**
     * Remove a command
     */
    public void removeCommand(String command) {
        commands.remove(command.toLowerCase());
        saveCommands();
    }

    /**
     * Get response for a command
     */
    public String getResponse(String command) {
        return commands.get(command.toLowerCase());
    }

    /**
     * Check if command exists
     */
    public boolean hasCommand(String command) {
        return commands.containsKey(command.toLowerCase());
    }

    /**
     * Get all commands
     */
    public Map<String, String> getAllCommands() {
        return new HashMap<>(commands);
    }
}
