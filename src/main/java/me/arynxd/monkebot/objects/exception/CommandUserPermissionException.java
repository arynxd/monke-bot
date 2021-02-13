package me.arynxd.monkebot.objects.exception;

import me.arynxd.monkebot.objects.command.Command;

public class CommandUserPermissionException extends CommandException
{
	public CommandUserPermissionException(Command command)
	{
		super(command);
	}
}
