package dev.tehmanu.skybad.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class StopCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("stop")) {
            event.reply("Shutting down...").setEphemeral(true).queue();

            final JDA jda = event.getJDA();
            OkHttpClient client = jda.getHttpClient();
            client.connectionPool().evictAll();
            client.dispatcher().executorService().shutdown();
            jda.shutdown();
        }
    }
}