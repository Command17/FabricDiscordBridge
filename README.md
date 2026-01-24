<img src="assets/icon.png" width="130" height="130" align="right"/>

# Fabric Discord Bridge

[![available_fabric](https://github.com/intergrav/devins-badges/raw/refs/heads/v3/assets/compact/supported/fabric_vector.svg)](https://fabricmc.net/)

[![requires_fabric_api](https://github.com/intergrav/devins-badges/raw/refs/heads/v3/assets/compact/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

---

## About 📖

A Minecraft Fabric mod made with **[JDA](https://github.com/discord-jda/JDA)** allowing you to bridge your Minecraft and Discord chats.

I made this mod for private usage first but thought maybe someone else could use this too, so I made it public (only on GitHub!)

**Please look at alternatives first before choosing this one!**

## Configuration ⚙️

### Bot Configuration

Located at: `YOUR_MINECRAFT_FOLDER/config/fabricDiscordBridge/bot.properties`

Configuration for your Discord bot. This also includes a customizable activity for your bot.

You'll need to paste your bot's token after `token=` for this mod to work.
`channelId` will also need to be replaced with the ID of your Discord server's channel.

```properties
# Token of the Discord bot.
token=
# Whether to automatically start the Discord bot on server start.
autoStart=true
# Type of activity the Discord bot should display. Allowed values: NONE, LISTENING, PLAYING, COMPETING, WATCHING, CUSTOM
activityType=PLAYING
# The activity the Discord bot should display.
activity=Minecraft
# ID of the Discord channel the Discord bot should send its messages in.
channelId=0
```

### Mod Configuration

Located at: `YOUR_MINECRAFT_FOLDER/config/fabricDiscordBridge/config.properties`

General configuration for your bot. Here you can enable and disable features or change messages
displayed in Minecraft.

When using [vanilla formatting](https://minecraft.wiki/w/Formatting_codes#Formatting_codes), you'll need to replace the code (e.g. `§1`, `§b`) with their Unicode variant.
Usually, the `§` character just needs to be replaced with `\u00A7`.

```properties
# Enables the Discord bot.
enabled=true
# Enables the server start message.
discord.serverStartMsgEnabled=true
# Enables the server stop message.
discord.serverStopMsgEnabled=true
# Enables the player join message.
discord.playerJoinMsgEnabled=true
# Enables the player leave message.
discord.playerLeaveMsgEnabled=true
# Enables the player award advancement message.
discord.playerAwardAdvancementMsgEnabled=true
# Enables the player died message.
discord.playerDiedMsgEnabled=true
# Enables the Minecraft to Discord chat bridge.
m2d.enabled=true
# If true, messages from chat will only be sent to Discord via a command.
m2d.sendCommandOnly=false
# Message to display in Minecraft when sending a message via command to Discord.
# Supported placeholders: player, playerDisplay, playerUuid, message
m2d.commandMsg=\u00A77[{player} -> DISCORD]: {message}
# Enables the Discord to Minecraft chat bridge.
d2m.enabled=true
# Maximum Discord message length to be displayed in Minecraft.
d2m.maxMsgLength=256
# Message to display in Minecraft when sending a message via Discord.
# Supported placeholders: discordUser, discordUserDisplay, message
d2m.msg=\u00A79[DISCORD] <{discordUserDisplay}>\u00A7r {message}
```

### Message Config

Located at: `YOUR_MINECRAFT_FOLDER/config/fabricDiscordBridge/message/MESSAGE_CONFIG_NAME.properties`

These allow you to configure the Discord messages send by the bot.

```properties
# The type of message to be sent. It can either be an embed or a text message. (Allowed Values: EMBED, TEXT)
type=TEXT
# Color of the embed in integer format. Set to any number smaller than 0 to use the default color.
embed.color=-1
# Title of the embed. Set to nothing to not show the title. (Can support placeholders)
embed.title=
# Message of the embed or text message. (Can support placeholders)
message=
```

## FAQ 📝

**Q. NeoForge support?**

A. Probably not, but you can try using **Sinytra Connector**

**Q. What about backports?**

A. Maybe, but nothing older than **1.21**

**Q. Why make this?**

A. Why not?

## Other 📚

**Libraries included in the jar:**
- [config-builder](https://github.com/henkelmax/config-builder)
- [JDA](https://github.com/discord-jda/JDA)
- [kotlin-stdlib](https://github.com/JetBrains/kotlin)
- [okio-jvm](https://github.com/square/okio)
- [trove4j](https://bitbucket.org/trove4j/trove/src/master/)
- [jackson-core](https://github.com/FasterXML/jackson-core)
- [jackson-databind](https://github.com/FasterXML/jackson-databind)
- [okhttp-jvm](https://github.com/square/okhttp)
- [nv-websocket-client](https://github.com/TakahikoKawasaki/nv-websocket-client)
- [commons-collections4](https://github.com/apache/commons-collections)