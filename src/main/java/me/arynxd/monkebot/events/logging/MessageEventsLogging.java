package me.arynxd.monkebot.events.logging;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.objects.cache.CachedMessage;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.cache.MessageCache;
import me.arynxd.monkebot.util.CommandUtils;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEventsLogging extends ListenerAdapter
{
	private final Monke monke;

	public MessageEventsLogging(Monke monke)
	{
		this.monke = monke;
	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event)
	{
		if(event.isFromGuild())
		{
			MessageCache cache = MessageCache.getCache(event.getGuild().getIdLong());

			if(cache.isCached(event.getMessage().getIdLong()))
			{
				Message newMessage = event.getMessage();
				CachedMessage oldMessage = cache.get(event.getMessageIdLong());
				MessageChannel channel = event.getChannel();
				String oldContent = oldMessage.getContentRaw();
				String newContent = newMessage.getContentRaw();
				Guild guild = event.getGuild();
				MessageChannel logChannel = guild.getTextChannelById(GuildSettingsCache.getCache(guild.getIdLong(), monke).getLogChannel());

				if(logChannel != null)
				{
					if(CommandUtils.isValidCommand(newMessage.getContentRaw(), event.getGuild().getIdLong(), monke))
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
							.setColor(Constants.EMBED_COLOUR)
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
			MessageCache cache = MessageCache.getCache(event.getGuild().getIdLong());

			if(cache.isCached(event.getMessageIdLong()))
			{
				CachedMessage message = cache.get(event.getMessageIdLong());
				Guild guild = event.getGuild();
				MessageChannel channel = event.getChannel();
				String content = message.getContentRaw();
				MessageChannel logChannel = guild.getTextChannelById(GuildSettingsCache.getCache(guild.getIdLong(), monke).getLogChannel());

				if(logChannel != null)
				{
					if(CommandUtils.isValidCommand(content, guild.getIdLong(), monke))
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
							.setColor(Constants.EMBED_COLOUR)
							.setTimestamp(Instant.now())
							.build()).queue();
					cache.remove(message);
				}
			}
		}
	}
}

