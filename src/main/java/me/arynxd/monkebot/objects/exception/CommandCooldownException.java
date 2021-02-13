package me.arynxd.monkebot.objects.exception;

import me.arynxd.monkebot.objects.command.Command;

public class CommandCooldownException extends CommandException
{
	public CommandCooldownException(Command command)
	{
		super(command);
	}
}
