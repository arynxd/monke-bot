package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

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
