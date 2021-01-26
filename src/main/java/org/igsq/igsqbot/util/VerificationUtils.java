package org.igsq.igsqbot.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;

import static org.igsq.igsqbot.entities.jooq.tables.Verification.VERIFICATION;

public class VerificationUtils
{
	private VerificationUtils()
	{
		//Overrides the default, public. constructor
	}

	public static Map<String, Long> getMappedPhrases(Guild guild, IGSQBot igsqBot)
	{
		Map<String, Long> result = new HashMap<>();

		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);

			var query = ctx
					.selectFrom(VERIFICATION)
					.where(VERIFICATION.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.put(row.getPhrase(), row.getRoleId());
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		result.forEach((phrase, role) -> System.out.println(phrase + " -> " + role));
		return result;
	}

	public static boolean addMapping(String phrase, long guildId, long roleId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			var existsQuery = ctx
					.selectFrom(VERIFICATION)
					.where(VERIFICATION.ROLE_ID.eq(roleId))
					.and(VERIFICATION.PHRASE.eq(phrase));

			if(existsQuery.fetch().isNotEmpty())
			{
				return false;
			}

			var insertQuery = ctx.insertInto(VERIFICATION)
					.columns(VERIFICATION.GUILD_ID, VERIFICATION.ROLE_ID, VERIFICATION.PHRASE)
					.values(guildId, roleId, phrase);

			return insertQuery.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean removeMapping(String phrase, long guildId, long roleId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			var existsQuery = ctx.selectFrom(VERIFICATION).where(VERIFICATION.ROLE_ID.eq(roleId));

			if(existsQuery.fetch().isEmpty())
			{
				return false;
			}

			var insertQuery = ctx.deleteFrom(VERIFICATION)
					.where(VERIFICATION.GUILD_ID.eq(guildId)
							.and(VERIFICATION.ROLE_ID.eq(roleId)
									.and(VERIFICATION.PHRASE.eq(phrase))));

			return insertQuery.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
