package org.igsq.igsqbot.entities.cache;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.User;

/**
 * Represents an author for a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} in the {@link org.igsq.igsqbot.entities.cache.MessageCache}.
 */
public class CachedAuthor
{
	private final boolean isBot;
	private final String mentionable;

	/**
	 * Constructs a {@link org.igsq.igsqbot.entities.cache.CachedAuthor author} from a {@link net.dv8tion.jda.api.entities.User user}.
	 * @param user The {@link net.dv8tion.jda.api.entities.User user} to construct from.
	 */
	public CachedAuthor(@Nonnull User user)
	{
		this.isBot = user.isBot();
		this.mentionable = user.getAsMention();
	}

	/**
	 * @return Whether this {@link org.igsq.igsqbot.entities.cache.CachedAuthor author} is a bot user.
	 */
	@Nonnull
	public Boolean isBot()
	{
		return isBot;
	}

	/**
	 * @return This {@link org.igsq.igsqbot.entities.cache.CachedAuthor author} as a mention.
	 */
	@Nonnull
	public String getAsMention()
	{
		return mentionable;
	}
}
