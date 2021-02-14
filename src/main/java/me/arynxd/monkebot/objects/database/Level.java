package me.arynxd.monkebot.objects.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.objects.jooq.Tables;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Levels;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import static me.arynxd.monkebot.objects.jooq.tables.Levels.LEVELS;

public class Level
{
	private Level()
	{
		//Overrides the default, public, constructor
	}

	public static void addLevel(@NotNull Role role, @NotNull Integer level, long guildId, @NotNull Monke igsqbot)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.LEVELS)
					.columns(LEVELS.GUILD_ID, LEVELS.ROLE_ID, LEVELS.AWARDED_AT)
					.values(guildId, role.getIdLong(), level);

			query.execute();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean removeLevel(@NotNull Role role, @NotNull Integer level, long guildId, @NotNull Monke igsqbot)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.LEVELS)
					.where(LEVELS.GUILD_ID.eq(guildId)
							.and(LEVELS.ROLE_ID.eq(role.getIdLong()))
							.and(LEVELS.AWARDED_AT.eq(level)));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static @NotNull List<Levels> getLevels(long guildId, @NotNull Monke igsqbot)
	{
		List<Levels> result = new ArrayList<>();
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.LEVELS)
					.where(LEVELS.GUILD_ID.eq(guildId));

			for(var row : query.fetch())
			{
				result.add(new Levels(row.getId(), row.getGuildId(), row.getRoleId(), row.getAwardedAt()));
			}
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}
}
