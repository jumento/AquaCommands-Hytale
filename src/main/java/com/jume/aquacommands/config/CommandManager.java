package com.jume.aquacommands.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private final Map<String, CommandData> commands;
    private final Gson gson;

    public static class CommandData {
        private String response;
        private String color;

        public CommandData(String response, String color) {
            this.response = response;
            this.color = color;
        }

        public String getResponse() {
            return response;
        }

        public String getColor() {
            return color;
        }
    }

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
            JsonElement rootElement = JsonParser.parseReader(reader);

            if (rootElement.isJsonObject()) {
                JsonObject root = rootElement.getAsJsonObject();
                commands.clear();

                if (root.has("version")) {
                    int version = root.get("version").getAsInt();
                    if (version == 1) {
                        // V1: { version: 1, commands: { "name": "response" } }
                        if (root.has("commands")) {
                            JsonObject cmds = root.getAsJsonObject("commands");
                            for (String key : cmds.keySet()) {
                                String resp = cmds.get(key).getAsString();
                                commands.put(key, new CommandData(resp, "#FFFFFF"));
                            }
                        }
                    } else if (version >= 2) {
                        // V2: { version: 2, commands: { "name": { "response": "...", "color": "..." } }
                        // }
                        if (root.has("commands")) {
                            JsonObject cmds = root.getAsJsonObject("commands");
                            for (String key : cmds.keySet()) {
                                JsonObject data = cmds.get(key).getAsJsonObject();
                                String resp = data.get("response").getAsString();
                                String color = data.has("color") ? data.get("color").getAsString() : "#FFFFFF";
                                commands.put(key, new CommandData(resp, color));
                            }
                        }
                    }
                } else {
                    // Start of handling for flat map or unknown format without version
                    // Assuming flat map { "cmd": "resp" }
                    for (String key : root.keySet()) {
                        try {
                            JsonElement val = root.get(key);
                            if (val.isJsonPrimitive() && val.getAsJsonPrimitive().isString()) {
                                commands.put(key, new CommandData(val.getAsString(), "#FFFFFF"));
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
                AquaCommands.LOGGER.at(Level.INFO).log("Loaded " + commands.size() + " custom commands");

                // If we loaded simpler versions, we should probably save the upgraded version
                saveCommands();
            }
        } catch (IOException e) {
            AquaCommands.LOGGER.at(Level.SEVERE).withCause(e).log("Failed to load commands.json");
        } catch (Exception e) {
            AquaCommands.LOGGER.at(Level.SEVERE).withCause(e)
                    .log("Error parsing commands.json - possibly invalid format");
        }
    }

    public void saveCommands() {
        try (Writer writer = new FileWriter(commandsFile)) {
            Map<String, Object> root = new LinkedHashMap<>();
            root.put("version", 2);
            root.put("commands", commands); // Gson serializes CommandData objects
            gson.toJson(root, writer);
            AquaCommands.LOGGER.at(Level.INFO).log("Saved " + commands.size() + " commands to disk");
        } catch (IOException e) {
            AquaCommands.LOGGER.at(Level.SEVERE).withCause(e).log("Failed to save commands.json");
        }
    }

    public void registerDynamicCommands(CommandRegistry registry) {
        for (Map.Entry<String, CommandData> entry : commands.entrySet()) {
            try {
                registry.registerCommand(new DynamicCommand(entry.getKey(), entry.getValue().getResponse(),
                        entry.getValue().getColor()));
            } catch (Exception e) {
                // Ignore duplicates
            }
        }
    }

    public void addCommand(String name, String response) {
        addCommand(name, response, "#FFFFFF");
    }

    public void addCommand(String name, String response, String color) {
        commands.put(name.toLowerCase(), new CommandData(response, color));
    }

    public void removeCommand(String name) {
        commands.remove(name.toLowerCase());
    }

    public boolean hasCommand(String name) {
        return commands.containsKey(name.toLowerCase());
    }

    public String getResponse(String name) {
        CommandData data = commands.get(name.toLowerCase());
        return data != null ? data.getResponse() : null;
    }

    public Map<String, String> getAllCommands() {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, CommandData> entry : commands.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getResponse());
        }
        return result;
    }

    public int getCommandCount() {
        return commands.size();
    }
}
