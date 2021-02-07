package me.arynxd.monkebot.entities.cache;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.Language;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;

import static me.arynxd.monkebot.entities.jooq.tables.Guilds.GUILDS;

public class GuildSettingsCache implements ICache<String, CachedGuildSetting>
{
	private static final Map<Long, GuildSettingsCache> GUILD_CACHES = new ConcurrentHashMap<>();

	private static final Map<String, CachedGuildSetting> cachedValues = new ConcurrentHashMap<>();

	private final Monke monke;
	private final Long guildId;

	public GuildSettingsCache(Long guildId, Monke monke)
	{
		this.monke = monke;
		this.guildId = guildId;
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
	public @Nonnull
	Boolean isCached(String key)
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

	public Long getLogChannel()
	{
		return cacheGetLong("logchannel", GUILDS.LOG_CHANNEL);
	}

	public @Nonnull
	Long getLevelUpBot()
	{
		return cacheGetLong("levelupbot", GUILDS.LEVEL_UP_BOT);
	}

	public void setLevelUpBot(@Nonnull Long newId)
	{
		cachePut("levelupbot", GUILDS.LEVEL_UP_BOT, newId);
	}

	public @Nonnull
	Long getUnverifiedRole()
	{
		return cacheGetLong("unverifiedrole", GUILDS.UNVERIFIED_ROLE);
	}

	public @Nonnull
	Long getReportChannel()
	{
		return cacheGetLong("reportchannel", GUILDS.REPORT_CHANNEL);
	}

	public @Nonnull
	Long getWelcomeChannel()
	{
		return cacheGetLong("welcomechannel", GUILDS.WELCOME_CHANNEL);
	}

	public @Nonnull
	Long getVoteChannel()
	{
		return cacheGetLong("votechannel", GUILDS.VOTE_CHANNEL);
	}

	public @Nonnull
	Long getTempBanRole()
	{
		return cacheGetLong("tempbanrole", GUILDS.MUTED_ROLE);
	}

	public @Nonnull
	Long getSuggestionChannel()
	{
		return cacheGetLong("suggestionchannel", GUILDS.SUGGESTION_CHANNEL);
	}

	public @Nonnull
	Long getChannelSuggestionChannel()
	{
		return cacheGetLong("channelsuggestionchannel", GUILDS.CHANNEL_SUGGESTION_CHANNEL);
	}

	public @Nonnull
	Long getVerifiedRole()
	{
		return cacheGetLong("verifiedrole", GUILDS.VERIFIED_ROLE);
	}

	public @Nonnull
	Language getLanguage()
	{
		String language = cacheGetString("language", GUILDS.PREFERED_LANGUAGE);
		if(language == null)
		{
			return Language.EN_US;
		}
		return Language.valueOf(language);
	}

	public void setLanguage(@Nonnull Language newLanguage)
	{
		setField(GUILDS.PREFERED_LANGUAGE, newLanguage.getLanguageCode());
	}

	public @Nonnull
	String getPrefix()
	{
		String prefix = cacheGetString("prefix", GUILDS.PREFIX);
		if(prefix == null)
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		return prefix;
	}

	public void setPrefix(@Nonnull String newPrefix)
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

	private <T> long cacheGetLong(String label, Field<T> field)
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

	private <T> String cacheGetString(String label, Field<T> field)
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
			context.update(me.arynxd.monkebot.entities.jooq.Tables.GUILDS).set(field, value).where(GUILDS.GUILD_ID.eq(guildId)).execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}
}
