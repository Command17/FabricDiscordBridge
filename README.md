<img src="assets/icon.png" width="130" height="130" align="right"/>

# Fabric Discord Bridge

[![available_fabric](https://github.com/intergrav/devins-badges/raw/refs/heads/v3/assets/compact/supported/fabric_vector.svg)](https://fabricmc.net/)

[![requires_fabric_api](https://github.com/intergrav/devins-badges/raw/refs/heads/v3/assets/compact/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)
[![requires_p-api](assets/requires_p-api.svg)](https://modrinth.com/mod/placeholder-api)

---

## About 📖

A minecraft Fabric mod made with **[JDA](https://github.com/discord-jda/JDA)** allowing you to bridge your Minecraft and Discord chats.

I made this mod for private usage first but thought maybe someone else could use this too, so I made it public (only on Github!)

**Look at other alternatives first before choosing to use this one, please!**

## Configuration ⚙️

### Bot Configuration

Located at: `YOUR_MINECRAFT_FOLDER/bot.properties`

NOTE: COMMENTS DO NOT COME WITH THE CONFIG*

```properties
# Enables or disables the bot automatically starting when the server starts
autostart=true
# The token of your Discord bot
token=
```

### Mod Configuration

Located at: `YOUR_MINECRAFT_FOLDER/config/fabricdiscordbridge.properties`

*NOTE: WHAT IS SHOWN BELOW IS FORMATTED FOR LESS CLUTTER*

*ANOTHER NOTE: COMMENTS DO NOT COME WITH THE CONFIG*

```properties
# Enables or disables the discord bot
enabled=true

# Used to change the activity of the bot (like "playing Minecraft")
# A string of whatever you want
discord.activityName=
# The activity type (allowed values are: none, playing, listening, competing, watching, custom)
discord.activityType=none

# Discord channel ID your bot will use, needs to be a number
discord.channelId=0

# Enables or disables your bot sending an embed if a player dies
discord.playerDiedMsg.enabled=true
# Message your bot will show (allows for Discord Markdown formatting, allows for placeholders: message, player, playerDisplay, playerUuid)
discord.playerDiedMsg=**%message%\!**
# An integer value of a color used for the embed
# This below is a shade of red
discord.playerDiedMsg.colorInt=16733525

# Enables or disables your bot sending an embed if a player joins the server
discord.playerJoinMsg.enabled=true
# Message your bot will show (allows for Discord Markdown formatting, allows for placeholders: player, playerDisplay, playerUuid)
discord.playerJoinMsg=**%player% has joined the server\!**
# An integer value of a color used for the embed
# This below is a shade of yellow
discord.playerJoinMsg.colorInt=16777045

# Enables or disables your bot sending an embed if a player leaves the server
discord.playerLeaveMsg.enabled=true
# Message your bot will show (allows for Discord Markdown formatting, allows for placeholders: player, playerDisplay, playerUuid)
discord.playerLeaveMsg=**%player% has left the server\!**
# An integer value of a color used for the embed
# This below is a shade of yellow
discord.playerLeaveMsg.colorInt=16777045

# Enables or disables your bot sending an embed if a player completes an advancement
discord.playerRewardedAdvancementMsg.enabled=true
# Message your bot will show (allows for Discord Markdown formatting, allows for placeholders: player, playerDisplay, playerUuid, advancementTitle, advancementDescription, advancementType)
discord.playerRewardedAdvancementMsg=**%player%** completed **%advancementTitle%**\!
# An integer value of a color used for the embed
# This below is a shade of green
discord.playerRewardedAdvancementMsg.colorInt=5635925

# Enables or disables the Discord to Minecraft chat bridge
discordToMinecraftChat.enabled=true
# Max length a Discord message can be to be sent in Minecraft
discordToMinecraftChat.maxMsgLength=256
# Message your bot will show in Minecraft (allows for Quick Text formatting, allows for placeholders: user, displayUser, message)
discordToMinecraftChat.msg=<color aqua>[DISCORD]</color> <color blue><%displayUser%> %message%</color>

# Enables or disables the Minecraft to Discord chat bridge
minecraftToDiscordChat.enabled=true
# Message that will show in Minecraft when using the '/discordbridge send' command (allows for Quick Text formatting, allows for placeholders: player, playerDisplay, playerUuid, message)
minecraftToDiscordChat.commandMsg=<color gray>[%player% -> DISCORD]\: %message%</color>
# Message your bot will show (allows for Discord Markdown formatting, allows for placeholders: player, playerDisplay, playerUuid, message)
minecraftToDiscordChat.msg=**%player%\:** %message%
# If true, your bot will send player messages as embeds in discord
minecraftToDiscordChat.sendAsEmbed=false
# If true, you can only use '/discordbridge send' to let the bot send messages to Discord
minecraftToDiscordChat.sendWithCommandOnly=false
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
- [JDA](https://github.com/discord-jda/JDA)
- [kotlin-stdlib](https://github.com/JetBrains/kotlin)
- [okio-jvm](https://github.com/square/okio)
- [trove4j](https://bitbucket.org/trove4j/trove/src/master/)
- [jackson-core](https://github.com/FasterXML/jackson-core)
- [jackson-databind](https://github.com/FasterXML/jackson-databind)
- [okhttp-jvm](https://github.com/square/okhttp)
- [nv-websocket-client](https://github.com/TakahikoKawasaki/nv-websocket-client)
- [commons-collections4](https://github.com/apache/commons-collections)