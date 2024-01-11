package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.concurrent.TimeUnit;

/**
 * A command that outputs the currently playing track to the text channel.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class NowPlayingCommand extends Command
{

    @SuppressWarnings("SpellCheckingInspection")
    public static String name = "nowplaying";
    public static String description = "Shows the track currently being played.";

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
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();


        if (playingTrack == null)
        {
            String nothingPlayingResponse = ":stop_sign: There is nothing playing at the moment.";
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

        EmbedBuilder nowPlayingResponse = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Currently streaming:\n `" + info.title + " ` by `" + info.author + "`" +
                        "\n Link: " + info.uri +
                        "\nIn Voice Channel: `" + selfVoiceState.getChannel().getName() + "`" +
                        "\n" + runtime  + completionBar + duration);

        interaction.replyEmbeds(nowPlayingResponse.build()).queue();
    }

    public static String calculateCompletionBar(double completePercentage) {
        String completionBar = "";
        if (inRange(completePercentage, 0,9))
        {
            completionBar = " :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 10,19))
        {
            completionBar = " :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 20, 29))
        {
            completionBar = " :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 30,39))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 40, 49))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square:";
        }
        if (inRange(completePercentage, 50, 59))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 60, 69))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 70, 79))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 80, 89))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: :white_large_square: ";
        }
        if (inRange(completePercentage, 90, 100))
        {
            completionBar = " :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :blue_square: :white_large_square: ";
        }
        return completionBar;
    }

    public static boolean inRange(double value, double min, double max) {
        return (value >= min && value <= max);
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
