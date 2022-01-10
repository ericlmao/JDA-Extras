package com.example.bot.commands;

import dev.negativekb.api.commands.Command;
import dev.negativekb.api.commands.CommandInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "mycommand", aliases = {"mycommand1", "myaliascommand"}, description = "A Simple Command with Alises!")
public class SingleAliasCommandExample extends Command {

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.reply("This will work with any alias you state!").setEphemeral(true).queue();
    }
}
