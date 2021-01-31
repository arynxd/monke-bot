package me.arynxd.monkebot.events.main;

import java.util.List;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.ReactionRole;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import me.arynxd.monkebot.util.DatabaseUtils;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildEventsMain extends ListenerAdapter
{
	private final Monke monke;

	public GuildEventsMain(Monke monke)
	{
		this.monke = monke;
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuild(), monke);
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuildIdLong(), monke);
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		DatabaseUtils.registerGuild(event.getGuild(), monke);
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), monke);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if(role != null)
				{
					if(event.getGuild().getSelfMember().canInteract(role))
					{
						event.retrieveMember().queue(reactionRole::addRole);
					}
				}
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event)
	{
		if(event.getMember().equals(event.getGuild().getSelfMember()))
		{
			GuildMusicManager manager = monke.getMusicHandler().getGuildMusicManager(event.getGuild());
			manager.getPlayer().destroy();
			manager.leave(event.getGuild());
			manager.getScheduler().clear();
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), monke);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if(role != null)
				{
					if(event.getGuild().getSelfMember().canInteract(role))
					{
						event.retrieveMember().queue(reactionRole::removeRole);
					}
				}
			}
		}
	}
}

