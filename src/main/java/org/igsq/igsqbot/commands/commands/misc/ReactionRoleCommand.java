package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleAddCommand;
import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ReactionRoleCommand extends Command
{
	public ReactionRoleCommand()
	{
		super("Reaction Role", "Controls reaction roles.", "[add / remove / show]");
		addAliases("rr", "reactionrole");
		addFlags(CommandFlag.GUILD_ONLY);
		addChildren(
				new ReactionRoleAddCommand(this),
				new ReactionRoleRemoveCommand(this),
				new ReactionRoleShowCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}