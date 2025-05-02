package dev.tehmanu.skybad;

import dev.tehmanu.skybad.commands.StopCommand;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * @author TehManu
 * @since 02.05.2025
 */
public class SkyBad {
    @Getter
    private static JDA jda;

    public static void main(String[] args) throws InterruptedException {
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

        commands.queue();

        jda.addEventListener(new StopCommand());
    }
}