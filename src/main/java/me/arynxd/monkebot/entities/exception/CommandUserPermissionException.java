package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;

public class CommandUserPermissionException extends CommandException
{
	public CommandUserPermissionException(Command command)
	{
		super(command);
	}
}
