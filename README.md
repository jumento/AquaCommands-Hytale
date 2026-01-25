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
| `/aquacmdremove` | Opens UI to delete a command | `aquacommands.manage` |
| `/aquareload` | Reloads all commands from config | `aquacommands.reload` |
| `/[custom]` | Execute a custom command | `aquacommands.[commandname]` |

## 🔐 Permission System

AquaCommands supports **LuckPerms** for advanced permission management, with automatic fallback if LuckPerms is not installed.

### With LuckPerms

Use these permission nodes:

- `aquacommands.manage` - Access to create, edit, list, and delete commands (`/aquacmd`, `/aqualist`, `/aquacmdremove`)
- `aquacommands.reload` - Reload commands from configuration (`/aquareload`)
- `aquacommands.<name>` - Use specific custom command (e.g. for `/discord`, use `aquacommands.discord`)
- `aquacommands.*` - Use all custom commands (wildcard), though specific node assignment is recommended.

**Example LuckPerms commands:**

```bash
# Give a group permission to manage commands
/lp group admin permission set aquacommands.manage true

# Give a player permission to use the "discord" command
/lp user Steve permission set aquacommands.discord true
```

### Without LuckPerms (Fallback Mode)

When LuckPerms is not installed:

- ✅ **All players** can use custom commands (unless restricted by another plugin)
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
- *(Optional)* LuckPerms-Hytale for advanced permissions

### Install Steps

1. Download `AquaCommands-1.3.0.jar`
2. Place in your server's `mods/` or `plugins/` folder
3. *(Optional)* Install LuckPerms-Hytale for permission management
4. Restart the server
5. Use `/aquacmd` to start creating commands

## 🛠️ Technical Details

### Architecture

- **Command Storage**: Versioned JSON storage with smart migration
- **UI System**: Interactive Hytale UI for management
- **Dynamic Registration**: Commands registered at runtime
- **Permission System**: Validates via `player.hasPermission()` ensuring LP integration works correctly by bypassing system-level restrictive defaults.

### Project Structure (Partial)

```
src/main/java/com/jume/aquacommands/
├── AquaCommands.java              # Main plugin class
├── commands/
│   ├── DynamicCommand.java        # Custom command handler with permission bypass
│   └── ...
├── permissions/
│   └── PermissionManager.java     # Permission node resolution
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
- **Version**: 1.3.0

## 📄 License

This project is for use with Hytale servers.

## 🔄 Changelog

### v1.3.0 (Stable)

- 🚀 **Permissions Fixed**: Custom commands now properly respect restricted permissions in LuckPerms (instead of being blocked by default).
- ✨ Implemented system-bypass architecture for dynamic commands.
- 🔧 Unified permission nodes (removed redundant nesting).

### v1.0.0

- ✨ Initial release
- ✨ Custom command creation via UI
- ✨ Command persistence to JSON
- ✨ Dynamic command registration
- ✨ Smart URL Styling

## 👨‍💻 Developer Notes: Hytale Permissions Handling

*For future maintainers:*

In the Hytale Beta environment, `AbstractPlayerCommand` enforces a system-level permission whitelist check before the command execution logic is reached. If a dynamic command does not register a specific permission requirement via the platform API, it is often blocked by default (or by the permissions module) preventing execution.

To support external permission managers like **LuckPerms** correctly:

1. **System Bypass**: We override `requiresPermission()` to return `false` (or similar strategy). This ensures the command execution method is called.
2. **Internal Validation**: Inside the `execute()` method, we strictly check `player.hasPermission("node")`.

This "Hybrid Bypass" architecture ensures that:

- The command isn't silently swallowed by the platform.
- The external permission provider (LuckPerms) receives the check request and can properly authorize or deny based on its configuration.
