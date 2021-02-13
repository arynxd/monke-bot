package me.arynxd.monkebot.util;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.objects.jooq.Tables;

import me.arynxd.monkebot.objects.jooq.tables.Guilds;
import me.arynxd.monkebot.objects.jooq.tables.Levels;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Tempbans;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Votes;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);

	private DatabaseUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void removeGuild(Guild guild, Monke monke)
	{
		LOGGER.debug("Removed guild " + guild.getId());
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(Guilds.GUILDS.GUILD_ID.eq(guild.getIdLong()));
			context.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void removeGuild(long guildId, Monke monke)
	{
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(Guilds.GUILDS.GUILD_ID.eq(guildId));
			context.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(Guild guild, Monke monke)
	{
		LOGGER.debug("Registered guild " + guild.getId());
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILD_ID)
					.values(guild.getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(long guildId, Monke monke)
	{
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILD_ID)
					.values(guildId)
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static List<Tempbans> getExpiredTempbans(Monke monke)
	{
		List<Tempbans> result = new ArrayList<>();
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection).selectFrom(Tables.TEMPBANS);

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
			monke.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static List<Role> getRoleForLevel(Guild guild, int level, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.LEVELS)
					.where(Levels.LEVELS.AWARDED_AT.eq(level).and(Levels.LEVELS.GUILD_ID.eq(guild.getIdLong())));

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
			monke.getLogger().error("An SQL error occurred", exception);
			return Collections.emptyList();
		}
	}

	public static List<Votes> getExpiredVotes(Monke monke)
	{
		List<Votes> result = new ArrayList<>();
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection).selectFrom(Tables.VOTES);

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
			monke.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}
}
