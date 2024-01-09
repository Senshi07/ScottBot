package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.cmd.Command;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command that calls the bot to the members voice channel.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class JoinCommand extends Command {

    public static String name = "join";
    public static String description = "Makes the bot join your Voice Channel";
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);

    @Override
    public void handle(CommandContext ctx) {

        final SlashCommandInteraction interaction = ctx.event().getInteraction();
        final GuildVoiceState selfVoiceState = ctx.getGuild().getSelfMember().getVoiceState();

        if (selfVoiceState.inAudioChannel()) {
            String alreadyConnectedResponse = ":no_entry: I am already connected to a voice channel.";
            interaction.reply(alreadyConnectedResponse).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            String memberNotConnectedResponse = ":no_entry: You must be connected to a voice channel to invoke this command.";
            interaction.reply(memberNotConnectedResponse).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        audioManager.openAudioConnection(memberChannel);
        audioManager.setSelfDeafened(true);

        String connectedResponse = ":notes: Connecting to `" + memberChannel.getName() + "`";
        interaction.reply(connectedResponse).queue();

        LOGGER.info("Joining '" + memberChannel.getName() + "' in the guild '" + ctx.getGuild().getName() + "'.");
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


