package dev.negativekb.api.commands.internal;

import dev.negativekb.api.commands.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

/**
 * Command Map Module
 *
 * This is responsible for handling and registering all
 * commands using the {@link SlashCommand} abstract class
 */
public interface CommandMap {

    /**
     * Register a Global Command
     * @param name {@link SlashCommand} Name
     * @param command {@link SlashCommand} instance
     */
    void registerGlobalCommand(@NotNull String name, @NotNull SlashCommand command);

    /**
     * Register a {@link SlashCommand} to a {@link Guild}
     * @param serverID ID ({@link String}) of the {@link Guild}
     * @param name {@link SlashCommand} name
     * @param command {@link SlashCommand} instance
     */
    void registerServerCommand(@NotNull String serverID, @NotNull String name, @NotNull SlashCommand command);

    @NotNull
    Collection<SlashCommand> getGlobalCommands();

    @NotNull
    Collection<SlashCommand> getServerCommands(@NotNull String serverID);

    HashMap<String, Collection<SlashCommand>> getAllServerCommands();

}
