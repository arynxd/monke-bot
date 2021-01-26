package me.arynxd.monkebot.entities.cache;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.User;

/**
 * Represents an author for a {@link CachedMessage message} in the {@link MessageCache}.
 */
public class CachedAuthor
{
	private final boolean isBot;
	private final String mentionable;

	/**
	 * Constructs a {@link CachedAuthor author} from a {@link net.dv8tion.jda.api.entities.User user}.
	 * @param user The {@link net.dv8tion.jda.api.entities.User user} to construct from.
	 */
	public CachedAuthor(@Nonnull User user)
	{
		this.isBot = user.isBot();
		this.mentionable = user.getAsMention();
	}

	/**
	 * @return Whether this {@link CachedAuthor author} is a bot user.
	 */
	@Nonnull
	public Boolean isBot()
	{
		return isBot;
	}

	/**
	 * @return This {@link CachedAuthor author} as a mention.
	 */
	@Nonnull
	public String getAsMention()
	{
		return mentionable;
	}
}
