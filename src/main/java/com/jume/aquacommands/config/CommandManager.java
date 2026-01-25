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
     * Load commands from config file
     */
    public void loadCommands() {
        if (!configFile.exists()) {
            LOGGER.info("Config file not found, creating new one");
            saveCommands();
            return;
        }

        try (FileReader reader = new FileReader(configFile)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                commands = loaded;
                LOGGER.info("Loaded {} custom commands", commands.size());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load commands", e);
        }
    }

    /**
     * Save commands to config file
     */
    public void saveCommands() {
        try {
            configFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(commands, writer);
                LOGGER.info("Saved {} custom commands", commands.size());
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
