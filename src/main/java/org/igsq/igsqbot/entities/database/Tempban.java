package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;

import static org.igsq.igsqbot.entities.jooq.tables.Roles.ROLES;
import static org.igsq.igsqbot.entities.jooq.tables.Tempbans.TEMPBANS;

public class Tempban
{
	public static boolean remove(long userId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			var existsQuery = ctx.selectFrom(Tables.TEMPBANS).where(TEMPBANS.USER_ID.eq(userId));

			if(existsQuery.fetch().isEmpty())
			{
				return false;
			}
			var roles = ctx.selectFrom(Tables.ROLES).where(ROLES.USER_ID.eq(userId));

			List<Role> collectedRoles = new ArrayList<>();
			Guild guild = null;
			for(var row : roles.fetch())
			{
				guild = igsqBot.getShardManager().getGuildById(row.getGuildId());
				if(guild != null)
				{
					Role role = guild.getRoleById(row.getRoleId());
					if(role != null)
					{
						if(!collectedRoles.contains(role))
						{
							collectedRoles.add(role);
						}
					}
				}
			}

			if(guild != null)
			{
				Guild finalGuild = guild;
				guild.retrieveMemberById(userId).queue(member -> finalGuild.modifyMemberRoles(member, collectedRoles).queue());
			}
			ctx.deleteFrom(Tables.TEMPBANS).where(TEMPBANS.USER_ID.eq(userId)).execute();
			ctx.deleteFrom(Tables.ROLES).where(ROLES.USER_ID.eq(userId)).execute();
			return true;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean add(long memberId, List<Long> roleIds, Guild guild, LocalDateTime mutedUntil, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);

			boolean exists = ctx.select(TEMPBANS.USER_ID).from(Tables.TEMPBANS).fetchOne() != null;
			if(exists)
			{
				return false;
			}

			for(long roleId : roleIds)
			{
				ctx.insertInto(Tables.ROLES).columns(ROLES.GUILD_ID, ROLES.USER_ID, ROLES.ROLE_ID).values(guild.getIdLong(), memberId, roleId).execute();
			}
			ctx.insertInto(Tables.TEMPBANS).columns(TEMPBANS.GUILD_ID, TEMPBANS.USER_ID, TEMPBANS.MUTED_UNTIL).values(guild.getIdLong(), memberId, mutedUntil).execute();

		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
		return true;
	}
}
