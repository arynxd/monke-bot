package me.arynxd.monkebot.entities.cache;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.User;

public class CachedAuthor implements ICacheableEntity<Long, CachedAuthor>
{
	private final boolean isBot;
	private final String mentionable;
	private final long id;

	public CachedAuthor(@Nonnull User user)
	{
		this.isBot = user.isBot();
		this.mentionable = user.getAsMention();
		this.id = user.getIdLong();
	}

	public @Nonnull
	Boolean isBot()
	{
		return isBot;
	}

	public @Nonnull
	String getAsMention()
	{
		return mentionable;
	}

	@Override
	public @Nonnull
	Long getKey()
	{
		return id;
	}

	@Override
	public @Nonnull
	CachedAuthor getData()
	{
		return this;
	}
}
