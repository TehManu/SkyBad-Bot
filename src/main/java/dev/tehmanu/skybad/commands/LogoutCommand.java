package dev.tehmanu.skybad.commands;

import dev.tehmanu.skybad.SkyBad;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class LogoutCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("logout")) {
            return;
        }

        final Member employee = event.getMember();
        if (employee == null || employee.getUser().isBot()) {
            return;
        }

        if (!SkyBad.getInstance().getTrackedEmployees().remove(employee.getIdLong())) {
            final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Logout fehlgeschlagen!"));
            final TextDisplay body = TextDisplay.create("Du bist nicht eingeloggt.");
            final Container container = Container.of(header, body).withAccentColor(Color.DARK_GRAY);

            event.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
            return;
        }

        // TODO: stop tracking working time
        final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Logout erfolgreich!"));
        final TextDisplay body = TextDisplay.create("Du hast dich erfolgreich ausgeloggt. Das bedeutet, dass deine Arbeitszeit ab sofort nicht"
            + "\nmehr gemessen wird und du deine Schicht offiziell beendet hast.");
        final TextDisplay footer = TextDisplay.create(MarkdownUtil.bold("Wichtig für dein Schichtende")
            + "\nBitte denke daran, deine Mitarbeiter-Rüstung in deinen Spind zu legen und deine Kassen-\nShulker abzugeben.");
        final Container container = Container.of(header, body, footer).withAccentColor(Color.RED);

        event.replyComponents(container).useComponentsV2().queue();
    }
}