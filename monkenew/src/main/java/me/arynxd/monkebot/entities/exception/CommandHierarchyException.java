package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;

public class CommandHierarchyException extends CommandException
{
	public CommandHierarchyException(Command command)
	{
		super(command);
	}
}
