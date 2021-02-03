package me.arynxd.monkebot.entities.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuildSettingsCache implements ICache<String, CachedGuildSetting>
{
	private static final Map<Long, GuildSettingsCache> GUILD_CACHES = new ConcurrentHashMap<>();

	private static final Map<String, CachedGuildSetting> cachedValues = new ConcurrentHashMap<>();

	public static GuildSettingsCache getCache(Long guildId)
	{
		GuildSettingsCache cache = GUILD_CACHES.get(guildId);
		if(GUILD_CACHES.get(guildId) == null)
		{
			cache = new GuildSettingsCache();
			GUILD_CACHES.put(guildId, cache);
		}
		return cache;
	}

	@Override
	public void put(CachedGuildSetting value)
	{
		cachedValues.put(value.getKey(), value);
	}

	@Override
	public void put(Collection<CachedGuildSetting> values)
	{
		values.forEach(this::put);
	}

	@Nullable
	@Override
	public CachedGuildSetting get(String key)
	{
		return cachedValues.get(key);
	}

	@Override
	public void update(CachedGuildSetting oldValue, CachedGuildSetting newValue)
	{
		cachedValues.put(oldValue.getKey(), newValue);
	}

	@Override
	public void update(String oldValue, CachedGuildSetting newValue)
	{
		cachedValues.put(oldValue, newValue);
	}

	@NotNull
	@Override
	public Boolean isCached(String key)
	{
		return cachedValues.containsKey(key);
	}

	@Override
	public void remove(String key)
	{
		cachedValues.remove(key);
	}

	@Override
	public void remove(CachedGuildSetting key)
	{
		remove(key.getKey());
	}

	@Override
	public void remove(Collection<CachedGuildSetting> values)
	{
		values.forEach(this::remove);
	}
}
