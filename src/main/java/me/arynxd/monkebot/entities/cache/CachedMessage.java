package me.arynxd.monkebot.entities.cache;

import java.time.OffsetDateTime;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;

public class CachedMessage implements ICacheableEntity<Long, CachedMessage>
{
	private final OffsetDateTime timeCreated;
	private final String contentRaw;
	private final CachedAuthor author;
	private final String channelId;
	private final Long id;

	public CachedMessage(@Nonnull Message message)
	{
		this.timeCreated = message.getTimeCreated();
		this.contentRaw = message.getContentRaw();
		this.author = new CachedAuthor(message.getAuthor());
		this.channelId = message.getChannel().getId();
		this.id = message.getIdLong();
	}

	public @Nonnull
	OffsetDateTime getTimeCreated()
	{
		return timeCreated;
	}

	public @Nonnull
	String getContentRaw()
	{
		return contentRaw;
	}

	public @Nonnull
	CachedAuthor getAuthor()
	{
		return author;
	}

	public @Nonnull
	String getChannelId()
	{
		return channelId;
	}

	@Override
	public @Nonnull
	Long getKey()
	{
		return id;
	}


	@Override
	public CachedMessage getData()
	{
		return this;
	}
}
