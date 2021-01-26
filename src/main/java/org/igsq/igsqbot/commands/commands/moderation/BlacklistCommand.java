package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistAddCommand;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BlacklistCommand extends Command
{
	public BlacklistCommand()
	{
		super("Blacklist", "Controls the blacklists.", "[add / remove / show]");
		addAliases("blacklist");
		addMemberPermissions(Permission.MANAGE_SERVER);
		addChildren(
				new BlacklistAddCommand(this),
				new BlacklistRemoveCommand(this),
				new BlacklistShowCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}