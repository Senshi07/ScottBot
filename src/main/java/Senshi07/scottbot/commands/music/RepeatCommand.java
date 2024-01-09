package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

/**
 * A command that toggles whether the music player will repeat the currently playing track.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class RepeatCommand extends Command
{

    public static String name = "repeat";
    public static String description = "Loops the currently playing track.";

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
        final boolean repeating = !musicManager.scheduler.repeatEnabled;
        musicManager.scheduler.repeatEnabled = repeating;

        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = playingTrack.getInfo();

        if (repeating)
        {
            String repeatEnabledResponse = ":repeat_one: Repeating: `" + info.title + "` by `" + info.author + "`";
            interaction.reply(repeatEnabledResponse).queue();
        } else
        {
            String repeatDisabledResponse = ":repeat: Repeating disabled";
            interaction.reply(repeatDisabledResponse).queue();
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
