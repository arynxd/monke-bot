package me.arynxd.monkebot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.arynxd.monkebot.Monke;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import static me.arynxd.monkebot.entities.jooq.tables.ReactionRoles.REACTION_ROLES;

public class ReactionRole
{
	private final long messageId;
	private final long roleId;
	private final String emote;
	private final Monke igsqbot;
	private final long guildId;

	public ReactionRole(@NotNull Long messageId, @NotNull Long roleId, @NotNull Long guildId, @NotNull String emote, @NotNull Monke monke)
	{
		this.messageId = messageId;
		this.roleId = roleId;
		this.guildId = guildId;
		this.emote = emote;
		this.igsqbot = monke;
	}

	public static @NotNull List<ReactionRole> getByMessageId(@NotNull Long messageId, @NotNull Monke igsqbot)
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

	public @NotNull Long getMessageId()
	{
		return messageId;
	}

	public @NotNull Long getRoleId()
	{
		return roleId;
	}

	public @NotNull String getEmote()
	{
		return emote;
	}

	public @NotNull Long getGuildId()
	{
		return guildId;
	}

	public void addRole(@NotNull Member member)
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

	public void removeRole(@NotNull Member member)
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

	public @NotNull Boolean isPresent()
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
