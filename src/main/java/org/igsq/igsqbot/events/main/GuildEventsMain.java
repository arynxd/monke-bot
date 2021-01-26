package org.igsq.igsqbot.events.main;

import java.util.List;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.DatabaseUtils;
import org.jetbrains.annotations.NotNull;

public class GuildEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public GuildEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event)
	{
		igsqBot.getMinecraft().getSync().syncMember(event.getMember());
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		igsqBot.getMinecraft().getSync().removeMember(event.getUser().getIdLong());
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
	{
		igsqBot.getMinecraft().getSync().syncMember(event.getMember());
	}

	@Override
	public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event)
	{
		igsqBot.getMinecraft().getSync().syncMember(event.getMember());
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		igsqBot.getMinecraft().getSync().syncMember(event.getMember());
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuild(), igsqBot);
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuildIdLong(), igsqBot);
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		DatabaseUtils.registerGuild(event.getGuild(), igsqBot);
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), igsqBot);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if(event.getGuild().getSelfMember().canInteract(role))
				{
					event.retrieveMember().queue(reactionRole::addRole);
				}
			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), igsqBot);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if(event.getGuild().getSelfMember().canInteract(role))
				{
					event.retrieveMember().queue(reactionRole::removeRole);
				}
			}
		}
	}
}

