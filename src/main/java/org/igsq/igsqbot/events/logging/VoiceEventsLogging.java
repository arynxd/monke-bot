package org.igsq.igsqbot.events.logging;

import java.time.Instant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.GuildConfig;

public class VoiceEventsLogging extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public VoiceEventsLogging(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event)
	{
		Guild guild = event.getGuild();
		VoiceChannel oldChannel = event.getChannelLeft();
		VoiceChannel newChannel = event.getChannelJoined();
		Member member = event.getMember();
		MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, igsqBot).getLogChannel());

		if(logChannel != null)
		{
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Moved VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Old Channel**: " + oldChannel.getName() + "\n" +
							"**New Channel**: " + newChannel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}


	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		VoiceChannel channel = event.getChannelLeft();
		Member member = event.getMember();
		Guild guild = event.getGuild();
		MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, igsqBot).getLogChannel());

		if(logChannel != null)
		{
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Left VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
		}


	}


	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
	{
		VoiceChannel channel = event.getChannelJoined();
		Member member = event.getMember();
		Guild guild = event.getGuild();
		MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, igsqBot).getLogChannel());

		if(logChannel != null)
		{
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Joined VC")
					.setDescription("**Member**: " + member.getAsMention() + "\n" +
							"**Channel**: " + channel.getName())
					.setColor(Constants.IGSQ_PURPLE)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}
}
