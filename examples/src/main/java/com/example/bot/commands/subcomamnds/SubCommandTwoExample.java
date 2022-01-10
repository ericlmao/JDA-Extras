package com.example.bot.commands.subcomamnds;

import dev.negativekb.api.commands.CommandInfo;
import dev.negativekb.api.commands.SubCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

@CommandInfo(name = "subcommand2", description = "SubCommand 2 of the Main Command")
public class SubCommandTwoExample extends SubCommand {
    @Override
    public void onCommand(SlashCommandEvent event) {
        event.reply("This is the SubCommand 2 of the Main Command!").setEphemeral(true).queue();
    }
}
