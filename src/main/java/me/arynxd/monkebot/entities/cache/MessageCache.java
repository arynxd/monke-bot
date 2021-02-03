package me.arynxd.monkebot.entities.cache;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageCache implements ICache<Long, CachedMessage>
{
	/**
	 * A {@link java.util.List list} of {@link CachedMessage messages}
	 * <p>
	 * A message contained within this list will be removed after 1 hour of the last access, or the cache size reaches 1000 messages, whichever occurs first.
	 */
	private final Map<Long, CachedMessage> cachedMessages;

	/**
	 * Holds all of the active {@link MessageCache caches}.
	 */
	private static final Map<Long, MessageCache> MESSAGE_CACHES = new ConcurrentHashMap<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCache.class);

	/**
	 * Constructs a new {@link MessageCache cache}
	 */
	public MessageCache()
	{
		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(1, TimeUnit.HOURS)
				.build();
	}


	/**
	 * Gets a {@link MessageCache cache} from the stored {@link #MESSAGE_CACHES caches}.
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
			cache = new MessageCache();
			MESSAGE_CACHES.put(guildId, cache);
		}
		return cache;
	}

	/**
	 * Adds a {@link CachedMessage message} to the {@link #cachedMessages}.
	 *
	 * @param message The {@link CachedMessage message} to add.
	 */
	@Override
	public void put(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Adding message " + message.getKey() + " to cache.");
		cachedMessages.put(message.getKey(), message);
	}

	/**
	 * Adds a {@link java.util.List list} of {@link CachedMessage messages} to the {@link #cachedMessages}.
	 *
	 * @param messages The {@link java.util.List list} of {@link CachedMessage messages} to add.
	 */
	@Override
	public void put(@Nonnull Collection<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			LOGGER.debug("Adding message " + selectedMessage.getKey() + " to cache.");
			cachedMessages.put(selectedMessage.getKey(), selectedMessage);
		}
	}

	/**
	 * Gets a {@link CachedMessage message} from the {@link #cachedMessages cache}.
	 *
	 * @param messageId The messageId to get.
	 * @return The {@link CachedMessage message} or <code>null</code> if a message is not found.
	 * @see #isCached(Long)
	 */
	@Override
	@Nullable
	public CachedMessage get(@Nonnull Long messageId)
	{
		LOGGER.debug("Fetching message " + messageId + " from cache.");
		return cachedMessages.get(messageId);
	}

	/**
	 * Removes a {@link CachedMessage message} from the {@link #cachedMessages cache}.
	 *
	 * @param messageId The messageId to remove.
	 */
	@Override
	public void remove(@Nonnull Long messageId)
	{
		LOGGER.debug("Removed message " + messageId + " from cache.");
		cachedMessages.remove(messageId);
	}

	/**
	 * Removes a {@link CachedMessage message} from the {@link #cachedMessages cache}.
	 *
	 * @param message The message to remove.
	 */
	@Override
	public void remove(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Removed message " + message.getKey() + " from cache.");
		cachedMessages.remove(message.getKey());
	}

	/**
	 * Removes a {@link CachedMessage message} from the {@link #cachedMessages cache}.
	 *
	 * @param messages The {@link java.util.List list} of {@link net.dv8tion.jda.api.entities.Message messages} to remove.
	 */
	@Override
	public void remove(@Nonnull Collection<CachedMessage> messages)
	{
		messages.forEach(message ->
		{
			LOGGER.debug("Removed message " + message.getKey() + " from cache.");
			cachedMessages.remove(message.getKey());
		});
	}

	/**
	 * Queries the {@link #cachedMessages cache} to see if it contains a {@link CachedMessage message} with the given id.
	 *
	 * @param messageId The messageId to look for.
	 * @return Whether the {@link #cachedMessages cache} contains the message.
	 */
	@Override
	@Nonnull
	public Boolean isCached(@Nonnull Long messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	/**
	 * Updates the {@link #cachedMessages cache}, replacing the oldMessage with the newMessage.
	 *
	 * @param oldMessage The old message.
	 * @param newMessage the new message.
	 */
	@Override
	public void update(@Nonnull CachedMessage oldMessage, @Nonnull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage.getKey() + " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage.getKey(), newMessage);
	}

	/**
	 * Updates the {@link #cachedMessages cache}, replacing the oldMessage with the newMessage.
	 *
	 * @param oldMessage The old message.
	 * @param newMessage the new message.
	 */
	@Override
	public void update(@Nonnull Long oldMessage, @Nonnull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage+ " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage, newMessage);
	}
}
