# AquaCommands - Custom Commands System for Hytale

Create and manage custom chat commands with an intuitive in-game UI. **Now with LuckPerms support!**

## 🌊 Features

- **Custom Commands**: Create commands that display custom messages in chat
- **In-Game Management**: Easy-to-use UI for creating, editing, and deleting commands
- **Persistent Storage**: Commands are saved to `commands.json`
- **Dynamic Registration**: Commands are registered automatically on server start
- **Hot Reload**: Reload commands without restarting the server with `/aquareload`
- **Permission System**: Full LuckPerms integration with fallback support
- **User-Friendly**: Manage everything through intuitive UIs

## 📋 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/aquacmd` | Opens the command creation/edit UI | `aquacommands.manage` |
| `/aqualist` | Opens UI showing all custom commands | `aquacommands.manage` |
| `/aquacmdremove` | Opens UI to delete a command | `aquacommands.admin` |
| `/aquareload` | Reloads all commands from config | `aquacommands.reload` |
| `/[custom]` | Execute a custom command | `aquacommands.command.[commandname]` |

## 🔐 Permission System

AquaCommands supports **LuckPerms** for advanced permission management, with automatic fallback if LuckPerms is not installed.

### With LuckPerms

Use these permission nodes:

- `aquacommands.manage` - Access to create, edit, and list commands (`/aquacmd`, `/aqualist`)
- `aquacommands.reload` - Reload commands from configuration (`/aquareload`)
- `aquacommands.command.<name>` - Use specific custom command (e.g., `aquacommands.command.discord`)
- `aquacommands.command.*` - Use all custom commands (wildcard)

**Example LuckPerms commands:**

```bash
# Give a group permission to manage commands
/lp group admin permission set aquacommands.manage true

# Give a player permission to use the "discord" command
/lp user Steve permission set aquacommands.command.discord true

# Give everyone permission to use all custom commands
/lp group default permission set aquacommands.command.* true
```

### Without LuckPerms (Fallback Mode)

When LuckPerms is not installed:

- ✅ **All players** can use custom commands
- ❌ **Only operators** can manage commands (`/aquacmd`, `/aqualist`, `/aquareload`)

## 🎮 Usage Example

1. Run `/aquacmd` to open the management UI
2. Enter command name: `discord`
3. Enter response: `Join our Discord: discord.gg/example`
4. Click "Create Command"
5. Now `/discord` will display "Join our Discord: discord.gg/example" in chat

## 📝 Configuration

Commands are stored in `mods/AquaCommands/commands.json`:

```json
{
  "discord": "Join our Discord: discord.gg/example",
  "website": "Visit our website: example.com",
  "rules": "1. Be respectful\n2. No griefing\n3. Have fun!"
}
```

You can manually edit this file and use `/aquareload` to apply changes without restarting.

## 🚀 Installation

### Requirements

- Hytale Server (Beta)
- *(Optional)* LuckPerms-Hytale 5.5.25+ for advanced permissions

### Install Steps

1. Download `AquaCommands-1.0.0.jar`
2. Place in your server's `plugins/` folder
3. *(Optional)* Install LuckPerms-Hytale for permission management
4. Restart the server
5. Use `/aquacmd` to start creating commands

## 🛠️ Technical Details

### Architecture

- **Command Storage**: JSON-based persistent storage
- **UI System**: Interactive Hytale UI for management
- **Dynamic Registration**: Commands registered at runtime
- **Permission System**: LuckPerms API integration with graceful fallback

### Project Structure

```
src/main/java/com/jume/aquacommands/
├── AquaCommands.java              # Main plugin class
├── commands/
│   ├── AquaCmdCommand.java        # Main management command (/aquacmd)
│   ├── ListCommandsCommand.java   # List commands UI (/aqualist)
│   ├── ReloadCommandsCommand.java # Reload command (/aquareload)
│   └── DynamicCommand.java        # Template for custom commands
├── config/
│   └── CommandManager.java        # Command storage manager
├── permissions/
│   └── PermissionManager.java     # LuckPerms integration & fallback
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
- View command responses
- Formatted table view
- Easy navigation

## 🤝 Credits

- **Authors**: jume, Antigravity
- **For**: Hytale Server
- **Version**: 1.0.0
- **LuckPerms Integration**: Uses LuckPerms API 5.5.25

## 📄 License

This project is for use with Hytale servers.

## 🐛 Troubleshooting

### Commands not working

- Ensure you have appropriate permissions
- Check `commands.json` exists in `mods/AquaCommands/`
- Try `/aquareload`
- Verify plugin loaded successfully in server logs

### UI not opening

- Verify plugin loaded successfully
- Check server logs for errors
- Ensure you have `aquacommands.manage` permission

### Permission system not working

- If using LuckPerms: Verify LuckPerms is installed and loaded **before** AquaCommands
- Check server startup logs for message: `Permission system initialized (LuckPerms: enabled/disabled)`
- If LuckPerms shows as disabled but is installed, check plugin load order

### LuckPerms Auto-Detection

AquaCommands will log one of these messages on startup:

- `Permission system initialized (LuckPerms: enabled)` ✅ Using LuckPerms
- `Permission system initialized (LuckPerms: disabled)` ⚠️ Using fallback (op-based)

## 📞 Support

For issues, check server logs or contact the development team.

## 🔄 Changelog

### v1.0.0 (Current)

- ✨ Initial release
- ✨ Custom command creation via UI
- ✨ Command persistence to JSON
- ✨ Dynamic command registration
- ✨ LuckPerms integration with automatic fallback
- ✨ `/aquareload` command for hot-reloading
- ✨ `/aqualist` and `/aquacmdremove` UI commands
- ✨ Per-command permission system
- ✨ **Smart URL Styling**: URLs in messages are automatically clickable, blue, and underlined without needing special codes.
