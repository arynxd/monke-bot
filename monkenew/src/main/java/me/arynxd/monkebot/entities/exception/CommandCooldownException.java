package me.arynxd.monkebot.entities.exception;

import me.arynxd.monkebot.entities.command.Command;

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
