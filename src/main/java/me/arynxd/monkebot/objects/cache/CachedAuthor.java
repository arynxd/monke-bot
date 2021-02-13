package me.arynxd.monkebot.objects.cache;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class CachedAuthor implements ICacheableEntity<Long, CachedAuthor>
{
	private final boolean isBot;
	private final String mentionable;
	private final long id;

	public CachedAuthor(@NotNull User user)
	{
		this.isBot = user.isBot();
		this.mentionable = user.getAsMention();
		this.id = user.getIdLong();
	}

	public @NotNull Boolean isBot()
	{
		return isBot;
	}

	public @NotNull String getAsMention()
	{
		return mentionable;
	}

	@Override
	public @NotNull Long getKey()
	{
		return id;
	}

	@Override
	public @NotNull CachedAuthor getData()
	{
		return this;
	}
}
