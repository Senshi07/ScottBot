package Senshi07.scottbot.utils.cmd;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

/**
 * Defines passes methods associated with the GuildMessageReceivedEvent to the
 * Command abstract class for use in creating a command's behaviour
 *
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class CommandContext
{
    private final SlashCommandInteractionEvent event;
    private final List<String> args;

    public CommandContext(SlashCommandInteractionEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    public Guild getGuild() {
        return this.event.getGuild();
    }


    public TextChannel getChannel()
    {
        return (TextChannel) this.event.getChannel();
    }

    public List<String> args()
    {
        return this.args;
    }

    public SlashCommandInteractionEvent event()
    {
        return this.event;
    }

    public Member getMember() {
        return this.event.getMember();
    }
}
