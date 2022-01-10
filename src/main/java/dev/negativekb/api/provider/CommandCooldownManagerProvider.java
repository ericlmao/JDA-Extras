package dev.negativekb.api.provider;

import dev.negativekb.api.commands.Command;
import dev.negativekb.api.commands.CommandCooldownManager;
import dev.negativekb.api.commands.SubCommand;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandCooldownManagerProvider implements CommandCooldownManager {

    private final HashMap<String, HashMap<Command, Long>> commandCooldowns = new HashMap<>();
    private final HashMap<String, HashMap<SubCommand, Long>> subCommandCooldowns = new HashMap<>();

    @Getter
    private static CommandCooldownManagerProvider instance;

    public CommandCooldownManagerProvider() {
        instance = this;

        new CooldownTask().start();
    }

    @Override
    public void addCooldown(@NotNull String id, @NotNull Command command, long duration) {
        HashMap<Command, Long> activeCooldowns = commandCooldowns.get(id);
        if (activeCooldowns == null)
            activeCooldowns = new HashMap<>();

        activeCooldowns.putIfAbsent(command, (System.currentTimeMillis() + duration));

        if (commandCooldowns.containsKey(id))
            commandCooldowns.replace(id, activeCooldowns);
        else
            commandCooldowns.put(id, activeCooldowns);
    }

    @Override
    public void addCooldown(@NotNull String id, @NotNull SubCommand subCommand, long duration) {
        HashMap<SubCommand, Long> activeCooldowns = subCommandCooldowns.get(id);
        if (activeCooldowns == null)
            activeCooldowns = new HashMap<>();

        activeCooldowns.putIfAbsent(subCommand, (System.currentTimeMillis() + duration));

        if (subCommandCooldowns.containsKey(id))
            subCommandCooldowns.replace(id, activeCooldowns);
        else
            subCommandCooldowns.put(id, activeCooldowns);
    }

    @Override
    public boolean checkCooldown(@NotNull String id, @NotNull Command command) {
        Optional<Map.Entry<String, HashMap<Command, Long>>> first = commandCooldowns.entrySet().stream()
                .filter(cooldownEntry -> cooldownEntry.getKey().equalsIgnoreCase(id)).findFirst();

        if (!first.isPresent())
            return false;

        Map.Entry<String, HashMap<Command, Long>> commandEntries = first.get();
        return commandEntries.getValue()
                .entrySet()
                .stream()
                .anyMatch(commandLongEntry ->
                        commandLongEntry.getKey().getName().equalsIgnoreCase(command.getName()));
    }

    @Override
    public boolean checkCooldown(@NotNull String id, @NotNull SubCommand subCommand) {
        Optional<Map.Entry<String, HashMap<SubCommand, Long>>> first = subCommandCooldowns.entrySet()
                .stream().filter(cooldownEntry -> cooldownEntry.getKey().equalsIgnoreCase(id)).findFirst();

        if (!first.isPresent())
            return false;

        Map.Entry<String, HashMap<SubCommand, Long>> commandEntries = first.get();
        return commandEntries.getValue()
                .entrySet()
                .stream()
                .anyMatch(subCommandEntry -> subCommandEntry.getKey().getName().equalsIgnoreCase(subCommand.getName()));
    }

    @Override
    public long getCooldown(@NotNull String id, @NotNull Command command) {
        if (!checkCooldown(id, command))
            return 0;

        HashMap<Command, Long> active = commandCooldowns.get(id);
        return active.getOrDefault(command, 0L);
    }

    @Override
    public long getCooldown(@NotNull String id, @NotNull SubCommand command) {
        if (!checkCooldown(id, command))
            return 0;

        HashMap<SubCommand, Long> active = subCommandCooldowns.get(id);
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
            HashMap<String, ArrayList<Command>> commandsToRemove = new HashMap<>();
            HashMap<String, ArrayList<SubCommand>> subCommandsToRemove = new HashMap<>();

            commandCooldowns.forEach((user, cooldowns) -> {
                ArrayList<Command> removable = new ArrayList<>();
                cooldowns.entrySet()
                        .stream()
                        .filter(commandEntry -> System.currentTimeMillis() >= commandEntry.getValue())
                        .forEach(commandEntry -> removable.add(commandEntry.getKey()));

                commandsToRemove.putIfAbsent(user, removable);
            });

            subCommandCooldowns.forEach((user, cooldowns) -> {
                ArrayList<SubCommand> removable = new ArrayList<>();
                cooldowns.entrySet()
                        .stream()
                        .filter(commandEntry -> System.currentTimeMillis() >= commandEntry.getValue())
                        .forEach(commandEntry -> removable.add(commandEntry.getKey()));

                subCommandsToRemove.putIfAbsent(user, removable);
            });

            commandsToRemove.forEach((user, commands) -> {
                HashMap<Command, Long> active = commandCooldowns.get(user);
                commands.forEach(active::remove);

                if (active.isEmpty())
                    commandCooldowns.remove(user);
                else
                    commandCooldowns.replace(user, active);
            });

            subCommandsToRemove.forEach((user, subCommands) -> {
                HashMap<SubCommand, Long> active = subCommandCooldowns.get(user);
                subCommands.forEach(active::remove);

                if (active.isEmpty())
                    subCommandCooldowns.remove(user);
                else
                    subCommandCooldowns.replace(user, active);
            });

        }
    }
}
