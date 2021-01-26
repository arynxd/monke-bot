package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.jooq.Field;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;

/**
 * A {@link org.igsq.igsqbot.handlers.DatabaseHandler database} wrapper for {@link net.dv8tion.jda.api.entities.Guild guild} configuration options.
 */
public class GuildConfig
{
	private final IGSQBot igsqbot;
	private final long guildId;

	/**
	 * Constructs a new {@link org.igsq.igsqbot.entities.database.GuildConfig GuildConfig}
	 * @param event The {@link org.igsq.igsqbot.entities.command.CommandEvent event} to construct from.
	 */
	public GuildConfig(@Nonnull CommandEvent event)
	{
		this.guildId = event.getGuild().getIdLong();
		this.igsqbot = event.getIGSQBot();
	}

	/**
	 * Constructs a new {@link org.igsq.igsqbot.entities.database.GuildConfig GuildConfig}
	 * @param guildId The id to construct from.
	 * @param igsqbot The {@link org.igsq.igsqbot.IGSQBot igsqbot} instance to construct from.
	 */
	public GuildConfig(@Nonnull Long guildId, @Nonnull IGSQBot igsqbot)
	{
		this.guildId = guildId;
		this.igsqbot = igsqbot;
	}

	/**
	 * Constructs a new {@link org.igsq.igsqbot.entities.database.GuildConfig GuildConfig}
	 * @param guild The {@link net.dv8tion.jda.api.entities.Guild guild} to construct from.
	 * @param igsqbot The {@link org.igsq.igsqbot.IGSQBot igsqbot} instance to construct from.
	 */
	public GuildConfig(@Nonnull Guild guild, @Nonnull IGSQBot igsqbot)
	{
		this.guildId = guild.getIdLong();
		this.igsqbot = igsqbot;
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
	 * @return The prefix for this guild, or {@link org.igsq.igsqbot.Constants#DEFAULT_BOT_PREFIX the default} if it does not exist, or an error occurred.
	 */
	@Nonnull
	public String getPrefix()
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
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
			igsqbot.getLogger().error("An SQL error occurred", exception);
			return Constants.DEFAULT_BOT_PREFIX;
		}
	}

	/**
	 * @return The self promotion role for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getPromoRole()
	{
		return getLong(GUILDS.SELF_PROMO_ROLE);
	}

	/**
	 * @return The level up bot for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getLevelUpBot()
	{
		return getLong(GUILDS.LEVEL_UP_BOT);
	}

	/**
	 * Sets a prefix for this guild.
	 * @param prefix The new prefix
	 */
	public void setPrefix(@Nonnull String prefix)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context.update(GUILDS).set(GUILDS.PREFIX, prefix).where(GUILDS.GUILD_ID.eq(guildId));
			query.execute();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
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
	 * @return The suggestions channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getPromoChannel()
	{
		return getLong(GUILDS.SELF_PROMO_CHANNEL);
	}

	/**
	 * @return The channel suggestions channel for this guild, -1 if it does not exist, or an error occurred.
	 */
	public long getChannelSuggestionChannel()
	{
		return getLong(GUILDS.CHANNEL_SUGGESTION_CHANNEL);
	}

	/**
	 * Sets a level up bot for this guild.
	 * @param userId The new bot.
	 */
	public void setLevelUpBot(@Nonnull Long userId)
	{
		setValue(GUILDS.LEVEL_UP_BOT, userId);
	}

	/**
	 * Gets the value for a field.
	 * @param field The field to get.
	 * @return The value, defaults to -1.
	 */
	private long getLong(Field<?> field)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context.select(field).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			long result = Long.parseLong(String.valueOf(query.fetchOne(field)));
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	/**
	 * Sets the new value for a field.
	 * @param field The field to set.
	 * @param value The value to set.
	 * @param <T> The type to set.
	 */
	private <T> void setValue(Field<T> field, T value)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			context.update(Tables.GUILDS).set(field, value).where(GUILDS.GUILD_ID.eq(guildId)).execute();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
