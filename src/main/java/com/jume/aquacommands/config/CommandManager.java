package com.jume.aquacommands.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jume.aquacommands.AquaCommands;
import com.jume.aquacommands.commands.DynamicCommand;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class CommandManager {
    private final AquaCommands plugin;
    private final File configDir;
    private final File commandsFile;
    private final Map<String, String> commands;
    private final Gson gson;

    public CommandManager(AquaCommands plugin) {
        this.plugin = plugin;
        this.configDir = new File("mods/AquaCommands");
        this.commandsFile = new File(configDir, "commands.json");
        this.commands = new ConcurrentHashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public void loadCommands() {
        if (!commandsFile.exists()) {
            AquaCommands.LOGGER.at(Level.INFO).log("No commands.json found, creating default");
            saveCommands();
            return;
        }

        try (Reader reader = new FileReader(commandsFile)) {
            Map<String, String> loaded = gson.fromJson(reader,
                    new TypeToken<Map<String, String>>() {
                    }.getType());

            if (loaded != null) {
                commands.clear();
                commands.putAll(loaded);
                AquaCommands.LOGGER.at(Level.INFO).log("Loaded " + loaded.size() + " custom commands");
            }
        } catch (IOException e) {
            AquaCommands.LOGGER.at(Level.SEVERE).withCause(e).log("Failed to load commands.json");
        }
    }

    public void saveCommands() {
        try (Writer writer = new FileWriter(commandsFile)) {
            gson.toJson(commands, writer);
            AquaCommands.LOGGER.at(Level.INFO).log("Saved " + commands.size() + " commands to disk");
        } catch (IOException e) {
            AquaCommands.LOGGER.at(Level.SEVERE).withCause(e).log("Failed to save commands.json");
        }
    }

    public void registerDynamicCommands(CommandRegistry registry) {
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            try {
                registry.registerCommand(new DynamicCommand(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                // Ignore duplicates
            }
        }
    }

    public void addCommand(String name, String response) {
        commands.put(name.toLowerCase(), response);
    }

    public void removeCommand(String name) {
        commands.remove(name.toLowerCase());
    }

    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }

    public String getResponse(String name) {
        return commands.get(name.toLowerCase());
    }

    public Map<String, String> getAllCommands() {
        return new HashMap<>(commands);
    }

    public int getCommandCount() {
        return commands.size();
    }
}
