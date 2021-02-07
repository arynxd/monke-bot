package me.arynxd.monkebot.events.main;

import java.util.List;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.cache.CachedMessage;
import me.arynxd.monkebot.entities.cache.MessageCache;
import me.arynxd.monkebot.entities.database.Vote;
import me.arynxd.monkebot.util.BlacklistUtils;
import me.arynxd.monkebot.util.CommandUtils;
import me.arynxd.monkebot.util.DatabaseUtils;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventsMain extends ListenerAdapter
{
	private final Monke monke;

	public MessageEventsMain(Monke monke)
	{
		this.monke = monke;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.isFromGuild() && !event.getAuthor().isBot() && !event.isWebhookMessage())
		{
			Guild guild = event.getGuild();

			if(BlacklistUtils.isChannelBlacklisted(event, monke))
			{
				return;
			}

			MessageCache.getCache(guild.getIdLong()).put(new CachedMessage(event.getMessage()));

			if(CommandUtils.getLevelUp(event, monke) != -1)
			{
				List<Role> newRoles = DatabaseUtils.getRoleForLevel(guild, CommandUtils.getLevelUp(event, monke), monke);
				Member member = event.getMessage().getMentionedMembers().get(0);

				if(!newRoles.isEmpty())
				{
					Role highest = null;
					if(member.getRoles().isEmpty())
					{
						highest = event.getMessage().getMentionedMembers().get(0).getRoles().get(0);
					}

					for(Role role : newRoles)
					{
						if(role != null)
						{
							if(highest == null || highest.canInteract(role))
							{
								guild.addRoleToMember(member, role).queue();
							}
						}
					}
				}
				return;
			}

		}

		if(handleVote(event))
		{
			return;
		}

		monke.getCommandHandler().handleEvent(event);
	}

	private boolean handleVote(MessageReceivedEvent event)
	{
		if(!event.getChannelType().equals(ChannelType.PRIVATE) && !event.getAuthor().isBot())
		{
			return false;
		}
		if(event.getMessage().getReferencedMessage() == null)
		{
			return false;
		}

		String content = event.getMessage().getContentRaw();
		long messageId = event.getMessage().getReferencedMessage().getIdLong();
		boolean isRunning = Vote.isVoteRunning(messageId, monke);

		if(isRunning)
		{
			String[] opts = content.split(" ");

			if(opts.length < 1)
			{
				EmbedUtils.sendError(event.getChannel(), "You need to enter an option to vote for.");
				return true;
			}

			try
			{
				int maxOptions = Vote.getMaxVoteById(messageId, monke);
				int option;

				if(opts[0].startsWith("abstain"))
				{
					option = -2;
				}
				else
				{
					option = Integer.parseInt(opts[0]);
					if(option < 0)
					{
						EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
						return true;
					}
					if(option > maxOptions)
					{
						EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
						return true;
					}
				}

				if(Vote.castById(event.getAuthor().getIdLong(), messageId, option, monke))
				{
					EmbedUtils.sendSuccess(event.getChannel(), "Vote cast!");
					return true;
				}
			}
			catch(Exception exception)
			{
				EmbedUtils.sendError(event.getChannel(), "Invalid option entered.");
				return true;
			}
		}

		return true;
	}
}

