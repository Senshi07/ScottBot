package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.Config;
import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static Senshi07.scottbot.commands.music.NowPlayingCommand.calculateCompletionBar;

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

    @Override
    public void handle(CommandContext ctx)
    {
        // Embed Defaults
        String defaultTitle = ":notes: ScottBot Music Player";

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
        final AudioTrack playingTrack = musicManager.audioPlayer.getPlayingTrack();

        if (queue.isEmpty())
        {
            String nothingPlayingResponse = ":stop_sign: No songs in the queue.";
            interaction.reply(nothingPlayingResponse).queue();
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();
        final long durationMilliseconds = playingTrack.getDuration();
        final long runtimeMilliseconds = playingTrack.getPosition();
        final double completePercentage = (double) runtimeMilliseconds / durationMilliseconds * 100;

        final long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMilliseconds);
        final long durationSeconds = (TimeUnit.MILLISECONDS.toSeconds(durationMilliseconds) % 60);
        final String duration = String.format("%02d", durationMinutes) + ":" + String.format("%02d", durationSeconds);

        final long runtimeMinutes = TimeUnit.MILLISECONDS.toMinutes(runtimeMilliseconds);
        final long runtimeSeconds = (TimeUnit.MILLISECONDS.toSeconds(runtimeMilliseconds) % 60);
        final String runtime = String.format("%02d", runtimeMinutes) + ":" + String.format("%02d", runtimeSeconds);

        String completionBar = calculateCompletionBar(completePercentage);

        if (!queue.isEmpty() || playingTrack != null) {
            List<String> playlist = new ArrayList<>();

            playlist.add("Currently streaming:\n\n `" + info.title + " ` by `" + info.author + "`" +
                    "\n Link: " + info.uri +
                    "\nIn Voice Channel: `" + selfVoiceState.getChannel().getName() + "`" +
                    "\n" + runtime + completionBar + duration + "\n--------------------------------------------------\n\nThe Queue:\n");

            AtomicInteger i = new AtomicInteger(1);
            queue.forEach(audioTrack -> playlist.add("\n" + i.getAndIncrement() + ": `" + audioTrack.getInfo().title +
                    "` by `" + audioTrack.getInfo().author +
                    "` \nLink: " + audioTrack.getInfo().uri + "\n"));

            EmbedBuilder QueueOutputResponse = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription(playlist.toString().replaceAll("\\[|\\]", "").replaceAll(",", ""))
                    .setFooter(Config.get("VERSION"));

            interaction.replyEmbeds(QueueOutputResponse.build()).queue();
        }
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
