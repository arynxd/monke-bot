package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class CommandCooldownException extends CommandException
{
	/**
	 * The command was on cooldown.
	 */
	public CommandCooldownException(Command command)
	{
		super(command);
	}
}
