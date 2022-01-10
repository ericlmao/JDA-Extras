package com.example.bot.commands.subcomamnds;

import dev.negativekb.api.commands.Command;
import dev.negativekb.api.commands.CommandInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "subcommandexample", description = "This command will have subcommands!")
public class SubCommandCommandExample extends Command {

    public SubCommandCommandExample() {
        addSubCommands(
                new SubCommandOneExample(),
                new SubCommandTwoExample()
        );
    }

    @Override
    public void onCommand(SlashCommandEvent slashCommandEvent) {
        // If you have subcommands, this function will never run.
        // So it can be left empty.
    }
}
