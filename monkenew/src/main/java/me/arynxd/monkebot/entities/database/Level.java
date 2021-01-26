package me.arynxd.monkebot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import me.arynxd.monkebot.Monke;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import me.arynxd.monkebot.entities.jooq.Tables;
import me.arynxd.monkebot.entities.jooq.tables.pojos.Levels;

import javax.annotation.Nonnull;

import static me.arynxd.monkebot.entities.jooq.tables.Levels.LEVELS;

/**
 * Controls {@link net.dv8tion.jda.api.entities.Role role} assigning level-ups for {@link net.dv8tion.jda.api.entities.Guild guilds}.
 */
public class Level
{
	private Level()
	{
		//Overrides the default, public, constructor
	}

	/**
	 * Adds a new level and {@link net.dv8tion.jda.api.entities.Role role} pair for the given guildId.
	 * @param role The {@link net.dv8tion.jda.api.entities.Role role}.
	 * @param level The level.
	 * @param guildId The guildId.
	 * @param igsqbot The {@link me.arynxd.monkebot.Monke monke} instance.
	 */
	public static void addLevel(@Nonnull Role role, @Nonnull Integer level, @Nonnull Long guildId, @Nonnull Monke igsqbot)
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

	/**
	 * Removes a new level and {@link net.dv8tion.jda.api.entities.Role role} pair for the given guildId.
	 * @param role The {@link net.dv8tion.jda.api.entities.Role role}.
	 * @param level The level.
	 * @param guildId The guildId.
	 * @param igsqbot The {@link me.arynxd.monkebot.Monke igsqbot} instance.
	 * @return {@code true} if successful, {@code false} otherwise.
	 */
	@Nonnull
	public static Boolean removeLevel(@Nonnull Role role, @Nonnull Integer level, @Nonnull Long guildId, @Nonnull Monke igsqbot)
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

	/**
	 * Gets all level and {@link net.dv8tion.jda.api.entities.Role role} pairs for a guildId.
	 * @param guildId The guildId.
	 * @param igsqbot The {@link me.arynxd.monkebot.Monke igsqbot} instance.
	 * @return The levels.
	 */
	@Nonnull
	public static List<Levels> getLevels(@Nonnull Long guildId, @Nonnull Monke igsqbot)
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

	/**
	 * Sets the level-up bot for a guildId.
	 * @param bot The new bot.
	 * @param guildId The guildId.
	 * @param igsqbot The {@link me.arynxd.monkebot.Monke igsqbot} instance.
	 */
	public void setBot(@Nonnull User bot, @Nonnull Long guildId, @Nonnull Monke igsqbot)
	{
		new GuildConfig(guildId, igsqbot).setLevelUpBot(bot.getIdLong());
	}
}
