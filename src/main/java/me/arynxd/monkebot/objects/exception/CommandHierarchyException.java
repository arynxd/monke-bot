package me.arynxd.monkebot.objects.exception;

import me.arynxd.monkebot.objects.command.Command;

public class CommandHierarchyException extends CommandException
{
	public CommandHierarchyException(Command command)
	{
		super(command);
	}
}
