package me.arynxd.monkebot.entities.cache;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.Language;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;

import static me.arynxd.monkebot.entities.jooq.tables.Guilds.GUILDS;

public class GuildSettingsCache implements ICache<String, CachedGuildSetting>
{
	private static final Map<Long, GuildSettingsCache> GUILD_CACHES = new ConcurrentHashMap<>();

	private final Map<String, CachedGuildSetting> cachedValues;

	private final Monke monke;
	private final Long guildId;

	public GuildSettingsCache(Long guildId, Monke monke)
	{
		this.monke = monke;
		this.guildId = guildId;
		this.cachedValues = ExpiringMap.builder()
				.maxSize(50)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(1, TimeUnit.HOURS)
				.build();
	}

	public static GuildSettingsCache getCache(Long guildId, Monke monke)
	{
		GuildSettingsCache cache = GUILD_CACHES.get(guildId);
		if(GUILD_CACHES.get(guildId) == null)
		{
			cache = new GuildSettingsCache(guildId, monke);
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

	@Override
	public @NotNull CachedGuildSetting get(String key)
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

	@Override
	public boolean isCached(String key)
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

	public @NotNull Long getLogChannel()
	{
		return cacheGetLong("logchannel", GUILDS.LOG_CHANNEL);
	}

	public @NotNull Long getLevelUpBot()
	{
		return cacheGetLong("levelupbot", GUILDS.LEVEL_UP_BOT);
	}

	public void setLevelUpBot(long newId)
	{
		cachePut("levelupbot", GUILDS.LEVEL_UP_BOT, newId);
	}

	public @NotNull Long getUnverifiedRole()
	{
		return cacheGetLong("unverifiedrole", GUILDS.UNVERIFIED_ROLE);
	}

	public @NotNull Long getReportChannel()
	{
		return cacheGetLong("reportchannel", GUILDS.REPORT_CHANNEL);
	}

	public @NotNull Long getWelcomeChannel()
	{
		return cacheGetLong("welcomechannel", GUILDS.WELCOME_CHANNEL);
	}

	public @NotNull Long getVoteChannel()
	{
		return cacheGetLong("votechannel", GUILDS.VOTE_CHANNEL);
	}

	public @NotNull Long getTempBanRole()
	{
		return cacheGetLong("tempbanrole", GUILDS.MUTED_ROLE);
	}

	public @NotNull Long getSuggestionChannel()
	{
		return cacheGetLong("suggestionchannel", GUILDS.SUGGESTION_CHANNEL);
	}

	public @NotNull Long getChannelSuggestionChannel()
	{
		return cacheGetLong("channelsuggestionchannel", GUILDS.CHANNEL_SUGGESTION_CHANNEL);
	}

	public @NotNull Long getVerifiedRole()
	{
		return cacheGetLong("verifiedrole", GUILDS.VERIFIED_ROLE);
	}

	public @NotNull Language getLanguage()
	{
		String language = cacheGetString("language", GUILDS.PREFERED_LANGUAGE);
		if(language == null)
		{
			return Language.EN_US;
		}
		return Language.valueOf(language);
	}

	public void setLanguage(@NotNull Language newLanguage)
	{
		setField(GUILDS.PREFERED_LANGUAGE, newLanguage.getLanguageCode());
	}

	public @NotNull String getPrefix()
	{
		String prefix = cacheGetString("prefix", GUILDS.PREFIX);
		if(prefix == null)
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		return prefix;
	}

	public void setPrefix(@NotNull String newPrefix)
	{
		cachePut("prefix", GUILDS.PREFIX, newPrefix);
	}

	private <T> T getField(Field<T> field)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context.select(field).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			T result = query.fetchOne(field);
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	private long cacheGetLong(String label, Field<Long> field)
	{
		if(cachedValues.get(label) == null)
		{
			cachedValues.put(label, new CachedGuildSetting(label, String.valueOf(getField(field))));
		}
		try
		{
			return Long.parseLong(cachedValues.get(label).getValue());
		}
		catch(Exception exception)
		{
			return -1;
		}
	}

	private String cacheGetString(String label, Field<String> field)
	{
		if(cachedValues.get(label) == null)
		{
			cachedValues.put(label, new CachedGuildSetting(label, String.valueOf(getField(field))));
		}
		return cachedValues.get(label).getValue();
	}

	private <T> void cachePut(String label, Field<T> field, T newValue)
	{
		update(label, new CachedGuildSetting(label, String.valueOf(newValue)));
		setField(field, newValue);
	}


	private <T> void setField(Field<T> field, T value)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			context.update(GUILDS).set(field, value).where(GUILDS.GUILD_ID.eq(guildId)).execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}
}
