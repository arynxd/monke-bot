package me.arynxd.monkebot.objects.exception;

import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;


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
