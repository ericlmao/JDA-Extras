# JDA-Extras
This library was designed to provide a more abstract experience to JDA's SlashCommand implementation. Please read the feature list for all features in this project, and please view the examples module to see how to utilize this project for your own personal use!

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
