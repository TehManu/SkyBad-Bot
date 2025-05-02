package dev.tehmanu.skybad.commands;

import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class WorkingTimeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("workingtime")) {
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

        event.deferReply(true).queue();

        // TODO: collect working times from database
        final TextDisplay header = TextDisplay.create(MarkdownUtil.bold("SkyBad System -> Aktuelle Arbeitszeit des Mitarbeiters"));
        final TextDisplay body = TextDisplay.create("Du hast so eben für den Mitarbeiter " + employee.getAsMention() + " die aktuelle Arbeitszeit abgefragt."
            + "\nHiermit erhälst du einen Auszug der bisherigen gesammelten Arbeitszeit des Mitarbeiters.");
        final TextDisplay today = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit (heute)") + "\nN/A");
        final TextDisplay week = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit (Woche /So-Sa)") + "\nN/A");
        final TextDisplay total = TextDisplay.create(MarkdownUtil.bold("Arbeitszeit (gesamt)") + "\nN/A");
        final Container container = Container.of(header, body, today, week, total).withAccentColor(Color.CYAN);

        event.getHook().sendMessageComponents(container).useComponentsV2().queue();
    }
}