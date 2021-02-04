package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;

public class CommandCooldownException extends CommandException
{
	public CommandCooldownException(Command command)
	{
		super(command);
	}
}
