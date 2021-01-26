package org.igsq.igsqbot.events.main;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.DatabaseUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public class MessageEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		MessageChannel channel = event.getChannel();
		if(event.isFromGuild())
		{
			Guild guild = event.getGuild();

			if(BlacklistUtils.isChannelBlacklisted(event, igsqBot))
			{
				return;
			}

			if(!event.getAuthor().isBot())
			{
				MessageCache.getCache(guild).set(new CachedMessage(event.getMessage()));
			}

			if(BlacklistUtils.isDiscordInvite(event))
			{
				EmbedUtils.sendError(channel, "You cannot advertise Discord servers.");
				if(guild.getSelfMember().hasPermission((GuildChannel) event.getChannel(), Permission.MESSAGE_MANAGE))
				{
					event.getMessage().delete().queue();
				}
				return;
			}

			if(BlacklistUtils.isAdvertising(event, igsqBot))
			{
				EmbedUtils.sendError(channel, "You cannot advertise here.");
				if(guild.getSelfMember().hasPermission((GuildChannel) event.getChannel(), Permission.MESSAGE_MANAGE))
				{
					event.getMessage().delete().queue();
				}
				return;
			}

			if(!event.isWebhookMessage())
			{
				if(CommandUtils.getLevelUp(event, igsqBot) != -1)
				{
					List<Role> newRoles = DatabaseUtils.getRoleForLevel(guild, CommandUtils.getLevelUp(event, igsqBot), igsqBot);
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
		}

		if(handleVote(event))
		{
			return;
		}

		igsqBot.getCommandHandler().handle(event);
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
		boolean isRunning = Vote.isVoteRunning(messageId, igsqBot);

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
				int maxOptions = Vote.getMaxVoteById(messageId, igsqBot);
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

				if(Vote.castById(event.getAuthor().getIdLong(), messageId, option, igsqBot))
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

