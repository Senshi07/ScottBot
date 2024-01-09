package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * A command that outputs the music player's queue of songs to the Text Channel.
 * @author Senshi
 * @since 1.1.7
 */
@SuppressWarnings("ConstantConditions")
public class QueueCommand extends Command
{

    public static String name = "queue";
    public static String description = "Outputs the list of songs to be played";
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinCommand.class);

    @Override
    public void handle(CommandContext ctx)
    {

        final SlashCommandInteraction interaction = ctx.event().getInteraction();
        final GuildVoiceState selfVoiceState = ctx.getGuild().getSelfMember().getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            String noConnectionToBotResponse = ":no_entry: You must be connected to my current voice channel to invoke this command.";
            interaction.reply(noConnectionToBotResponse).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty())
        {
            String nothingPlayingResponse = ":stop_sign: There is nothing playing at the moment.";
            interaction.reply(nothingPlayingResponse).queue();
            return;
        }
        final String Queue2 = queue.poll().getInfo().title;

        LOGGER.info(Queue2);


    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getHelp()
    {
        return description;
    }
}
