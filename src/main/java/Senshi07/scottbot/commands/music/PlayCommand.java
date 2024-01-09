package Senshi07.scottbot.commands.music;

import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;
import Senshi07.scottbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A command that adds a track or playlist of tracks to the queue
 * and calls the bot to the members voice channel if not already connected.
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class PlayCommand extends Command
{

    public static String name = "play";
    public static String description = "Adds a song to the queue";
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayCommand.class);

    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel = ctx.getChannel();
        final SlashCommandInteraction interaction = ctx.event().getInteraction();
        final GuildVoiceState selfVoiceState = ctx.getGuild().getSelfMember().getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();


        if (ctx.args().isEmpty())
        {
            String missingArgsResponse = ":no_entry: Missing Arguments\nUsage: `/play [URL/Search]`";
            interaction.reply(missingArgsResponse).queue();
            return;
        }


        if (!memberVoiceState.inAudioChannel())
        {
            String notConnectedToVCResponse = ":no_entry: You must be connected to a voice channel to invoke this command.";
            interaction.reply(notConnectedToVCResponse).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel().asVoiceChannel();

        if (!selfVoiceState.inAudioChannel())
        {
            audioManager.openAudioConnection(memberChannel);
            audioManager.setSelfDeafened(true);

            String connectedResponse = ":notes: Connecting to `" + memberChannel.getName() + "`";
            channel.sendMessage(connectedResponse).queue();
            LOGGER.info("Joining '" + memberChannel.getName() + "' in the guild '" + ctx.getGuild().getName() + "'.");
        }


        String query = String.join(" ", ctx.args());
        if (!isUrl(query))
        {
            query = "ytsearch:" + query;
            LOGGER.info(query);
        }

        PlayerManager.getInstance().loadAndPlay(interaction, query);

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


    /**
     * Checks if a string conforms to a URL syntax.
     * @param url The String being tested
     * @return {@code true} if the param is a URL, otherwise false
     */
    private boolean isUrl(String url)
    {
        try
        {
            new URI(url);
            return true;
        } catch (URISyntaxException e)
        {
            return false;
        }
    }
}
