package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command that resumes the currently playing track if it is paused.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class ResumeCommand extends Command
{

    public static String name = "resume";
    public static String description = "Resumes the music player";
    private static final Logger LOGGER = LoggerFactory.getLogger(ResumeCommand.class);

    @Override
    public void handle(CommandContext ctx) {

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

        if (audioPlayer.getPlayingTrack() == null)
        {
            String nothingPlayingResponse = ":stop_sign: Nothing is playing right now.";
            interaction.reply(nothingPlayingResponse).queue();
            return;
        }

        if (!musicManager.scheduler.player.isPaused())
        {
            String alreadyResumedResponse = ":play_pause: The Music Player is playing.";
            interaction.reply(alreadyResumedResponse).queue();
            return;
        }

        musicManager.scheduler.player.setPaused(false);
        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();
        String resumedResponse = ":play_pause: The Music Player was resumed.";
        interaction.reply(resumedResponse).queue();
        LOGGER.info("Player resumed in '" + memberChannel.getName() + "' in the guild '" + ctx.getGuild().getName() + "'.");
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
