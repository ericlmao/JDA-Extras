package dev.negativekb.api;

import dev.negativekb.api.commands.Command;
import dev.negativekb.api.commands.internal.CommandMap;
import dev.negativekb.api.listener.SlashCommandListener;
import dev.negativekb.api.provider.CommandMapProvider;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public abstract class JDABot {

    @Getter
    private CommandMap commandMap;
    public void initCommandMap(@NotNull JDABuilder builder) {
        commandMap = new CommandMapProvider();

        builder.addEventListeners(new SlashCommandListener(commandMap));
    }

    /**
     * Register a {@link Command} as a global command
     * @param command {@link Command} instance
     * @apiNote This may take up to an hour for Discord to register it!
     */
    public void registerGlobalCommand(@NotNull Command command) {
        commandMap.registerGlobalCommand(command.getName(), command);
    }

    /**
     * Register a {@link Command} as a server command
     * @param serverID {@link Guild} ID
     * @param command {@link Command} instance
     * @apiNote This should register almost instantly!
     */
    public void registerServerCommand(@NotNull String serverID, @NotNull Command command) {
        commandMap.registerServerCommand(serverID, command.getName(), command);
    }

    /**
     * Initalize all the commands in the {@link CommandMap} to Discord
     * @apiNote This should be called after {@link JDABuilder#build()#awaitReady()}
     * @param jda {@link JDA} instance
     */
    @SuppressWarnings("all")
    public void initializeCommands(@NotNull JDA jda) {
        // Global Commands
        Collection<Command> globalCommands = commandMap.getGlobalCommands();
        CommandListUpdateAction commands = jda.updateCommands();

        globalCommands.forEach(command -> {
            if (!command.getAliases().isEmpty()) {
                command.getAliases().forEach(name -> {
                    CommandData commandData = new CommandData(name, command.getDescription());
                    Optional.ofNullable(command.getData()).ifPresent(data -> data.accept(commandData));
                    if (!command.getSubCommands().isEmpty()) {
                        command.getSubCommands().forEach(subCommand -> {
                            if (!subCommand.getAliases().isEmpty()) {
                                subCommand.getAliases().forEach(subName -> {
                                    SubcommandData subcommandData = new SubcommandData(subName, subCommand.getDescription());
                                    Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                                    commandData.addSubcommands(subcommandData);
                                });
                            }

                            SubcommandData subcommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                            Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                            commandData.addSubcommands(subcommandData);
                        });
                    }
                    System.out.println("[Command Registry] Registered Global Command `" + commandData.getName() +"`");
                    commands.addCommands(commandData);
                });
            }

            CommandData commandData = new CommandData(command.getName(), command.getDescription());
            Optional.ofNullable(command.getData()).ifPresent(data -> data.accept(commandData));
            if (!command.getSubCommands().isEmpty()) {
                command.getSubCommands().forEach(subCommand -> {
                    if (!subCommand.getAliases().isEmpty()) {
                        subCommand.getAliases().forEach(name -> {
                            SubcommandData subcommandData = new SubcommandData(name, subCommand.getDescription());
                            Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                            commandData.addSubcommands(subcommandData);
                        });
                    }

                    SubcommandData subcommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                    Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                    commandData.addSubcommands(subcommandData);
                });
            }
            System.out.println("[Command Registry] Registered Global Command `" + commandData.getName() +"`");
            commands.addCommands(commandData);
        });

        commands.queue();

        // Server Bound Commands
        commandMap.getAllServerCommands().entrySet().stream().filter(serverEntry -> jda.getGuildById(serverEntry.getKey()) != null).forEach(serverEntry -> {
            Guild guild = jda.getGuildById(serverEntry.getKey());
            assert guild != null;
            CommandListUpdateAction guildCommands = guild.updateCommands();

            Collection<Command> serverCommands = serverEntry.getValue();
            serverCommands.forEach(command -> {
                if (!command.getAliases().isEmpty()) {
                    command.getAliases().forEach(name -> {
                        CommandData commandData = new CommandData(name, command.getDescription());
                        Optional.ofNullable(command.getData()).ifPresent(data -> data.accept(commandData));
                        if (!command.getSubCommands().isEmpty()) {
                            command.getSubCommands().forEach(subCommand -> {
                                if (!subCommand.getAliases().isEmpty()) {
                                    subCommand.getAliases().forEach(subName -> {
                                        SubcommandData subcommandData = new SubcommandData(subName, subCommand.getDescription());
                                        Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                                        commandData.addSubcommands(subcommandData);
                                    });
                                }

                                SubcommandData subcommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                                Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                                commandData.addSubcommands(subcommandData);
                            });
                        }
                        System.out.println("[Command Registry] Registered Server Command `" + commandData.getName()
                                + "` to Guild `" + guild.getName() + "`");
                        guildCommands.addCommands(commandData);
                    });
                }

                CommandData commandData = new CommandData(command.getName(), command.getDescription());
                Optional.ofNullable(command.getData()).ifPresent(data -> data.accept(commandData));
                if (!command.getSubCommands().isEmpty()) {
                    command.getSubCommands().forEach(subCommand -> {
                        if (!subCommand.getAliases().isEmpty()) {
                            subCommand.getAliases().forEach(name -> {
                                SubcommandData subcommandData = new SubcommandData(name, subCommand.getDescription());
                                Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                                commandData.addSubcommands(subcommandData);
                            });
                        }

                        SubcommandData subcommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                        Optional.ofNullable(subCommand.getData()).ifPresent(data -> data.accept(subcommandData));
                        commandData.addSubcommands(subcommandData);
                    });
                }
                System.out.println("[Command Registry] Registered Server Command `" + commandData.getName()
                        + "` to Guild `" + guild.getName() + "`");
                guildCommands.addCommands(commandData);
            });

            guildCommands.queue();

        });

    }
}
