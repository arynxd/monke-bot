package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;


public class CommandSyntaxException extends CommandException
{
	public CommandSyntaxException(Command command)
	{
		super(command);
	}

	public CommandSyntaxException(CommandEvent ctx)
	{
		super(ctx.getCommand());
	}
}
