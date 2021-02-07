package me.arynxd.monkebot.entities.cache;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageCache implements ICache<Long, CachedMessage>
{
	private static final Map<Long, MessageCache> MESSAGE_CACHES = new ConcurrentHashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCache.class);
	private final Map<Long, CachedMessage> cachedMessages;

	public MessageCache()
	{
		this.cachedMessages = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(1, TimeUnit.HOURS)
				.build();
	}

	public static @Nonnull
	MessageCache getCache(@Nonnull Long guildId)
	{
		MessageCache cache = MESSAGE_CACHES.get(guildId);
		if(MESSAGE_CACHES.get(guildId) == null)
		{
			cache = new MessageCache();
			MESSAGE_CACHES.put(guildId, cache);
		}
		return cache;
	}

	@Override
	public void put(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Adding message " + message.getKey() + " to cache.");
		cachedMessages.put(message.getKey(), message);
	}

	@Override
	public void put(@Nonnull Collection<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			LOGGER.debug("Adding message " + selectedMessage.getKey() + " to cache.");
			cachedMessages.put(selectedMessage.getKey(), selectedMessage);
		}
	}

	@Override
	public @Nonnull
	CachedMessage get(@Nonnull Long messageId)
	{
		LOGGER.debug("Fetching message " + messageId + " from cache.");
		return cachedMessages.get(messageId);
	}

	@Override
	public void remove(@Nonnull Long messageId)
	{
		LOGGER.debug("Removed message " + messageId + " from cache.");
		cachedMessages.remove(messageId);
	}

	@Override
	public void remove(@Nonnull CachedMessage message)
	{
		LOGGER.debug("Removed message " + message.getKey() + " from cache.");
		cachedMessages.remove(message.getKey());
	}

	@Override
	public void remove(@Nonnull Collection<CachedMessage> messages)
	{
		messages.forEach(message ->
		{
			LOGGER.debug("Removed message " + message.getKey() + " from cache.");
			cachedMessages.remove(message.getKey());
		});
	}

	@Override
	public @Nonnull
	Boolean isCached(@Nonnull Long messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	@Override
	public void update(@Nonnull CachedMessage oldMessage, @Nonnull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage.getKey() + " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage.getKey(), newMessage);
	}

	@Override
	public void update(@Nonnull Long oldMessage, @Nonnull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage + " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage, newMessage);
	}
}
