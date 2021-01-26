package org.igsq.igsqbot.entities.exception;


public class MissingConfigurationException extends CommandException
{
	/**
	 * A required configuration option was missing.
	 */
	public MissingConfigurationException(String text)
	{
		super(text);
	}
}
