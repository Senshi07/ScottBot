# ScottBot Changelog
This document tracks changes made to the ScottBot project including changes made to 
the application's predecessor, MaxBot, which was used to create this application

### ScottBot v1.2.2-beta.2
- Implement Auto-disconnect function.
- Implement Task Scheduling framework

### ScottBot v1.2.2-beta.1
- Implement JDA-NAS library.

### ScottBot v1.2.1
- Implemented the Queue Command

### ScottBot v1.2.0
- Added Slash Commands Functionality to all command classes
- Removed redundant Help Command
- Removed redundant getUsage() method from all command classes
- Replaced sendMessage() method calls with interaction.reply() calls to improve responsiveness
- Updated to JDA v5
- Removed BotCommons library
- Some Music Commands now log their activity to the console via the LOGGER.info function

### ScottBot v1.1.7
- Removed Custom Prefixes

### ScottBot v1.1.6
- Converted Command interface to an abstract class
- Simplified Ping Command
- Moved Default presence to the .env file
- Replaced unnecessary Embed responses with Strings

### ScottBot v1.1.5
- Removed SetPrefix command in anticipation of slash commands in v1.2

### ScottBot v1.1.4
- Added README.md
- Added Apache 2.0 Licence

### ScottBot v1.1.3
- Updated SQLite and Logback Dependencies

### ScottBot v1.1.2
- Updated Logback, Dotenv, HikariCP, and SQLite dependencies

### ScottBot v1.1.1
- HatsuneRadio renamed to ScottBot on request from Server owner
- Major refactoring of directories

### HatsuneRadio v1.0.3
- PlayerManager.java bugfixes
- TrackScheduler.java bugfixes

### HatsuneRadio v1.0.2
- Added BotInfo defaults to Config.java
- Added Disconnect command
- Minor project refactoring

### HatsuneRadio v1.0.1
- MaxBot project forked to create HatsuneRadio
- Initial HatsuneRadio Commit
- Removed moderation commands (ban, kick, etc.)

## MaxBot Changelog

### 0.1.6-alpha - 10 August 2021
- Simplified Embeds
- Added mass ban option in the ban command
- Added mass kick option in the kick command
- Added JavaDocs to Commands and Command Interface
- Removed getAliases() method from Command interface

### 0.1.5-alpha - 29 July 2021
- Added Music Commands
- Implement LavaPlayer

### 0.1.4-alpha - 23 July 2021
- Implement a SQLite database for custom prefixes
- Added MaxBot Git webhook in MaxBot Discord Server
- Switch Syntax to Usage statement in help command
- Implement Gateway intents in the Main class
- Embeds now reference local string variables for titles, footers, etc

### 0.1.3-alpha - 19 July 2021
- Moved shutdown command to the console
- Added Custom Prefixes


### 0.1.2-alpha - 15 July 2021
- Added Help Command
- Added Ban Command
- Added Kick Command
- Added Embeds for all Bot Responses
- Removed REST ping from Ping Command

### 0.1.1-alpha - 13 July 2021
- Added Listener
- Added CommandManager
- Added CommandContext
- Added Ping Command
