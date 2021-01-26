# IGSQBot Developer Edition
[![See latest version](https://img.shields.io/badge/download-0.0.1-blue)](https://github.com/IGSQ/IGSQ-Bot/releases)
[![GitHub license](https://img.shields.io/badge/license-GNU%20AGP-lightgrey)](https://github.com/IGSQ/IGSQ-Bot/tree/Dev/LICENSE)

## TODO
**Features that are still to be implemented**

### **COMPLETED**
 - [x] Per guild prefixing
 - [x] Project Refactor
 - [x] Move from Arrays to ArrayLists and Deprecate ArrayUtils#append / ArrayUtils#depend
 - [x] Input catching on commands
 - [x] Logging expansion (log voice channel stuff) (completed primitive)
 - [x] Change all command invokes to more proper names
 - [x] Change EmbedUtils to new prefix system (removed prefixes from errors)
 - [x] Move to InterfacedEventManager as to run event processing async
 - [x] Change cache to ExpiringMap(MessageID, Message)
 - [x] GuildConfigCache#close - unneeded
 - [x] Remove EventWaiter where possible in favour of MessageDataCache
 - [x] Move all events.commands to MessageDataCache
 - [x] Kill the callback hell
 - [x] Rework MessageCache to use hashmaps + refactor
 - [x] Remove raw Yaml links in place of caches / methods
 - [x] Fully implement the logger - needs review
 - [x] GUIGenerator further implementation (use callbacks / catch exceptions)
 - [x] Catch 2FA DMs
 - [x] Change CooldownHandler to use Strings vs Longs
 - [x] ReactionRoles
 - [x] Setters for GuildConfig
 - [x] Emote constants, success, failure
 - [x] Check verified role in LinkCommand
 - [x] Class#wipe for configuration classes - removes all configuration options
 - [x] Remove raw Database calls (Marked with TODO)
 - [x] Add Database#isOnline
 - [x] Catch empty bot.server option in SyncMinecraft
 - [x] Use MessageChannel where possible - only remaining on in CommandHandler (Required)
 - [x] Custom message class (also added user class)
 - [x] OnGuildRemove removing guild configuration (+ unavailableGuild)
 - [x] Filename Enum
 - [x] Check required tables in testDatabase
 - [x] Invite command
 - [x] Refactor events to not include 'Event'
 - [x] Switched to shardmanager
 - [x] ReactionRole#clear for a message
 - [x] Throw unsuportedoperation if datacache is used before type setting
 - [x] InstanceOf checks within command mapping
 - [x] Move Command functionality to abstract methods versus constructor calls for increased adaptability
 - [x] configuration.yml wrapper
 - [x] Catch different exceptions during login
 - [x] Catch invalid emojis in RR (regex) ('/[^\w$\x{0080}-\x{FFFF}]+/u')
 - [x] Change command#getPermission to command#canExecute, will implement various perm checks (maybe permcheck class)
 - [x] Condensed events
 - [x] Add developer permissions check (extend existing enum)
 - [x] Help command
 - [x] Remove EmbedGenerator in favour of JDAs EmbedBuilder
 - [x] Created new EmbedUtils to compensate for missing functionality
 - [x] Investigate caffeine > expiringmap (unneeded for now)
 - [x] Replaced concurrenthashmap with hashmap if possible
 - [x] IJsonCachable interface
 - [x] Muting (needs debugged) (fixed)
 - [x] Json punishment objects
 - [x] Consider final usage
 - [x] ctx isDeveloper method
 - [x] Restructure minecraft
 - [x] Objectify the IGSQBot class, make it instansiate listners and pass itself, will be a central object for everything
 - [x] Remove statics and use getInstance()
 - [x] Custom Task object to hold task info
 - [x] Refactor json objects to remove 'Json' prefix
 - [x] Report into JSON (punishment) with helper cache method
 - [x] GuildConfig to take something (Guild) as input to fetch the channels and check for null prior to getting (its far easier to check on the other side)
 - [x] Jsonify logging including prefix detection
 - [x] Use optionals for Json (didnt work out)
 - [x] Investigate https://github.com/twitter/twemoji (DB can handle emojis)
 - [x] Switch to postgres
 - [x] Change user loading to lazy (removed)
 - [x] Consider users table ^
 - [x] Redo Channel / Role / User / Emote/Emoji parsing - into CommandContext (JDA internal checker)
 - [x] Complex args parser ^
 - [x] Muting (Temp ban) (improve time parsing)
 - [x] Remove emoji-java
 - [x] Improve time parsing
 - [x] Insert time parsing into Parser
 - [x] Help menu
 - [x] Minecraft syncing (with preparedstatements)
 - [x] Minecraft syncing etc
 - [x] Message generation garbage (in test command for now)
 - [x] Child / Sub commands system
 - [x] Rewrite the event waiter (Deprecated)
 - [x] Info command (Info objects, UserInfo, BotInfo, RoleInfo, GuildInfo)
 - [x] Switch to longs for IDs (as far as i can tell)
 - [x] Register and removal of users (removed with guild)
 - [x] ReactionRoles
 - [x] Debug logging
 - [x] Voting (Done!)
 - [x] Change all database classes to static utils if possible
 - [x] Check '.' at the end of every command description
 - [x] Add sub commands to help menu.
 - [x] Catch config issues at startup
 - [x] Change mc syncing to events
 - [x] Make more granular errors with reaction role reaction adding (ErrorHandler)
 - [x] Verification
 - [x] Improve permission error embed
 
### **TeaEyes**
None!

### **arynxd**
 - [ ] Improve user feedback
 - [ ] Documentation
 - [ ] Move logging exceptions to errorhandler
 - [ ] **PREPARE FOR PR**

### **Future work**
 - [ ] Translation
 - [ ] XP system
 - [ ] Implement slash commands when they release
 - [ ] Restarting of the bot

## Known Issues
**Known bugs or issues, these will get patched in the next minor update.**
 
 - [x] GUIGenerator#menu & GUIGenerator#confirmation can take extended amounts of time to register (fixed but untested)
 - [x] discord_accounts table returning errors - fixed with additional error catching
 - [x] 2FA not functioning as intended - unintended feature
 - [x] Muting causing duplicate entries (fixed, was a Map issue)
 - [x] Resource loading fails (incorrect method used)
 - [x] Unchecked interaction check for reaction roles
 - [ ] Time parsing might have issues (vote)
 - [ ] Verification does not always work

## Building and Running

**In order to run this project, you need JDK 14 or higher.**

Cloning the project and running a Gradle build with the `build.gradle` will build the project into a JAR with all required dependencies.

When running the JAR, JDK 14's `java.exe` must be pointed to (see the example bat).

In order to connect to discord successfully, you must put a valid bot token (see https://discord.com/developers/applications)
in `token` within the `bot.cfg` file, within the config subdirectory, which is automatically generated on first launch.

**Example .bat file**

```
@echo off
"C:\Program Files\Java\jdk-14.0.2\bin\java.exe" -jar "your JAR here"
pause
```

## Developer Notes
None