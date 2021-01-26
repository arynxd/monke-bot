package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.commands.subcommands.vote.VoteCloseCommand;
import org.igsq.igsqbot.commands.subcommands.vote.VoteCreateCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VoteCommand extends Command
{
	public VoteCommand()
	{
		super("Vote", "Controls voting", "[create / close]");
		addAliases("vote");
		addMemberPermissions(Permission.MANAGE_SERVER);
		addChildren(
				new VoteCreateCommand(this),
				new VoteCloseCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}
