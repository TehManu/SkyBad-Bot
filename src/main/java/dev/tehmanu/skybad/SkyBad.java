package dev.tehmanu.skybad;

import dev.tehmanu.skybad.commands.*;
import dev.tehmanu.skybad.datasource.PostgreSQLDataSource;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author TehManu
 * @since 02.05.2025
 */
@Getter
public class SkyBad {
    @Getter
    private static SkyBad instance;

    private final JDA jda;
    private final PostgreSQLDataSource dataSource;

    private final Set<Long> trackedEmployees = new HashSet<>();
    private final ExecutorService postgresExecutor = Executors.newCachedThreadPool();

    private SkyBad() throws InterruptedException {
        instance = this;
        jda = JDABuilder.createDefault(System.getenv("TOKEN"))
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .setChunkingFilter(ChunkingFilter.ALL)
            .build();

        this.dataSource = new PostgreSQLDataSource();

        jda.awaitReady();

        jda.updateCommands()
            // stop command
            .addCommands(Commands.slash("stop", "Stoppt den Bot.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED))
            // login command
            .addCommands(Commands.slash("login", "Beginnt die Messung der Arbeitszeit.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED))
            // logout command
            .addCommands(Commands.slash("logout", "Beendet die Messung der Arbeitszeit.")
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED))
            // adminlogout command
            .addCommands(Commands.slash("adminlogout", "Beendet die Messung der Arbeitszeit für einen Mitarbeiter.")
                .addOption(OptionType.USER, "mitarbeiter", "Der Mitarbeiter für den die Messung beendet werden soll.", true)
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED))
            // workingtime command
            .addCommands(Commands.slash("workingtime", "Zeigt die aktuelle Arbeitszeit des Mitarbeiters an.")
                .addOption(OptionType.USER, "mitarbeiter", "Mitarbeiter für den die Arbeitszeit abgefragt werden soll.", true)
                .setContexts(InteractionContextType.GUILD)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED))
            .queue();

        jda.addEventListener(new StopCommand());
        jda.addEventListener(new LoginCommand());
        jda.addEventListener(new LogoutCommand());
        jda.addEventListener(new AdminLogoutCommand());
        jda.addEventListener(new WorkingTimeCommand());
    }

    public static void main(String[] args) throws InterruptedException {
        new SkyBad();
    }
}