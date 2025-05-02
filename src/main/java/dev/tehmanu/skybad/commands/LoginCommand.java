package dev.tehmanu.skybad.commands;

import dev.tehmanu.skybad.SkyBad;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class LoginCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("login")) {
            return;
        }

        final Member employee = event.getMember();
        if (employee == null || employee.getUser().isBot()) {
            return;
        }

        if (!SkyBad.getInstance().getTrackedEmployees().add(employee.getIdLong())) {
            final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Login fehlgeschlagen!"));
            final TextDisplay body = TextDisplay.create("Du bist bereits eingeloggt.");
            final Container container = Container.of(header, body).withAccentColor(Color.DARK_GRAY);

            event.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
            return;
        }

        // TODO: start time tracking
        final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Login erfolgreich!"));
        final TextDisplay body = TextDisplay.create("Du hast dich erfolgreich eingeloggt. Das bedeutet, dass deine Arbeitszeit ab sofort gemessen"
            + "\nwird und du dich auf dem SkyBad Plot befindest."
            + "\nWir wünschen dir eine erfolgreiche Schicht!");
        final TextDisplay footer = TextDisplay.create(MarkdownUtil.bold("Wichtig für deine Schicht")
            + "\nUm deine Arbeitszeit gelten zu lassen, denke daran, dich am Ende deiner Schicht wieder"
            + "\nauszuloggen. Bedenke zusätzlich, dass du deinen Tätigkeiten nachgehen sollst.");
        final Container container = Container.of(header, body, footer).withAccentColor(Color.GREEN);

        event.replyComponents(container).useComponentsV2().queue();
        sendLoginMessageToManagement(employee, event.getChannel().asTextChannel());
    }

    // TODO: This needs to be adjusted to the right channel later on
    private void sendLoginMessageToManagement(Member employee, TextChannel channel) {
        final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("Neuer Login gemeldet"));
        final TextDisplay body = TextDisplay.create("Der Mitarbeiter " + employee.getAsMention() + " hat sich so eben erfolgreich eingeloggt.");
        final TextDisplay footer = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit-Beginn:")
            + "\n" + TimeFormat.DATE_TIME_LONG.format(System.currentTimeMillis()));
        final Container container = Container.of(header, body, footer).withAccentColor(Color.GREEN);

        channel.sendMessageComponents(container).useComponentsV2().queue();
    }
}