# BOT (Cognitive Orchestration System)

Document Name: BOT
Status: Canonical, immutable-by-edit
Document Version: 1.0.0
Created (Local Time - America/Monterrey): 2026-02-02

## 1) Purpose

This document is the primordial base of understanding between the AI and the user.
It defines the operating rules, constraints, and protocols for Hytale mod development.

## 2) Immutability and Governance (Append-Only)

- This document must never be deleted.
- This document must always be consulted before making decisions or changes.
- This document is append-only:
  - No corrections, rewrites, or deletions are allowed.
  - Any change in rules or direction must be recorded as a new log entry.
  - Rollbacks are handled by appending a note that something is deprecated, canceled, reverted, or superseded.

## 3) Non-Contamination Rule (Hytale Only)

- The project is 100% focused on Hytale.
- Never assume the code, architecture, or APIs relate to Minecraft or any other development ecosystem.
- Do not import terminology, design patterns, or mechanics from other games unless explicitly required by Hytale docs and confirmed by sources.

## 4) Language Contract

- User-facing explanations: Spanish.
- Programming artifacts (code, identifiers, filenames): English.
- Code comments: minimal, ASCII only, no emojis.

## 5) Development Focus

- Primary domain: Hytale mod development.
- Authoritative API and documentation source is: <https://github.com/Henry-Bonikowsky/hytale-docs>

## 6) Chat Message Formatting (Hytale)

- **NEVER use Minecraft color codes** (e.g., `\u00A7c`, `§c`) in chat messages.
- Hytale uses the `Message` API with method chaining for formatting.
- **Correct pattern for colored messages**:

  ```java
  playerRef.sendMessage(Message.empty().insert("Text").color("#HEX"));
  ```

- Links should be clickable using `.link(url)`.

---

# LOG (Append-Only)

## 2026-02-02 (Entry 1)

- Timestamp (America/Monterrey): 2026-02-02 00:43
- Actor: AI (Antigravity)
- Type: save_point
- Summary: Save Point 'aqua_recon' - Massive reconstruction and API fix.
- Details:
  - **Reconstruction**: Restored UI files (`.ui`) from the original JAR resources.
  - **API Fix**: Pivoted from `net.hytale.api` to `com.hypixel.hytale.server.core` to match the local `HytaleServer.jar`.
  - **Architecture**:
    - Ported main class to `JavaPlugin`.
    - Implemented commands using `AbstractPlayerCommand`.
    - Created `CommandManager` for JSON-based dynamic command storage.
  - **UI Implementation**:
    - Implemented `CommandEditorPage`, `CommandListPage`, and `CommandRemovePage`.
    - Fixed UI list display issue by explicitly setting field values in `build()`.
    - Corrected UI paths to be relative to `Common/UI/Custom/`.
  - **Color & Links**:
    - Implemented `AquaColors.java` to translate `&` codes into Hytale `Message` with hex colors.
    - Added automatic URL detection for clickable links (violet color `#EE82EE`).
  - **Git**: Commiitted and tagged as `aqua_recon`.
- Affected artifacts: All source files, build.gradle, manifest.json, resources/Common/UI/..., BOT.md.
- Verification (build/compile): SUCCESS (Gradle build successful).
- Status: active

## 2026-02-17 (Entry 2)

- Timestamp (America/Monterrey): 2026-02-17 17:01
- Actor: AI (Antigravity)
- Type: fix
- Summary: Fix build dependency path for LuckPerms.
- Details:
  - **Build Config**: Updated `build.gradle` to reference the correct filename `LuckPerms-Hytale-5.5.34.jar` instead of generic `LuckPerms.jar`.
- Affected artifacts: build.gradle.
- Verification (build/compile): SUCCESS (Gradle build successful).
- Status: active

## 2026-02-18 (Entry 3)

- Timestamp (America/Monterrey): 2026-02-18 04:15
- Actor: AI (Antigravity)
- Type: feature_release
- Summary: Save Point 'aqua24' - UI Color Picker & Config V2.
- Details:
  - **Command Config V2**:
    - Refactored 'commands.json' to version 2 structure ('{ "response": "...", "color": "..." }').
    - Added automatic migration from V1/Flat formats.
  - **UI Implementation**:
    - Added 'ColorPickerDropdownBox' to 'AquaCommandEditor.ui' (fixing previous crash with standard 'ColorPicker').
    - Updated 'CommandEditorPage' to bind to '.Color' property.
  - **Color & Links**:
    - Commands can now have custom colors.
    - Updated 'AquaColors' to handle 8-digit hex codes (stripping alpha).
    - URLs now inherit the command's custom color (unless white, then default Violet).
- Affected artifacts: CommandManager, AquaColors, AquaCommandEditor.ui, CommandEditorPage.
- Verification: Gradle build success. Client crash resolved.
- Status: active
