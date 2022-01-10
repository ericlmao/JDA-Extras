package com.example.bot.commands.subcomamnds;

import dev.negativekb.api.commands.CommandInfo;
import dev.negativekb.api.commands.SubCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "subcommand1", description = "SubCommand 1 of the Main Command")
public class SubCommandOneExample extends SubCommand {
    @Override
    public void onCommand(SlashCommandEvent event) {
        event.reply("This is the SubCommand 1 of the Main Command!").setEphemeral(true).queue();
    }
}
