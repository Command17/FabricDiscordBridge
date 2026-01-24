*Bumped version to **2.0.0**!*

Changelog:
- Ported to **1.21.11**
- Overhauled config system:
  - *Incompatible with the old config system*
  - Located at `YOUR_MINECRAFT_FOLDER/config/fabricDiscordBridge/`
  - Comments are included
  - Every Discord message is even more configurable
  - Removed `/discordbridge config` command
- New placeholder system using curly brackets:
  - `%placeholder%` has been replaced by `{placeholder}`
  - [QuickText](https://placeholders.pb4.eu/user/quicktext/) got removed. Use [vanilla's formatting](https://minecraft.wiki/w/Formatting_codes) instead
  - Some placeholders have been renamed:
    - `%user%` -> `{discordUser}`
    - `%userDisplay%` -> `{discordUserDisplay}`
    - `%advancementDescription%` -> `{advancementDesc}`
  - New placeholder has been added:
    - `{advancementDisplay}` will show "Task completed!", "Goal completed!" or "Challenge completed!" depending on the advancement type
- New Discord messages have been added:
  - Server start message
  - Server stop message
- Improved Discord to Minecraft chat bridge:
  - Vanilla formatting codes will now get stripped from Discord messages when being displayed in Minecraft
  - Messages with Attachments will show "[Attachment]" at the end in Minecraft
  - Fixed voice messages not being displayed as "[Voice Message]" in Minecraft