package dev.negativekb.api.commands;

import dev.negativekb.api.commands.exception.InvalidCommandInfoException;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
public abstract class SubCommand {

    private final String name;
    private final String description;
    @Getter
    private List<String> aliases = new ArrayList<>();
    @Getter
    @Setter
    private Consumer<SubcommandData> data;

    public SubCommand() {
        if (!getClass().isAnnotationPresent(CommandInfo.class))
            throw new InvalidCommandInfoException();


        CommandInfo info = getClass().getAnnotation(CommandInfo.class);
        this.name = info.name();
        this.description = info.description();
        List<String> a = new ArrayList<>(Arrays.asList(info.aliases()));
        // There will always be an empty index even if no arguments are
        // set. So the way you identify if there are actual arguments in the command
        // is you check if the first index is empty.
        if (!a.get(0).isEmpty()) {
            this.aliases = a;
        }
    }

    public void runCommand(SlashCommandEvent event) {
        onCommand(event);
    }

    public abstract void onCommand(SlashCommandEvent event);
}