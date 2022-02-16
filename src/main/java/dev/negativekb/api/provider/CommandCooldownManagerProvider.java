package dev.negativekb.api.provider;

import dev.negativekb.api.commands.SlashCommand;
import dev.negativekb.api.commands.CommandCooldownManager;
import dev.negativekb.api.commands.SlashSubCommand;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCooldownManagerProvider implements CommandCooldownManager {

    private final HashMap<String, HashMap<SlashCommand, Long>> commandCooldowns = new HashMap<>();
    private final HashMap<String, HashMap<SlashSubCommand, Long>> subCommandCooldowns = new HashMap<>();

    @Getter
    private static CommandCooldownManagerProvider instance;

    public CommandCooldownManagerProvider() {
        instance = this;

        new CooldownTask().start();
    }

    @Override
    public void addCooldown(@NotNull String id, @NotNull SlashCommand command, long duration) {
        HashMap<SlashCommand, Long> activeCooldowns = commandCooldowns.get(id);
        if (activeCooldowns == null)
            activeCooldowns = new HashMap<>();

        activeCooldowns.putIfAbsent(command, (System.currentTimeMillis() + duration));

        if (commandCooldowns.containsKey(id))
            commandCooldowns.replace(id, activeCooldowns);
        else
            commandCooldowns.put(id, activeCooldowns);
    }

    @Override
    public void addCooldown(@NotNull String id, @NotNull SlashSubCommand subCommand, long duration) {
        HashMap<SlashSubCommand, Long> activeCooldowns = subCommandCooldowns.get(id);
        if (activeCooldowns == null)
            activeCooldowns = new HashMap<>();

        activeCooldowns.putIfAbsent(subCommand, (System.currentTimeMillis() + duration));

        if (subCommandCooldowns.containsKey(id))
            subCommandCooldowns.replace(id, activeCooldowns);
        else
            subCommandCooldowns.put(id, activeCooldowns);
    }

    @Override
    public boolean checkCooldown(@NotNull String id, @NotNull SlashCommand command) {
        Optional<Map.Entry<String, HashMap<SlashCommand, Long>>> first = commandCooldowns.entrySet().stream()
                .filter(cooldownEntry -> cooldownEntry.getKey().equalsIgnoreCase(id)).findFirst();

        if (!first.isPresent())
            return false;

        Map.Entry<String, HashMap<SlashCommand, Long>> commandEntries = first.get();
        return commandEntries.getValue()
                .entrySet()
                .stream()
                .anyMatch(commandLongEntry ->
                        commandLongEntry.getKey().getName().equalsIgnoreCase(command.getName()));
    }

    @Override
    public boolean checkCooldown(@NotNull String id, @NotNull SlashSubCommand subCommand) {
        Optional<Map.Entry<String, HashMap<SlashSubCommand, Long>>> first = subCommandCooldowns.entrySet()
                .stream().filter(cooldownEntry -> cooldownEntry.getKey().equalsIgnoreCase(id)).findFirst();

        if (!first.isPresent())
            return false;

        Map.Entry<String, HashMap<SlashSubCommand, Long>> commandEntries = first.get();
        return commandEntries.getValue()
                .entrySet()
                .stream()
                .anyMatch(subCommandEntry -> subCommandEntry.getKey().getName().equalsIgnoreCase(subCommand.getName()));
    }

    @Override
    public long getCooldown(@NotNull String id, @NotNull SlashCommand command) {
        if (!checkCooldown(id, command))
            return 0;

        HashMap<SlashCommand, Long> active = commandCooldowns.get(id);
        return active.getOrDefault(command, 0L);
    }

    @Override
    public long getCooldown(@NotNull String id, @NotNull SlashSubCommand command) {
        if (!checkCooldown(id, command))
            return 0;

        HashMap<SlashSubCommand, Long> active = subCommandCooldowns.get(id);
        return active.getOrDefault(command, 0L);
    }

    private class CooldownTask extends Thread {
        @Override
        public void run() {
            int seconds = 1;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new Task(), 0, (1000L * seconds));
        }
    }

    private class Task extends TimerTask {

        @SneakyThrows
        @Override
        public void run() {
            HashMap<String, ArrayList<SlashCommand>> commandsToRemove = new HashMap<>();
            HashMap<String, ArrayList<SlashSubCommand>> subCommandsToRemove = new HashMap<>();

            commandCooldowns.forEach((user, cooldowns) -> {
                ArrayList<SlashCommand> removable = new ArrayList<>();
                cooldowns.entrySet()
                        .stream()
                        .filter(commandEntry -> System.currentTimeMillis() >= commandEntry.getValue())
                        .forEach(commandEntry -> removable.add(commandEntry.getKey()));

                commandsToRemove.putIfAbsent(user, removable);
            });

            subCommandCooldowns.forEach((user, cooldowns) -> {
                ArrayList<SlashSubCommand> removable = new ArrayList<>();
                cooldowns.entrySet()
                        .stream()
                        .filter(commandEntry -> System.currentTimeMillis() >= commandEntry.getValue())
                        .forEach(commandEntry -> removable.add(commandEntry.getKey()));

                subCommandsToRemove.putIfAbsent(user, removable);
            });

            commandsToRemove.forEach((user, commands) -> {
                HashMap<SlashCommand, Long> active = commandCooldowns.get(user);
                commands.forEach(active::remove);

                if (active.isEmpty())
                    commandCooldowns.remove(user);
                else
                    commandCooldowns.replace(user, active);
            });

            subCommandsToRemove.forEach((user, subCommands) -> {
                HashMap<SlashSubCommand, Long> active = subCommandCooldowns.get(user);
                subCommands.forEach(active::remove);

                if (active.isEmpty())
                    subCommandCooldowns.remove(user);
                else
                    subCommandCooldowns.replace(user, active);
            });

        }
    }
}
