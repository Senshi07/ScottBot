package Senshi07.scottbot.utils.Tasks;

import Senshi07.scottbot.utils.music.GuildMusicManager;
import Senshi07.scottbot.utils.music.PlayerManager;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static Senshi07.scottbot.Scott.jda;

public class PlayerCleanup extends ScottTask
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerCleanup.class);

    private static final AtomicBoolean ACTIVE = new AtomicBoolean(true);
    public static final Map<Long, Long> VC_LAST_USED = new HashMap<>();
    private static final long CLEANUP_THRESHOLD = TimeUnit.MINUTES.toMillis(20);
    private static final int WEBSOCKET_THRESHOLD = 50;

    private static final AtomicInteger closedConnections = new AtomicInteger(0);

    public PlayerCleanup(String taskName)
    {
        super(taskName, TimeUnit.MINUTES.toMillis(20), TimeUnit.MINUTES.toMillis(5));
    }

    @Override
    public void run()
    {
        if (!ACTIVE.get())
        {
            cancel();
            return;
        }
        cleanupVoiceChannels();
    }

    private void cleanupVoiceChannels()
    {
        closedConnections.set(0);
        LOGGER.info("Checking for guilds with an inactive voice channel");
        final AtomicInteger totalGuilds = new AtomicInteger(0);
        final AtomicInteger totalVcs = new AtomicInteger(0);
        final AtomicInteger killedVcs = new AtomicInteger(0);

        jda.getGuildCache().forEach(guild -> {
            try {
                totalGuilds.incrementAndGet();

                if (guild != null && guild.getSelfMember() != null && guild.getSelfMember().getVoiceState() != null
                        && guild.getSelfMember().getVoiceState().getChannel() != null) {
                    totalVcs.incrementAndGet();

                    VoiceChannel vc = guild.getSelfMember().getVoiceState().getChannel().asVoiceChannel();

                    if (getHumansInChannel(vc) == 0) {
                        killedVcs.incrementAndGet();
                        cleanup(guild, PlayerManager.getMusicManager(vc.getGuild()), vc.getIdLong());
                    } else if (isPlayingMusic(vc)) {
                        VC_LAST_USED.put(vc.getIdLong(), System.currentTimeMillis());
                    } else {
                        if (!VC_LAST_USED.containsKey(vc.getIdLong())) {
                            VC_LAST_USED.put(vc.getIdLong(), System.currentTimeMillis());
                            return;
                        }

                        long lastUsed = VC_LAST_USED.get(vc.getIdLong());

                        if (System.currentTimeMillis() - lastUsed >= CLEANUP_THRESHOLD) {
                            killedVcs.incrementAndGet();
                            cleanup(guild, PlayerManager.getMusicManager(vc.getGuild()), vc.getIdLong());
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to check {} for inactive voice connection!", guild.getIdLong(), e);
            }
        });

        LOGGER.info("Checked {} guilds for inactive voice channels.", totalGuilds.get());
        LOGGER.info("Killed {} out of {} voice connections!", killedVcs.get(), totalVcs.get());
    }


    private void cleanup(Guild guild, GuildMusicManager manager, long id) {
        if (guild != null && guild.getSelfMember().getVoiceState() != null
                && guild.getSelfMember().getVoiceState().getChannel() != null
                && guild.getAudioManager().isConnected()
                && guild.getAudioManager().getConnectionStatus() == ConnectionStatus.CONNECTED
                && closedConnections.get() < WEBSOCKET_THRESHOLD) { // Cut off at threshold in-case we send too many
            guild.getAudioManager().closeAudioConnection();
            closedConnections.incrementAndGet();
        }

        assert guild != null;
        manager.audioPlayer.stopTrack();
        manager.scheduler.queue.clear();
        guild.getAudioManager().closeAudioConnection();
        VC_LAST_USED.remove(id);
    }

    private int getHumansInChannel(VoiceChannel channel) {
        int i = 0;
        for (Member m : channel.getMembers())
            if (!m.getUser().isBot())
                i++;
        return i;
    }

    private boolean isPlayingMusic(VoiceChannel vc) {
        GuildMusicManager manager = PlayerManager.getMusicManager(vc.getGuild());
        return manager != null && !manager.audioPlayer.isPaused() && manager.audioPlayer.getPlayingTrack() != null;
    }
}
