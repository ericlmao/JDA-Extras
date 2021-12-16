package dev.negativekb.api.commands.internal;

import dev.negativekb.api.commands.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;

public interface CommandMap {

    void registerGlobalCommand(@NotNull String name, @NotNull Command command);

    void registerServerCommand(@NotNull String serverID, @NotNull String name, @NotNull Command command);

    @NotNull
    Collection<Command> getGlobalCommands();

    @NotNull
    Collection<Command> getServerCommands(@NotNull String serverID);

    HashMap<String, Collection<Command>> getAllServerCommands();

}
