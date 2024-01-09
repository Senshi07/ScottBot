package Senshi07.scottbot.utils.cmd;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilises the ListenerAdapter function of JDA to allow the bot to examine messages received in
 * guilds so commands may be passed to the Command handling classes
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    public void onReady(@NotNull ReadyEvent event)
    {
        LOGGER.info("{} is online!", event.getJDA().getSelfUser().getName());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)
    {
        manager.handle(event);
    }
}
