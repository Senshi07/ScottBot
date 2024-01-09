package Senshi07.scottbot.utils.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads music to the guild's music player queue
 * @author Senshi
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class PlayerManager
{

    private static PlayerManager INSTANCE;
    private static Map<Long, GuildMusicManager> musicManager;
    private static AudioPlayerManager playerManager;

    public PlayerManager()
    {
        musicManager = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static GuildMusicManager getMusicManager(Guild guild)
    {
        return musicManager.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(playerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandInteraction interaction, String trackUrl)
    {
        final GuildMusicManager musicManager = getMusicManager(interaction.getGuild());
        final MessageChannelUnion channel = interaction.getChannel();

        // Embed Defaults
        String defaultTitle = ":notes: ScottBot Music Player";


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                musicManager.scheduler.queue(track);

                EmbedBuilder trackLoadedResponse = new EmbedBuilder()
                        .setTitle(defaultTitle)
                        .setDescription("Adding to queue: \n`" + track.getInfo().title + "` by `" + track.getInfo().author
                                + "`\n" + "Link: " + track.getInfo().uri);

                interaction.replyEmbeds(trackLoadedResponse.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                if (isUrl(trackUrl)) {
                    for (final AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }
                    EmbedBuilder playlistLoadedResponse = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("Adding to queue: `" + tracks.size() + "` tracks from playlist `" + playlist.getName() + "`\n"
                                    + "Link: " + trackUrl);

                    interaction.replyEmbeds(playlistLoadedResponse.build()).queue();
                    return;
                }

                // Takes the first item in the list if the argument is a search query.
                // This function is necessary due to a bug where search queries are treated as playlists.
                if (!isUrl(trackUrl)) {
                    final AudioTrack audioTrack = playlist.getTracks().get(0);
                    musicManager.scheduler.queue(audioTrack);

                    EmbedBuilder trackLoaded = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("Adding to queue: `" + audioTrack.getInfo().title + "` by `" + audioTrack.getInfo().author + "`\n"
                                    + "Link: " + audioTrack.getInfo().uri);

                    interaction.replyEmbeds(trackLoaded.build()).queue();
                }
            }

            @Override
            public void noMatches() {
                String noMatchesForSearchResponse = ":stop_sign: Could not find the song requested. \nIf you used the search function, try sending a URL instead.";
                String noMatchesForURLResponse = ":stop_sign: Could not find the song requested.";

                if (!isUrl(trackUrl)) {
                    interaction.replyFormat(noMatchesForSearchResponse).queue();
                } else {
                    interaction.replyFormat(noMatchesForURLResponse).queue();
                }
            }

            @Override
            public void loadFailed(FriendlyException e)
            {
                String loadFailedResponse = ":stop_sign: Load failed. Please try again.";
                channel.sendMessage(loadFailedResponse).queue();
            }
        });
    }

    public static PlayerManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

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
