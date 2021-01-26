package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class CommandHierarchyException extends CommandException
{
	public CommandHierarchyException(Command command)
	{
		super(command);
	}
}
