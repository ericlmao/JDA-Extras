package com.example.bot;

import com.example.bot.commands.SingleCommandExample;
import dev.negativekb.api.DiscordBot;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot extends DiscordBot {

    @SneakyThrows
    public Bot() {
        JDABuilder builder = JDABuilder.createDefault("BOT_TOKEN");
        initCommandMap(builder);

        // Server Command is Guild Commands!
        // Guild Commands will update automatically. This is primarily used
        // for development work on Commands.
        registerServerCommand("DEV_SERVER_ID", new SingleCommandExample());
        registerServerCommand("DEV_SERVER_ID", new SingleCommandExample());

        // Global Commands
        // Global Commands will take up to an hour to register
        // with Discord (for whatever reason).
        registerGlobalCommand(new SingleCommandExample());

        // Finalization
        // This part is required. You must awaitReady so
        // JDA will cache all Guilds so Server Commands will work.
        JDA jda = builder.build().awaitReady();
        initializeCommands(jda);
    }
}
