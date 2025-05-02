package dev.tehmanu.skybad;

import dev.tehmanu.skybad.commands.AdminLogoutCommand;
import dev.tehmanu.skybad.commands.LoginCommand;
import dev.tehmanu.skybad.commands.LogoutCommand;
import dev.tehmanu.skybad.commands.StopCommand;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TehManu
 * @since 02.05.2025
 */
@Getter
public class SkyBad {
    @Getter
    private static JDA jda;

    @Getter
    private static SkyBad instance;

    private final Set<Long> trackedEmployees = new HashSet<>();

    public static void main(String[] args) throws InterruptedException {
        instance = new SkyBad();
        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.ALL)
            .build();

        jda.awaitReady();

        final CommandListUpdateAction commands = jda.updateCommands();

        // stop command
        commands.addCommands(
            Commands.slash("stop", "Stoppt den Bot.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        // login command
        commands.addCommands(
            Commands.slash("login", "Beginnt die Messung der Arbeitszeit.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        // logout command
        commands.addCommands(
            Commands.slash("logout", "Beendet die Messung der Arbeitszeit.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        // adminlogout command
        commands.addCommands(
            Commands.slash("adminlogout", "Beendet die Messung der Arbeitszeit für einen Mitarbeiter.")
                .addOption(OptionType.USER, "mitarbeiter", "Der Mitarbeiter für den die Messung beendet werden soll.", true)
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        );

        commands.queue();

        jda.addEventListener(new StopCommand());
        jda.addEventListener(new LoginCommand());
        jda.addEventListener(new LogoutCommand());
        jda.addEventListener(new AdminLogoutCommand());
    }
}