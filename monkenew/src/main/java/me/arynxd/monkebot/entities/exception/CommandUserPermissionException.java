package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;

public class CommandUserPermissionException extends CommandException
{
	/**
	 * An error occurred with the members permissions.
	 */
	public CommandUserPermissionException(Command command)
	{
		super(command);
	}
}
