package me.arynxd.monkebot.objects.cache;

import java.time.OffsetDateTime;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

public class CachedMessage implements ICacheableEntity<Long, CachedMessage>
{
	private final OffsetDateTime timeCreated;
	private final String contentRaw;
	private final CachedAuthor author;
	private final String channelId;
	private final Long id;

	public CachedMessage(@NotNull Message message)
	{
		this.timeCreated = message.getTimeCreated();
		this.contentRaw = message.getContentRaw();
		this.author = new CachedAuthor(message.getAuthor());
		this.channelId = message.getChannel().getId();
		this.id = message.getIdLong();
	}

	public @NotNull OffsetDateTime getTimeCreated()
	{
		return timeCreated;
	}

	public @NotNull String getContentRaw()
	{
		return contentRaw;
	}

	public @NotNull CachedAuthor getAuthor()
	{
		return author;
	}

	public @NotNull String getChannelId()
	{
		return channelId;
	}

	@Override
	public @NotNull Long getKey()
	{
		return id;
	}


	@Override
	public @NotNull CachedMessage getData()
	{
		return this;
	}
}
