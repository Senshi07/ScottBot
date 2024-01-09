package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command that disconnects the bot from the voice channel.
 * @author Senshi
 * @since 1.0.2
 */
@SuppressWarnings("ConstantConditions")
public class DisconnectCommand extends Command {

    public static String name = "disconnect";
    public static String description = "Disconnects the bot from the voice channel.";
    private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectCommand.class);

    public void handle(CommandContext ctx) {

        final SlashCommandInteraction interaction = ctx.event().getInteraction();
        final GuildVoiceState selfVoiceState = ctx.getGuild().getSelfMember().getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            String botNotConnectedResponse = ":stop_sign: I am not connected to a voice channel.";
            interaction.reply(botNotConnectedResponse).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            String memberNotConnectedResponse = ":stop_sign: You must be connected to a voice channel to invoke this command.";
            interaction.reply(memberNotConnectedResponse).queue();
            return;
        }
        disconnect(ctx);
    }

    public static void disconnect(CommandContext ctx) {

        final SlashCommandInteraction interaction = ctx.event().getInteraction();
        final GuildVoiceState selfVoiceState = ctx.getGuild().getSelfMember().getVoiceState();
        final String voiceChannel = selfVoiceState.getChannel().getName();

        ctx.getGuild().getAudioManager().closeAudioConnection();

        String disconnectedResponse = ":telephone: Disconnected from `" + voiceChannel + "`";
        interaction.reply(disconnectedResponse).queue();

        LOGGER.info("Disconnected from '" + voiceChannel + "' in the guild '" + ctx.getGuild().getName() + "'.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelp() {
        return description;
    }
}

