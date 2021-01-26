package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;


public class CommandSyntaxException extends CommandException
{
	/**
	 * The user entered invalid syntax.
	 */
	public CommandSyntaxException(Command command)
	{
		super(command);
	}

	/**
	 * The user entered invalid syntax.
	 */
	public CommandSyntaxException(CommandEvent ctx)
	{
		super(ctx.getCommand());
	}
}
