package Senshi07.scottbot.commands;

import Senshi07.scottbot.Scott;
import Senshi07.scottbot.utils.cmd.Command;
import Senshi07.scottbot.utils.cmd.CommandContext;

/**
 * Outputs the current Gateway ping to the members text channel.
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class PingCommand extends Command {

    public static String name = "ping";
    public static String description = "Shows the current ping";

    public void handle(CommandContext ctx) {
        long gatePing = Scott.jda.getGatewayPing();

        String response = ":table_tennis: Pong!: " + gatePing + " ms";

        ctx.event().getInteraction().reply(response).queue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getHelp() {
        return description;
    }
}