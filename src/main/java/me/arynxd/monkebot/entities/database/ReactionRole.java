package me.arynxd.monkebot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import me.arynxd.monkebot.Monke;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import static me.arynxd.monkebot.entities.jooq.tables.ReactionRoles.REACTION_ROLES;

/**
 * Controls ReactionRoles.
 */
public class ReactionRole
{
	private final long messageId;
	private final long roleId;
	private final String emote;
	private final Monke igsqbot;
	private final long guildId;

	/**
	 * Constructs a new reaction role.
	 *
	 * @param messageId The messageId.
	 * @param roleId    The roleId.
	 * @param guildId   The guildId.
	 * @param emote     The emote / emoji.
	 * @param monke     The {@link me.arynxd.monkebot.Monke monke} instance.
	 */
	public ReactionRole(@Nonnull Long messageId, @Nonnull Long roleId, @Nonnull Long guildId, @Nonnull String emote, @Nonnull Monke monke)
	{
		this.messageId = messageId;
		this.roleId = roleId;
		this.guildId = guildId;
		this.emote = emote;
		this.igsqbot = monke;
	}

	/**
	 * Gets all reaction roles associated with a messageId.
	 *
	 * @param messageId The messageId.
	 * @param igsqbot   The {@link me.arynxd.monkebot.Monke igsqbot} instance.
	 * @return The reaction roles.
	 */
	@Nonnull
	public static List<ReactionRole> getByMessageId(@Nonnull Long messageId, @Nonnull Monke igsqbot)
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId));

			var result = query.fetch();
			query.close();

			if(result.isEmpty())
			{
				return Collections.emptyList();
			}
			else
			{
				List<ReactionRole> reactionRoles = new ArrayList<>();
				for(var rr : result)
				{
					reactionRoles.add(new ReactionRole(rr.getMessageId(), rr.getRoleId(), rr.getGuildId(), rr.getEmoteId(), igsqbot));
				}
				return reactionRoles;
			}
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
			return Collections.emptyList();
		}
	}

	/**
	 * Adds this reaction role to the database.
	 *
	 * @see #getByMessageId(Long, me.arynxd.monkebot.Monke)
	 */
	public void add()
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(REACTION_ROLES)
					.columns(REACTION_ROLES.GUILD_ID, REACTION_ROLES.MESSAGE_ID, REACTION_ROLES.ROLE_ID, REACTION_ROLES.EMOTE_ID)
					.values(guildId, messageId, roleId, emote);
			query.execute();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
	}

	/**
	 * @return The messageId.
	 */
	@Nonnull
	public Long getMessageId()
	{
		return messageId;
	}

	/**
	 * @return The roleId.
	 */
	@Nonnull
	public Long getRoleId()
	{
		return roleId;
	}

	/**
	 * @return The emote / emoji.
	 */
	@Nonnull
	public String getEmote()
	{
		return emote;
	}

	/**
	 * @return The guildId.
	 */
	@Nonnull
	public Long getGuildId()
	{
		return guildId;
	}

	/**
	 * Adds the {@link net.dv8tion.jda.api.entities.Role role} to the given member.
	 * <p>
	 * This will not succeed if the role or guild is null.
	 *
	 * @param member The member to apply the role to.
	 */
	public void addRole(Member member)
	{
		Guild guild = igsqbot.getShardManager().getGuildById(guildId);
		if(guild != null)
		{
			Role role = guild.getRoleById(roleId);
			if(role != null)
			{
				guild.addRoleToMember(member, role).queue();
			}
		}
	}

	/**
	 * Remove the {@link net.dv8tion.jda.api.entities.Role role} from the given member.
	 * <p>
	 * This will not succeed if the role or guild is null.
	 *
	 * @param member The member to remove the role from.
	 */
	public void removeRole(Member member)
	{
		Guild guild = igsqbot.getShardManager().getGuildById(guildId);
		if(guild != null)
		{
			Role role = guild.getRoleById(roleId);
			if(role != null)
			{
				guild.removeRoleFromMember(member, role).queue();
			}
		}
	}

	/**
	 * Removes this reaction role from the database.
	 */
	public void remove()
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.ROLE_ID.eq(roleId)).and(REACTION_ROLES.EMOTE_ID.eq(emote)));
			query.execute();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
		}
	}

	/**
	 * @return If this reaction role exists in the database.
	 */
	@Nonnull
	public Boolean isPresent()
	{
		try(Connection connection = igsqbot.getDatabaseHandler().getConnection())
		{
			var context = igsqbot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.EMOTE_ID.eq(emote)).and(REACTION_ROLES.ROLE_ID.eq(roleId)));

			var result = query.fetch();
			query.close();
			return !result.isEmpty();
		}
		catch(Exception exception)
		{
			igsqbot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
