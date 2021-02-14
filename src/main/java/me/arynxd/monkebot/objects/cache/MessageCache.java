package me.arynxd.monkebot.objects.cache;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;
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

	public static @NotNull MessageCache getCache(long guildId)
	{
		MessageCache cache = MESSAGE_CACHES.get(guildId);
		if(MESSAGE_CACHES.get(guildId) == null)
		{
			cache = new MessageCache();
			MESSAGE_CACHES.put(guildId, cache);
		}
		return cache;
	}

	public static void removeCache(long guildId)
	{
		MESSAGE_CACHES.remove(guildId);
	}

	@Override
	public void put(@NotNull CachedMessage message)
	{
		LOGGER.debug("Adding message " + message.getKey() + " to cache.");
		cachedMessages.put(message.getKey(), message);
	}

	@Override
	public void put(@NotNull Collection<CachedMessage> messages)
	{
		for(CachedMessage selectedMessage : messages)
		{
			LOGGER.debug("Adding message " + selectedMessage.getKey() + " to cache.");
			cachedMessages.put(selectedMessage.getKey(), selectedMessage);
		}
	}

	@Override
	public @NotNull CachedMessage get(@NotNull Long messageId)
	{
		LOGGER.debug("Fetching message " + messageId + " from cache.");
		return cachedMessages.get(messageId);
	}

	@Override
	public void remove(@NotNull Long messageId)
	{
		LOGGER.debug("Removed message " + messageId + " from cache.");
		cachedMessages.remove(messageId);
	}

	@Override
	public void remove(@NotNull CachedMessage message)
	{
		LOGGER.debug("Removed message " + message.getKey() + " from cache.");
		cachedMessages.remove(message.getKey());
	}

	@Override
	public void remove(@NotNull Collection<CachedMessage> messages)
	{
		messages.forEach(message ->
		{
			LOGGER.debug("Removed message " + message.getKey() + " from cache.");
			cachedMessages.remove(message.getKey());
		});
	}

	@Override
	public boolean isCached(@NotNull Long messageId)
	{
		return cachedMessages.containsKey(messageId);
	}

	@Override
	public void update(@NotNull CachedMessage oldMessage, @NotNull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage.getKey() + " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage.getKey(), newMessage);
	}

	@Override
	public void update(@NotNull Long oldMessage, @NotNull CachedMessage newMessage)
	{
		LOGGER.debug("Updating message " + oldMessage + " -> " + newMessage.getKey() + " in cache.");
		cachedMessages.put(oldMessage, newMessage);
	}
}
