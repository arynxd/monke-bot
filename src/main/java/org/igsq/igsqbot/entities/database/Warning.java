package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;

import static org.igsq.igsqbot.entities.jooq.tables.Warnings.WARNINGS;

public class Warning
{
	private final long userId;
	private final long guildId;
	private final IGSQBot igsqBot;

	public Warning(Guild guild, User user, IGSQBot igsqBot)
	{
		this.guildId = guild.getIdLong();
		this.userId = user.getIdLong();
		this.igsqBot = igsqBot;
	}

	public void add(String reason)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.insertInto(Tables.WARNINGS).columns(WARNINGS.GUILD_ID, WARNINGS.USER_ID, WARNINGS.WARN_TEXT).values(guildId, userId, reason);
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public void remove(long key)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			context.deleteFrom(Tables.WARNINGS).where(WARNINGS.ID.eq(key)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public List<Warnings> get()
	{
		List<org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.WARNINGS).where(WARNINGS.GUILD_ID.eq(guildId)).and(WARNINGS.USER_ID.eq(userId));

			for(var value : query.fetch())
			{
				result.add(new org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings(value.getId(), value.getUserId(), value.getGuildId(), value.getTimestamp(), value.getWarnText()));
			}

		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}

		return result;
	}

	public Warnings getByWarnId(long warnId)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.selectFrom(WARNINGS)
					.where(WARNINGS.ID.eq(warnId));

			var result = context.fetch();
			context.close();
			if(!result.isEmpty())
			{
				var warn = result.get(0);
				return new Warnings(warn.getId(), warn.getUserId(), warn.getGuildId(), warn.getTimestamp(), warn.getWarnText());
			}
			else
			{
				return null;
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	public boolean isPresent(long warnId)
	{
		return getByWarnId(warnId) != null;
	}
}
