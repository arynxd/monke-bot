package me.arynxd.monkebot.entities.command;

/**
 * An enum for flags related to {@link Command command} execution.
 */
public enum CommandFlag
{
	/**
	 * If the {@link Command command} must be executed in a {@link net.dv8tion.jda.api.entities.Guild guild}.
	 *
	 * @see CommandEvent#isFromGuild() .
	 */
	GUILD_ONLY,
	/**
	 * If the {@link Command command} can only be used by developers.
	 *
	 * @see CommandEvent#isDeveloper().
	 */
	DEVELOPER_ONLY,
	/**
	 * If the {@link Command command} authors message should be automatically deleted.
	 */
	AUTO_DELETE_MESSAGE,
	/**
	 * If the {@link Command command's} content bypasses the {@link me.arynxd.monkebot.util.BlacklistUtils#isBlacklistedPhrase(net.dv8tion.jda.api.events.message.MessageReceivedEvent, me.arynxd.monkebot.Monke) blacklist checks}.
	 */
	BLACKLIST_BYPASS,
	/**
	 * If the {@link Command command} is disabled.
	 *
	 * @see Command#isDisabled() isDisabled()
	 */
	DISABLED;
}
