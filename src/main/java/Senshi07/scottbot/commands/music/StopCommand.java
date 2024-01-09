package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.lavaplayer.GuildMusicManager;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A command that stops the currently playing track and clears the queue.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class StopCommand extends Command
{

    public static String name = "stop";
    public static String description = "Stops the current track and clears the queue.";
    private static final Logger LOGGER = LoggerFactory.getLogger(StopCommand.class);

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

        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();
        final GuildMusicManager musicManager = PlayerManager.getMusicManager(ctx.getGuild());
        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        String playerStoppedResponse = ":stop_button: The music player was stopped and the queue was cleared.";
        interaction.reply(playerStoppedResponse).queue();
        LOGGER.info("Player stopped and cleared in '" + memberChannel.getName() + "' in the guild '" + ctx.getGuild().getName() + "'.");
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
