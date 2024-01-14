package Senshi07.scottbot;

import Senshi07.scottbot.utils.Tasks.PlayerCleanup;
import Senshi07.scottbot.utils.cmd.CommandManager;
import Senshi07.scottbot.utils.cmd.Listener;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * The main method class that starts the bot and defines its startup parameters.
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class Scott
{

    private static final Logger LOGGER = LoggerFactory.getLogger(Scott.class);
    public static JDA jda;

    public Scott() throws  InterruptedException
    {
        // NOTE TO SELF: SWITCH ENVIRONMENT VARIABLE "DEV_TOKEN" to "TOKEN" BEFORE BUILDING THE JAR LIBRARY
        jda = JDABuilder
                .create(Config.get("DEV_TOKEN"),
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setChunkingFilter(ChunkingFilter.ALL)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS,
                        CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .setAudioSendFactory(new NativeAudioSendFactory(400))
                .addEventListeners(new Listener())
                .build()
                .awaitReady();


        jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.listening(Config.get("PRESENCE")));

        CommandManager.updateSlashCommands();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void main(String[] args) throws InterruptedException
    {
        runTasks();
        new Scott();


        // Commands invoked in the console window
        Scanner consoleCommands = new Scanner(System.in);
        while (consoleCommands.hasNext())
        {

            String scanned = consoleCommands.next();

            if (scanned.equals("stop"))
            {
                LOGGER.info("Shutting Down...");
                jda.shutdown();
                System.exit(0);

            } else
            {
                LOGGER.warn("Invalid Command.");
            }
        }
    }
    public static void runTasks() {
        new PlayerCleanup("PlayerCleanup");
    }
}
