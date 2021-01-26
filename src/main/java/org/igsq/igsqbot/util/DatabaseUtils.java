package org.igsq.igsqbot.util;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Tempbans;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Votes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;
import static org.igsq.igsqbot.entities.jooq.tables.Levels.LEVELS;

public class DatabaseUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

	private DatabaseUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void removeGuild(Guild guild, IGSQBot igsqBot)
	{
		LOGGER.debug("Removed guild " + guild.getId());
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(GUILDS.GUILD_ID.eq(guild.getIdLong()));
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void removeGuild(long guildId, IGSQBot igsqBot)
	{
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(GUILDS.GUILD_ID.eq(guildId));
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(Guild guild, IGSQBot igsqBot)
	{
		LOGGER.debug("Registered guild " + guild.getId());
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(GUILDS.GUILD_ID)
					.values(guild.getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(long guildId, IGSQBot igsqBot)
	{
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(GUILDS.GUILD_ID)
					.values(guildId)
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static List<Tempbans> getExpiredTempbans(IGSQBot igsqBot)
	{
		List<Tempbans> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection).selectFrom(Tables.TEMPBANS);

			for(var value : context.fetch())
			{
				if(value.getMutedUntil().isBefore(LocalDateTime.now()))
				{
					result.add(new Tempbans(value.getId(), value.getUserId(), value.getGuildId(), value.getMutedUntil()));
				}
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static List<Role> getRoleForLevel(Guild guild, int level, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.LEVELS)
					.where(LEVELS.AWARDED_AT.eq(level).and(LEVELS.GUILD_ID.eq(guild.getIdLong())));

			var result = query.fetch();

			if(result.isEmpty())
			{
				return Collections.emptyList();
			}

			List<Role> roles = new ArrayList<>();

			result.forEach(role -> roles.add(guild.getRoleById(role.getRoleId())));
			return roles;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return Collections.emptyList();
		}
	}

	public static List<Votes> getExpiredVotes(IGSQBot igsqBot)
	{
		List<Votes> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection).selectFrom(Tables.VOTES);

			for(var value : context.fetch())
			{
				if(value.getExpiry().isBefore(LocalDateTime.now()))
				{
					result.add(new Votes(value.getId(), value.getVoteId(), value.getGuildId(), value.getDirectMessageId(), value.getUserId(), value.getOption(), value.getMaxOptions(), value.getExpiry(), value.getHasVoted()));
				}
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}
}
