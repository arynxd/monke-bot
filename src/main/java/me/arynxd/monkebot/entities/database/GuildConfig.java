package me.arynxd.monkebot.entities.database;

import java.sql.Connection;
import javax.annotation.Nonnull;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.jooq.Tables;
import net.dv8tion.jda.api.entities.Guild;
import org.jooq.Field;

import static me.arynxd.monkebot.entities.jooq.tables.Guilds.GUILDS;

/**
 * A {@link me.arynxd.monkebot.handlers.DatabaseHandler database} wrapper for {@link net.dv8tion.jda.api.entities.Guild guild} configuration options.
 */
public class GuildConfig
{
	private final Monke monke;
	private final long guildId;

	/**
	 * Constructs a new {@link GuildConfig GuildConfig}
	 *
	 * @param event The {@link me.arynxd.monkebot.entities.command.CommandEvent event} to construct from.
	 */
	public GuildConfig(@Nonnull CommandEvent event)
	{
		this.guildId = event.getGuild().getIdLong();
		this.monke = event.getMonke();
	}

	/**
	 * Constructs a new {@link GuildConfig GuildConfig}
	 *
	 * @param guildId The id to construct from.
	 * @param monke   The {@link me.arynxd.monkebot.Monke igsqbot} instance to construct from.
	 */
	public GuildConfig(@Nonnull Long guildId, @Nonnull Monke monke)
	{
		this.guildId = guildId;
		this.monke = monke;
	}

	/**
	 * Constructs a new {@link GuildConfig GuildConfig}
	 *
	 * @param guild The {@link net.dv8tion.jda.api.entities.Guild guild} to construct from.
	 * @param monke The {@link me.arynxd.monkebot.Monke igsqbot} instance to construct from.
	 */
	public GuildConfig(@Nonnull Guild guild, @Nonnull Monke monke)
	{
		this.guildId = guild.getIdLong();
		this.monke = monke;
	}

	/**
	 * @return The verified role for this guild, -1 if it does not exist, or an error occurred.
	 */
	@Nonnull
	public Long getVerifiedRole()
	{
		return getLong(GUILDS.VERIFIED_ROLE);
	}

	/**
	 * @return The prefix for this guild, or {@link me.arynxd.monkebot.Constants#DEFAULT_BOT_PREFIX the default} if it does not exist, or an error occurred.
	 */
	@Nonnull
	public String getPrefix()
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context.select(GUILDS.PREFIX).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			String result = query.fetchOne(GUILDS.PREFIX);

			if(result == null)
			{
				return Constants.DEFAULT_BOT_PREFIX;
			}
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return Constants.DEFAULT_BOT_PREFIX;
		}
	}

	/**
	 * Sets a prefix for this guild.
	 *
	 * @param prefix The new prefix
	 */
	public void setPrefix(@Nonnull String prefix)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context.update(GUILDS).set(GUILDS.PREFIX, prefix).where(GUILDS.GUILD_ID.eq(guildId));
			query.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}


	/**
	 * Sets the preferred {@link me.arynxd.monkebot.entities.database.Language language} for this guild. for this guild.
	 *
	 * @param language The new {@link me.arynxd.monkebot.entities.database.Language language}.
	 */
	public void setLanguage(@Nonnull Language language)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.update(GUILDS)
					.set(GUILDS.PREFERED_LANGUAGE, language.getLanguageCode())
					.where(GUILDS.GUILD_ID.eq(guildId));

			query.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	/**
	 * @return The preferred {@link me.arynxd.monkebot.entities.database.Language language} for this guild. Default en_US.
	 */
	@Nonnull
	public Language getLanguage()
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.select(GUILDS.PREFERED_LANGUAGE)
					.from(GUILDS)
					.where(GUILDS.GUILD_ID.eq(guildId));

			String result = query.fetchOne(GUILDS.PREFERED_LANGUAGE);

			if(result == null)
			{
				return Language.EN_US;
			}
			query.close();
			return Language.valueOf(result.toUpperCase());
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return Language.EN_US;
		}
	}

	/**
	 * @return The level up bot for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getLevelUpBot()
	{
		return getLong(GUILDS.LEVEL_UP_BOT);
	}

	/**
	 * Sets a level up bot for this guild.
	 *
	 * @param userId The new bot.
	 */
	public void setLevelUpBot(@Nonnull Long userId)
	{
		setValue(GUILDS.LEVEL_UP_BOT, userId);
	}

	/**
	 * @return The unverified role for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getUnverifiedRole()
	{
		return getLong(GUILDS.UNVERIFIED_ROLE);
	}

	/**
	 * @return The reports channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getReportChannel()
	{
		return getLong(GUILDS.REPORT_CHANNEL);
	}

	/**
	 * @return The log channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getLogChannel()
	{
		return getLong(GUILDS.LOG_CHANNEL);
	}

	/**
	 * @return The welcome channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getWelcomeChannel()
	{
		return getLong(GUILDS.WELCOME_CHANNEL);
	}

	/**
	 * @return The vote channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getVoteChannel()
	{
		return getLong(GUILDS.VOTE_CHANNEL);
	}

	/**
	 * @return The temporary ban role for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getTempBanRole()
	{
		return getLong(GUILDS.MUTED_ROLE);
	}

	/**
	 * @return The suggestions channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getSuggestionChannel()
	{
		return getLong(GUILDS.SUGGESTION_CHANNEL);
	}

	/**
	 * @return The channel suggestions channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getChannelSuggestionChannel()
	{
		return getLong(GUILDS.CHANNEL_SUGGESTION_CHANNEL);
	}

	/**
	 * Gets the value for a field.
	 *
	 * @param field The field to get.
	 * @return The value, defaults to -1.
	 */
	private long getLong(Field<?> field)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context.select(field).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			long result = Long.parseLong(String.valueOf(query.fetchOne(field)));
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	/**
	 * Sets the new value for a field.
	 *
	 * @param field The field to set.
	 * @param value The value to set.
	 * @param <T>   The type to set.
	 */
	private <T> void setValue(Field<T> field, T value)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			context.update(Tables.GUILDS).set(field, value).where(GUILDS.GUILD_ID.eq(guildId)).execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}
}
