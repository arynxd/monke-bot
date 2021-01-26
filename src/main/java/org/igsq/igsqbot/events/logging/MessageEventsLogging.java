package org.igsq.igsqbot.events.logging;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.StringUtils;

public class MessageEventsLogging extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsLogging(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if(event.isFromGuild())
		{
			MessageCache cache = MessageCache.getCache(event.getGuild().getIdLong());

			if(cache.isInCache(event.getMessage()))
			{
				Message newMessage = event.getMessage();
				CachedMessage oldMessage = cache.get(event.getMessageIdLong());
				MessageChannel channel = event.getChannel();
				String oldContent = oldMessage.getContentRaw();
				String newContent = newMessage.getContentRaw();
				Guild guild = event.getGuild();
				MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, igsqBot).getLogChannel());

				if(logChannel != null)
				{
					if(CommandUtils.isValidCommand(newMessage.getContentRaw(), event.getGuild().getIdLong(), igsqBot))
					{
						return;
					}

					if(newMessage.getAuthor().isBot()) return;
					if(newContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";
					if(oldContent.length() >= 2000) newContent = newContent.substring(0, 1500) + " **...**";

					logChannel.sendMessage(new EmbedBuilder()
							.setTitle("Message Altered")
							.setDescription("**Author**: " + newMessage.getAuthor().getAsMention() +
									"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
									"\n**Sent On**: " + newMessage.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
									"\n\n**Message Content Before**: " + oldContent +
									"\n**Message Content After**: " + newContent)
							.setColor(Constants.IGSQ_PURPLE)
							.setTimestamp(Instant.now())
							.build()).queue();

					cache.update(oldMessage, new CachedMessage(newMessage));
				}
			}
		}
	}

	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT))
		{
			MessageCache cache = MessageCache.getCache(event.getGuild());

			if(cache.isInCache(event.getMessageIdLong()))
			{
				CachedMessage message = cache.get(event.getMessageIdLong());
				Guild guild = event.getGuild();
				MessageChannel channel = event.getChannel();
				String content = message.getContentRaw();
				MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, igsqBot).getLogChannel());

				if(logChannel != null)
				{
					if(CommandUtils.isValidCommand(content, guild.getIdLong(), igsqBot))
					{
						return;
					}
					if(message.getAuthor().isBot()) return;
					if(content.length() >= 2000) content = content.substring(0, 1500) + " **...**";


					logChannel.sendMessage(new EmbedBuilder()
							.setTitle("Message Deleted")
							.setDescription("**Author**: " + message.getAuthor().getAsMention() +
									"\n**Sent In**: " + StringUtils.getChannelAsMention(channel.getId()) +
									"\n**Sent On**: " + message.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
									"\n\n**Message Content**: " + content)
							.setColor(Constants.IGSQ_PURPLE)
							.setTimestamp(Instant.now())
							.build()).queue();
					cache.remove(message);
				}
			}
		}
	}
}

