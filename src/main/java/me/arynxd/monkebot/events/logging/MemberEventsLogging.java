package me.arynxd.monkebot.events.logging;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.GuildConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import me.arynxd.monkebot.Constants;

public class MemberEventsLogging extends ListenerAdapter
{
	private final Monke monke;

	public MemberEventsLogging(Monke monke)
	{
		this.monke = monke;
	}

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event)
	{
		Guild guild = event.getGuild();
		User user = event.getUser();
		MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, monke).getLogChannel());

		if(logChannel != null)
		{
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Left")
					.setDescription("**Member**: " + user.getAsMention())
					.setColor(Constants.EMBED_COLOUR)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event)
	{
		Guild guild = event.getGuild();
		Member member = event.getMember();


		MessageChannel logChannel = guild.getTextChannelById(new GuildConfig(guild, monke).getLogChannel());

		if(logChannel != null)
		{
			logChannel.sendMessage(new EmbedBuilder()
					.setTitle("Member Joined")
					.setDescription("**Member**: " + member.getAsMention() +
							"\n**Joined On**: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
					.setColor(Constants.EMBED_COLOUR)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}
}
