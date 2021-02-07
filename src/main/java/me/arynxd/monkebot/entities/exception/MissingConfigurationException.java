package me.arynxd.monkebot.entities.exception;


public class MissingConfigurationException extends CommandException
{
	public MissingConfigurationException(String text)
	{
		super(text);
	}
}
