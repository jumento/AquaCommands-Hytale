# AquaCommands - Custom Commands System for Hytale

Create and manage custom chat commands with an intuitive in-game UI.

## 🌊 Features

- **Custom Commands**: Create commands that display custom messages in chat
- **In-Game Management**: Easy-to-use UI for creating, editing, and deleting commands
- **Persistent Storage**: Commands are saved to `commands.json`
- **Dynamic Registration**: Commands are registered automatically on server start
- **User-Friendly**: No restart required after creating/editing commands

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/aquacmd` | Opens the command management UI | Operator |
| `/aquacmd list` | Lists all custom commands | Operator |
| `/aquacmd reload` | Reloads commands from config | Operator |

## 🎮 Usage Example

1. Run `/aquacmd` to open the management UI
2. Enter command name: `yt`
3. Enter response: `Subscribe to my channel!`
4. Click "Create Command"
5. Now `/yt` will display "Subscribe to my channel!" in chat

## 📝 Configuration

Commands are stored in `mods/AquaCommands/commands.json`:

```json
{
  "yt": "Subscribe to my channel!",
  "discord": "Join our Discord: discord.gg/example",
  "rules": "1. Be respectful\n2. No griefing\n3. Have fun!"
}
```

## 🚀 Installation

1. Download `AquaCommands-1.0.0.jar`
2. Place in your server's `plugins/` folder
3. Restart the server
4. Use `/aquacmd` to start creating commands

## 🛠️ Technical Details

### Architecture

- **Command Storage**: JSON-based persistent storage
- **UI System**: Interactive Hytale UI for management
- **Dynamic Registration**: Commands registered at runtime

### Project Structure

```
src/main/java/com/jume/aquacommands/
├── AquaCommands.java              # Main plugin class
├── commands/
│   ├── AquaCmdCommand.java        # Main management command
│   └── DynamicCommand.java        # Template for custom commands
├── config/
│   └── CommandManager.java        # Command storage manager
└── ui/
    ├── CommandEditorPage.java     # Create/Edit UI
    └── CommandListPage.java       # List/Delete UI

src/main/resources/
├── Common/UI/Custom/Pages/
│   ├── AquaCommandEditor.ui      # Editor UI definition
│   └── AquaCommandList.ui        # List UI definition
├── manifest.json
├── mod.json
└── commands.json                  # Commands storage
```

## 🎨 UI Features

### Command Editor

- Command Name input field
- Response/Message textarea
- Create and Cancel buttons
- Real-time validation

### Command List

- Display all custom commands
- Delete button for each command
- Edit button for each command
- Formatted table view

## 🤝 Credits

- **Authors**: jume, Antigravity
- **For**: Hytale Server
- **Version**: 1.0.0

## 📄 License

This project is for use with Hytale servers.

## 🐛 Troubleshooting

### Commands not working

- Ensure you have operator permissions
- Check `commands.json` exists in `mods/AquaCommands/`
- Try `/aquacmd reload`

### UI not opening

- Verify plugin loaded successfully
- Check server logs for errors
- Ensure you're an operator

## 📞 Support

For issues, check server logs or contact the development team.
