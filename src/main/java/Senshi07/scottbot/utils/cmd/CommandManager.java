package Senshi07.scottbot.utils.cmd;

import Senshi07.scottbot.Scott;
import Senshi07.scottbot.commands.PingCommand;
import Senshi07.scottbot.commands.music.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to pass the parameters of Command classes to the
 * {@link Listener Listener} class
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class CommandManager
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final List<Command> commands = new ArrayList<>();

    public CommandManager()
    {
        // General Commands
        addCommand(new PingCommand());

        // Music Commands
        addCommand(new JoinCommand());
        addCommand(new DisconnectCommand());
        addCommand(new PlayCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new StopCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new RepeatCommand());
        addCommand(new SkipCommand());

        addCommand(new QueueCommand());
    }

    private void addCommand(Command cmd)
    {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound)
        {
            throw new IllegalArgumentException("Command Name in use.");
        }
        commands.add(cmd);
    }

    public Command getCommand(String search)
    {
        String searchLower = search.toLowerCase();

        for (Command cmd : this.commands)
        {
            if (cmd.getName().equals(searchLower))
            {
                return cmd;
            }
        }

        return null;
    }

    void handle(SlashCommandInteractionEvent event)
    {
        String[] split = event.getCommandString().replaceFirst("/", "").replaceFirst("input:", "")
                .split("\\s+");
        LOGGER.info(Arrays.toString(split));

        String invoke = split[0].toLowerCase();
        Command cmd = this.getCommand(invoke);

        if (cmd != null)
        {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }

    public static void updateSlashCommands()
    {
        Scott.jda.updateCommands().addCommands(
                        // General Commands
                        Commands.slash(PingCommand.name,PingCommand.description),
                        // Music Commands0
                        Commands.slash(JoinCommand.name, JoinCommand.description),
                        Commands.slash(DisconnectCommand.name, DisconnectCommand.description),
                        Commands.slash(PlayCommand.name, PlayCommand.description)
                                .addOption(OptionType.STRING, "input", "A search term or URL. Youtube/YT Music Only"),
                        Commands.slash(PauseCommand.name, PauseCommand.description),
                        Commands.slash(ResumeCommand.name, ResumeCommand.description),
                        Commands.slash(StopCommand.name, StopCommand.description),
                        Commands.slash(NowPlayingCommand.name, NowPlayingCommand.description),
                        Commands.slash(RepeatCommand.name, RepeatCommand.description),
                        Commands.slash(SkipCommand.name, SkipCommand.description))
                .queue();

        LOGGER.info("Commands updated successfully");
    }
}
