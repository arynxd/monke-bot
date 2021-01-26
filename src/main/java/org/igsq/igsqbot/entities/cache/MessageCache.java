package org.igsq.igsqbot.entities.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class representing a cache of {@link org.igsq.igsqbot.entities.cache.CachedMessage messages}.
 *
 * @see #getCache(Long)
 * @see #getCache(net.dv8tion.jda.api.entities.Guild)
 */
public class MessageCache
{
	/**
	 * Holds all of the active {@link org.igsq.igsqbot.entities.cache.MessageCache caches}.
	 */
	private static final Map<Long, MessageCache> MESSAGE_CACHES = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCache.class);
	/**
	 * A {@link java.util.List list} of {@link org.igsq.igsqbot.entities.cache.CachedMessage messages}
	 * <p>
	 * A message contained within this list will be removed after 1 hour of the last access, or the cache size reaches 1000 messages, whichever occurs first.
	 */
	private final Map<Long, CachedMessage> cachedMessages;
	private final long guildId;

	/**
	 * Constructs a new {@link org.igsq.igsqbot.entities.cache.MessageCache cache}
	 *
	 * @param guildId The guildId for the cache.
	 */
	public MessageCache(long guildId)
	{
		this.guildId = guildId;

		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(1, TimeUnit.HOURS)
				.build();
	}

	/**
	 * Gets a {@link org.igsq.igsqbot.entities.cache.MessageCache cache} from the stored {@link #MESSAGE_CACHES caches}.
	 * <p>
	 * This will create a new cache if one does not exist.
	 *
	 * @param guildId The guildId.
	 * @return The cache associated with the guildId.
	 */
	@Nonnull
	public static MessageCache getCache(@Nonnull Long guildId)
	{
		MessageCache cache = MESSAGE_CACHES.get(guildId);
		if(MESSAGE_CACHES.get(guildId) == null)
		{
			cache = new MessageCache(guildId);
			MESSAGE_CACHES.put(guildId, cache);
		}
		return cache;
	}

	/**
	 * Gets a {@link org.igsq.igsqbot.entities.cache.MessageCache cache} from the stored {@link #MESSAGE_CACHES caches}.
	 * <p>
	 * This will create a new cache if one does not exist.
	 *
	 * @param guild The guild.
	 * @return The cache associated with the guild.
	 */
	@Nonnull
	public static MessageCache getCache(@Nonnull Guild guild)
	{
		MessageCache cache = MESSAGE_CACHES.get(guild.getIdLong());
		if(MESSAGE_CACHES.get(guild.getIdLong()) == null)
		{
			cache = new MessageCache(guild.getIdLong());
			MESSAGE_CACHES.put(guild.getIdLong(), cache);
		}
		return cache;
	}

	/**
	 * Adds a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} to the {@link #cachedMessages}.
	 * @param message The {@link org.igsq.igsqbot.entities.cache.CachedMessage message} to add.
	 */
	public void set(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Adding message " + message.getIdLong() + " to cache.");
		cachedMessages.putIfAbsent(message.getIdLong(), message);
	}

	/**
	 * Adds a {@link java.util.List list} of {@link org.igsq.igsqbot.entities.cache.CachedMessage messages} to the {@link #cachedMessages}.
	 * @param messages The {@link java.util.List list} of {@link org.igsq.igsqbot.entities.cache.CachedMessage messages} to add.
	 */
	public void set(@Nonnull List<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			LOGGER.debug("Adding message " + selectedMessage.getIdLong() + " to cache.");
			cachedMessages.putIfAbsent(selectedMessage.getIdLong(), selectedMessage);
		}
	}

	/**
	 * Gets a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} from the {@link #cachedMessages cache}.
	 * @param messageId The messageId to get.
	 * @return The {@link org.igsq.igsqbot.entities.cache.CachedMessage message} or <code>null</code> if a message is not found.
	 * @see #isInCache(Long) 
	 */
	@Nullable
	public CachedMessage get(@Nonnull Long messageId)
	{
		LOGGER.debug("Fetching message " + messageId + " from cache.");
		for(Map.Entry<Long, CachedMessage> entry : cachedMessages.entrySet())
		{
			if(entry.getKey().equals(messageId))
			{
				LOGGER.debug("returned message " + entry.getValue().getIdLong() + " from cache.");
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Removes a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} from the {@link #cachedMessages cache}.
	 * @param messageId The messageId to remove.
	 */
	public void remove(@Nonnull Long messageId)
	{
		LOGGER.debug("Removed message " + messageId + " from cache.");
		cachedMessages.remove(messageId);
	}

	/**
	 * Removes a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} from the {@link #cachedMessages cache}.
	 * @param message The message to remove.
	 */
	public void remove(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
		cachedMessages.remove(message.getIdLong());
	}

	/**
	 * Removes a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} from the {@link #cachedMessages cache}.
	 * @param message The message to remove.
	 */
	public void remove(@Nonnull Message message)
	{
		LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
		cachedMessages.remove(message.getIdLong());
	}

	/**
	 * Removes a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} from the {@link #cachedMessages cache}.
	 * @param messages The {@link java.util.List list} of {@link net.dv8tion.jda.api.entities.Message messages} to remove.
	 */
	public void remove(@Nonnull List<Message> messages)
	{
		messages.forEach(message ->
		{
			LOGGER.debug("Removed message " + message.getIdLong() + " from cache.");
			cachedMessages.remove(message.getIdLong());
		});
	}

	/**
	 * Queries the {@link #cachedMessages cache} to see if it contains a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} with the given id.
	 * @param messageId The messageId to look for.
	 * @return Whether the {@link #cachedMessages cache} contains the message.
	 */
	public boolean isInCache(@Nonnull Long messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	/**
	 * Queries the {@link #cachedMessages cache} to see if it contains a {@link org.igsq.igsqbot.entities.cache.CachedMessage message} with the given id.
	 * @param message The message to look for.
	 * @return Whether the {@link #cachedMessages cache} contains the message.
	 */
	public boolean isInCache(@Nonnull Message message)
	{
		return cachedMessages.containsKey(message.getIdLong());
	}

	/**
	 * Updates the {@link #cachedMessages cache}, replacing the oldMessage with the newMessage.
	 * @param oldMessage The old message.
	 * @param newMessage the new message.
	 */
	public void update(@Nonnull CachedMessage oldMessage, @Nonnull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage.getIdLong() + " -> " + newMessage.getIdLong() + " in cache.");
		cachedMessages.remove(oldMessage.getIdLong());
		set(newMessage);
	}

	/**
	 * Updates the {@link #cachedMessages cache}, replacing the oldMessage with the newMessage.
	 * @param oldMessageId The old messageId.
	 * @param newMessage the new message.
	 */
	public void update(@Nonnull Long oldMessageId, @Nonnull CachedMessage newMessage)
	{
		cachedMessages.remove(oldMessageId);
		set(newMessage);
	}

	/**
	 * @return The id associated with this {@link org.igsq.igsqbot.entities.cache.MessageCache cache}. Never null.
	 */
	@Nonnull
	public Long getID()
	{
		return guildId;
	}

	/**
	 * @return The {@link #cachedMessages cached message} associated with this {@link org.igsq.igsqbot.entities.cache.MessageCache cache}.
	 */
	@Nonnull
	public Map<Long, CachedMessage> getCacheView()
	{
		return cachedMessages;
	}
}
