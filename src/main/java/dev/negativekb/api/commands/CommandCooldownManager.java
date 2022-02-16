package dev.negativekb.api.commands;

import org.jetbrains.annotations.NotNull;

public interface CommandCooldownManager {

    void addCooldown(@NotNull String id, @NotNull SlashCommand command, long duration);

    void addCooldown(@NotNull String id, @NotNull SlashSubCommand subCommand, long duration);

    boolean checkCooldown(@NotNull String id, @NotNull SlashCommand command);

    boolean checkCooldown(@NotNull String id, @NotNull SlashSubCommand subCommand);

    long getCooldown(@NotNull String id, @NotNull SlashCommand command);

    long getCooldown(@NotNull String id, @NotNull SlashSubCommand command);

}
