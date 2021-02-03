package me.arynxd.monkebot.entities.cache;

import java.time.OffsetDateTime;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Message;

/**
 * Represents a message in the {@link MessageCache cache}.
 */
public class CachedMessage implements ICacheableEntity<Long, CachedMessage>
{
	private final OffsetDateTime timeCreated;
	private final String contentRaw;
	private final CachedAuthor author;
	private final String channelId;
	private final Long id;

	/**
	 * Constructs a {@link CachedMessage cached message} from a received {@link net.dv8tion.jda.api.entities.Message message}.
	 *
	 * @param message The {@link net.dv8tion.jda.api.entities.Message message} to construct from.
	 */
	public CachedMessage(@Nonnull Message message)
	{
		this.timeCreated = message.getTimeCreated();
		this.contentRaw = message.getContentRaw();
		this.author = new CachedAuthor(message.getAuthor());
		this.channelId = message.getChannel().getId();
		this.id = message.getIdLong();
	}

	/**
	 * @return The creation time for this {@link CachedMessage message}.
	 */
	@Nonnull
	public OffsetDateTime getTimeCreated()
	{
		return timeCreated;
	}

	/**
	 * @return The content for this {@link CachedMessage message}.
	 */
	@Nonnull
	public String getContentRaw()
	{
		return contentRaw;
	}

	/**
	 * @return The {@link CachedAuthor author} for this {@link CachedMessage message}.
	 */
	@Nonnull
	public CachedAuthor getAuthor()
	{
		return author;
	}

	/**
	 * @return The channel Id for this {@link CachedMessage message}.
	 */
	@Nonnull
	public String getChannelId()
	{
		return channelId;
	}

	/**
	 * @return The message Id for this {@link CachedMessage message}.
	 */
	@Override
	@Nonnull
	public Long getKey()
	{
		return id;
	}


	@Override
	public CachedMessage getData()
	{
		return this;
	}
}
