package com.example.bot.commands;

import dev.negativekb.api.commands.Command;
import dev.negativekb.api.commands.CommandInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Objects;

@CommandInfo(name = "mycommand", description = "An example command for this project!")
public class SingleCommandExample extends Command {

    public SingleCommandExample() {
        setCooldownInSeconds(5); // Command Cooldown Integration

        // The setData function will be used for things
        // such as Options
        setData(data -> data.addOption(OptionType.STRING, "message", "The message", true));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        // Since the Option is required we will not need a null check.
        String message = Objects.requireNonNull(event.getOption("message")).getAsString();

        event.reply("Your message is: " + message).setEphemeral(true).queue();
    }
}
