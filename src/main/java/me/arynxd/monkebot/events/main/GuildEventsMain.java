package me.arynxd.monkebot.events.main;

import java.time.Instant;
import java.util.List;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.objects.bot.ConfigOption;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.cache.MessageCache;
import me.arynxd.monkebot.objects.database.ReactionRole;
import me.arynxd.monkebot.objects.info.BotInfo;
import me.arynxd.monkebot.util.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
		GuildSettingsCache.removeCache(event.getGuild().getIdLong());
		MessageCache.removeCache(event.getGuild().getIdLong());

		String[] config = monke.getConfiguration().getString(ConfigOption.LOG_CHANNEL).split("(, *)");

		if (config.length < 2)
		{
			return;
		}

		Guild logGuild = monke.getShardManager().getGuildById(config[0]);
		if (logGuild != null)
		{
			MessageChannel logChannel = logGuild.getTextChannelById(config[1]);
			if (logChannel != null)
			{
				logChannel.sendMessage(new EmbedBuilder()
						.setTitle("Left a server.")
						.setDescription("server name: " + event.getGuild().getName()
								+ "\n\nTotal servers: " + BotInfo.getGuildCount(event.getJDA().getShardManager()))
						.setColor(Constants.EMBED_COLOUR)
						.setTimestamp(Instant.now())
						.build()).queue();
			}
		}
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuildIdLong(), monke);
		GuildSettingsCache.removeCache(event.getGuildIdLong());
		MessageCache.removeCache(event.getGuildIdLong());
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		DatabaseUtils.registerGuild(event.getGuild(), monke);

		String[] config = monke.getConfiguration().getString(ConfigOption.LOG_CHANNEL).split("(, *)");

		if (config.length < 2)
		{
			return;
		}

		Guild logGuild = monke.getShardManager().getGuildById(config[0]);
		if (logGuild != null)
		{
			MessageChannel logChannel = logGuild.getTextChannelById(config[1]);
			if (logChannel != null)
			{
				logChannel.sendMessage(new EmbedBuilder()
						.setTitle("Joined a server.")
						.setDescription("server name: " + event.getGuild().getName()
								+ "\n\nTotal servers: " + BotInfo.getGuildCount(event.getJDA().getShardManager()))
						.setColor(Constants.EMBED_COLOUR)
						.setTimestamp(Instant.now())
						.build()).queue();
			}
		}
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), monke);

		for (ReactionRole reactionRole : reactionRoles)
		{
			if (reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if (role != null)
				{
					if (event.getGuild().getSelfMember().canInteract(role))
					{
						event.retrieveMember().queue(reactionRole::addRole);
					}
				}
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		long humansInVC = event.getChannelLeft().getMembers().stream().filter(member -> !member.getUser().isBot()).count();
		if (event.getMember().equals(event.getGuild().getSelfMember()) || humansInVC == 0)
		{
			monke.getMusicHandler().cleanupPlayer(event.getGuild());
		}
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event)
	{
		long humansInVC = event.getChannelLeft().getMembers().stream().filter(member -> !member.getUser().isBot()).count();
		if (event.getMember().equals(event.getGuild().getSelfMember()) || humansInVC == 0)
		{
			monke.getMusicHandler().cleanupPlayer(event.getGuild());
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), monke);

		for (ReactionRole reactionRole : reactionRoles)
		{
			if (reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				Role role = event.getGuild().getRoleById(reactionRole.getRoleId());
				if (role != null)
				{
					if (event.getGuild().getSelfMember().canInteract(role))
					{
						event.retrieveMember().queue(reactionRole::removeRole);
					}
				}
			}
		}
	}
}

