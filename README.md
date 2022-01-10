# JDA-Extras
Extra utilities for JDA

# Feature List
## Commands
Automatic command manager with the addition of the features provided
* Abstract Command API
* Abstract SubCommand API
* Command aliases
* SubCommand aliases
* Command Cooldowns
* SubCommand Cooldowns

## Other Utilities
* Time Translator
* Decimal Formatter

# Adding to Project
Repository
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
Dependency
```xml
<dependency>
    <groupId>com.github.NegativeKB</groupId>
    <artifactId>JDA-Extras</artifactId>
    <version>{VERSION}</version>
    <scope>compile</scope>
</dependency>
```
Build Configuration
```xml
<configuration>
  <relocations>
    <relocation>
      <pattern>dev.negativekb.api</pattern>
      <shadedPattern>{YOUR PACKAGE NAME}</shadedPattern>
    </relocation>
  </relocations>
</configuration>
```
