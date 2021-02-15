package me.arynxd.monkebot.commands.subcommands.vote;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Vote;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class VoteCloseCommand extends Command
{
	public VoteCloseCommand(Command parent)
	{
		super(parent, "close", "Closes a vote.", "[voteID]");
		addMemberPermissions(Permission.MANAGE_CHANNEL);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;

		OptionalLong id = new Parser(args.get(0), event).parseAsUnsignedLong();

		if (id.isPresent())
		{
			Boolean success = Vote.closeById(id.getAsLong(), event);
			if (success == null)
			{
				return;
			}

			if (!success)
			{
				failure.accept(new CommandResultException("Vote with ID **" + id.getAsLong() + "** not found."));
			}
			else
			{
				event.replySuccess("Closed vote **" + id.getAsLong() + "** successfully.");
			}
		}
	}
}
