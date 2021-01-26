package org.igsq.igsqbot.entities.command;

/**
 * An enum for flags related to {@link org.igsq.igsqbot.entities.command.Command command} execution.
 */
public enum CommandFlag
{
	/**
	 * If the {@link org.igsq.igsqbot.entities.command.Command command} must be executed in a {@link net.dv8tion.jda.api.entities.Guild guild}.
	 *
	 * @see org.igsq.igsqbot.entities.command.CommandEvent#isFromGuild() .
	 */
	GUILD_ONLY,
	/**
	 * If the {@link org.igsq.igsqbot.entities.command.Command command} can only be used by developers.
	 *
	 * @see org.igsq.igsqbot.entities.command.CommandEvent#isDeveloper().
	 */
	DEVELOPER_ONLY,
	/**
	 * If the {@link org.igsq.igsqbot.entities.command.Command command} authors message should be automatically deleted.
	 */
	AUTO_DELETE_MESSAGE,
	/**
	 * If the {@link org.igsq.igsqbot.entities.command.Command command's} content bypasses the {@link org.igsq.igsqbot.util.BlacklistUtils#isBlacklistedPhrase(net.dv8tion.jda.api.events.message.MessageReceivedEvent, org.igsq.igsqbot.IGSQBot) blacklist checks}.
	 */
	BLACKLIST_BYPASS,
	/**
	 * If the {@link org.igsq.igsqbot.entities.command.Command command} is disabled.
	 *
	 * @see org.igsq.igsqbot.entities.command.Command#isDisabled() isDisabled()
	 */
	DISABLED;
}
