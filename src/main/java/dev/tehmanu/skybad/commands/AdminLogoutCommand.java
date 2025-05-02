package dev.tehmanu.skybad.commands;

import dev.tehmanu.skybad.SkyBad;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class AdminLogoutCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("adminlogout")) {
            return;
        }

        final Member manager = event.getMember();
        if (manager == null || manager.getUser().isBot()) {
            return;
        }

        final Member employee = event.getOptionsByType(OptionType.USER).getFirst().getAsMember();
        if (employee == null || employee.getUser().isBot()) {
            return;
        }

        if (manager.getIdLong() == employee.getIdLong()) {
            final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Adminlogout fehlgeschlagen!"));
            final TextDisplay body = TextDisplay.create("Bitte verwende /logout um dich auszuloggen.");
            final Container container = Container.of(header, body).withAccentColor(Color.DARK_GRAY);

            event.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
            return;
        }

        if (!SkyBad.getInstance().getTrackedEmployees().remove(employee.getIdLong())) {
            final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Adminlogout fehlgeschlagen!"));
            final TextDisplay body = TextDisplay.create("Der Mitarbeiter " + employee.getAsMention() + " ist nicht eingeloggt.");
            final Container container = Container.of(header, body).withAccentColor(Color.DARK_GRAY);

            event.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
            return;
        }

        // TODO: untrack working time
        final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Adminlogout erfolgreich!"));
        final TextDisplay body = TextDisplay.create("Der Adminlogout von " + employee.getAsMention() + " wurde erfolgreich durchgeführt. Die"
            + "\nMessung der Arbeitszeit des Mitarbeiters wurde gestoppt.");
        // TODO: replace with actual login time
        final TextDisplay loginTime = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit-Beginn:") + "\nN/A");
        final TextDisplay logoutTime = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit-Ende:")
            + "\n" + TimeFormat.DATE_TIME_LONG.format(System.currentTimeMillis()));
        // TODO: replace with working time
        final TextDisplay workingTime = TextDisplay.create(MarkdownUtil.bold("Gesammelte Arbeitszeit in dieser Schicht:") + "\nN/A");

        final Container container = Container.of(header, body, loginTime, logoutTime, workingTime).withAccentColor(Color.YELLOW);

        event.replyComponents(container).useComponentsV2().setEphemeral(true).queue();
    }
}