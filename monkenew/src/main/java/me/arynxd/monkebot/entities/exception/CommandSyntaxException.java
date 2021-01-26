package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;


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
