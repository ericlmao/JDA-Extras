package dev.negativekb.api.commands.internal;

import dev.negativekb.api.commands.Command;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

/**
 * Command Map Module
 *
 * This is responsible for handling and registering all
 * commands using the {@link Command} abstract class
 */
public interface CommandMap {

    /**
     * Register a Global Command
     * @param name {@link Command} Name
     * @param command {@link Command} instance
     */
    void registerGlobalCommand(@NotNull String name, @NotNull Command command);

    /**
     * Register a {@link Command} to a {@link Guild}
     * @param serverID ID ({@link String}) of the {@link Guild}
     * @param name {@link Command} name
     * @param command {@link Command} instance
     */
    void registerServerCommand(@NotNull String serverID, @NotNull String name, @NotNull Command command);

    @NotNull
    Collection<Command> getGlobalCommands();

    @NotNull
    Collection<Command> getServerCommands(@NotNull String serverID);

    HashMap<String, Collection<Command>> getAllServerCommands();

}
