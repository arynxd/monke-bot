package me.arynxd.monkebot.entities.exception;


public class CommandResultException extends CommandException
{
	/**
	 * The command produced an unexpected result.
	 */
	public CommandResultException(String text)
	{
		super(text);
	}
}