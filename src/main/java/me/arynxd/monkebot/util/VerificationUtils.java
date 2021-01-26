package me.arynxd.monkebot.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.jooq.tables.Verification;
import net.dv8tion.jda.api.entities.Guild;

public class VerificationUtils
{
	private VerificationUtils()
	{
		//Overrides the default, public. constructor
	}

	public static Map<String, Long> getMappedPhrases(Guild guild, Monke monke)
	{
		Map<String, Long> result = new HashMap<>();

		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var ctx = monke.getDatabaseHandler().getContext(connection);

			var query = ctx
					.selectFrom(Verification.VERIFICATION)
					.where(Verification.VERIFICATION.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.put(row.getPhrase(), row.getRoleId());
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
		result.forEach((phrase, role) -> System.out.println(phrase + " -> " + role));
		return result;
	}

	public static boolean addMapping(String phrase, long guildId, long roleId, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var ctx = monke.getDatabaseHandler().getContext(connection);
			var existsQuery = ctx
					.selectFrom(Verification.VERIFICATION)
					.where(Verification.VERIFICATION.ROLE_ID.eq(roleId))
					.and(Verification.VERIFICATION.PHRASE.eq(phrase));

			if(existsQuery.fetch().isNotEmpty())
			{
				return false;
			}

			var insertQuery = ctx.insertInto(Verification.VERIFICATION)
					.columns(Verification.VERIFICATION.GUILD_ID, Verification.VERIFICATION.ROLE_ID, Verification.VERIFICATION.PHRASE)
					.values(guildId, roleId, phrase);

			return insertQuery.execute() > 0;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean removeMapping(String phrase, long guildId, long roleId, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var ctx = monke.getDatabaseHandler().getContext(connection);
			var existsQuery = ctx.selectFrom(Verification.VERIFICATION).where(Verification.VERIFICATION.ROLE_ID.eq(roleId));

			if(existsQuery.fetch().isEmpty())
			{
				return false;
			}

			var insertQuery = ctx.deleteFrom(Verification.VERIFICATION)
					.where(Verification.VERIFICATION.GUILD_ID.eq(guildId)
							.and(Verification.VERIFICATION.ROLE_ID.eq(roleId)
									.and(Verification.VERIFICATION.PHRASE.eq(phrase))));

			return insertQuery.execute() > 0;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
