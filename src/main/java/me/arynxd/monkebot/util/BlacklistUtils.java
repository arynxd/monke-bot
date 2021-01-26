package me.arynxd.monkebot.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.entities.jooq.Tables;
import me.arynxd.monkebot.entities.jooq.tables.ChannelBlacklists;
import me.arynxd.monkebot.entities.jooq.tables.WordBlacklists;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BlacklistUtils
{
	public static final List<String> LINKS = List.copyOf(List.of("youtube.com", "twitch.tv", "youtu.be", "https://", "http://", "www."));
	public static final List<String> DISCORD = List.copyOf(List.of("discord.gg/"));
	private BlacklistUtils()
	{
		//Overrides the default, public, constructor
	}

	public static boolean isBlacklistedPhrase(MessageReceivedEvent event, Monke monke)
	{
		if(!event.isFromGuild())
		{
			return false;
		}

		Guild guild = event.getGuild();
		String content = event.getMessage().getContentRaw();
		Member member = event.getMember();

		if(member == null || member.hasPermission(Permission.ADMINISTRATOR))
		{
			return false;
		}

		return getBlacklistedPhrases(guild, monke).stream().anyMatch(phrase -> content.contains(phrase.toLowerCase()));
	}

	public static boolean isChannelBlacklisted(MessageReceivedEvent event, Monke monke)
	{
		if(!event.isFromGuild())
		{
			return false;
		}
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.CHANNEL_BLACKLISTS)
					.where(ChannelBlacklists.CHANNEL_BLACKLISTS.CHANNEL_ID.eq(event.getChannel().getIdLong())
							.and(ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID.eq(event.getGuild().getIdLong())));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean isAdvertising(MessageReceivedEvent event, Monke monke)
	{
		if(!event.isFromGuild())
		{
			return false;
		}
		Guild guild = event.getGuild();
		String content = event.getMessage().getContentRaw();
		Member member = event.getMember();

		MessageChannel promoChannel = guild.getTextChannelById(new GuildConfig(guild.getIdLong(), monke).getPromoChannel());
		Role promoBypass = guild.getRoleById(new GuildConfig(guild.getIdLong(), monke).getPromoRole());

		if(promoChannel == null || promoBypass == null)
		{
			return false;
		}


		if(findLink(content))
		{
			if(member == null)
			{
				return true;
			}

			if(member.hasPermission(Permission.MESSAGE_MANAGE) || !event.getChannel().equals(promoChannel))
			{
				return false;
			}

			return event.getChannel().equals(promoChannel) && !member.getRoles().contains(promoBypass);
		}
		return false;
	}

	private static boolean findLink(String content)
	{
		String finalContent = content.replaceAll("\\s+", "").toLowerCase();
		return LINKS.stream().anyMatch(word -> finalContent.contains(word.toLowerCase()));
	}

	public static boolean isDiscordInvite(MessageReceivedEvent event)
	{
		if(!event.isFromGuild())
		{
			return false;
		}
		Member member = event.getMember();

		if(member == null || member.hasPermission(Permission.MESSAGE_MANAGE))
		{
			return false;
		}

		String content = event.getMessage().getContentRaw().replaceAll("\\s+", "").toLowerCase();

		return DISCORD.stream().anyMatch(phrase -> content.contains(phrase.toLowerCase()));
	}

	public static List<String> getBlacklistedPhrases(Guild guild, Monke monke)
	{
		List<String> result = new ArrayList<>();
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.WORD_BLACKLISTS)
					.where(WordBlacklists.WORD_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.add(row.getPhrase());
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static void addPhrase(Guild guild, String phrase, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.WORD_BLACKLISTS)
					.columns(WordBlacklists.WORD_BLACKLISTS.GUILD_ID, WordBlacklists.WORD_BLACKLISTS.PHRASE)
					.values(guild.getIdLong(), phrase);

			query.execute();
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean addChannel(MessageChannel channel, Guild guild, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.CHANNEL_BLACKLISTS)
					.columns(ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID, ChannelBlacklists.CHANNEL_BLACKLISTS.CHANNEL_ID)
					.values(guild.getIdLong(), channel.getIdLong());

			var exists = context
					.selectFrom(Tables.CHANNEL_BLACKLISTS)
					.where(ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong())
							.and(ChannelBlacklists.CHANNEL_BLACKLISTS.CHANNEL_ID.eq(channel.getIdLong())));

			if(exists.fetch().isNotEmpty())
			{
				return false;
			}

			query.execute();
			return true;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static List<me.arynxd.monkebot.entities.jooq.tables.pojos.ChannelBlacklists> getBlacklistedChannels(Guild guild, Monke monke)
	{
		List<me.arynxd.monkebot.entities.jooq.tables.pojos.ChannelBlacklists> result = new ArrayList<>();
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(ChannelBlacklists.CHANNEL_BLACKLISTS)
					.where(ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.add(new me.arynxd.monkebot.entities.jooq.tables.pojos.ChannelBlacklists(row.getId(), row.getGuildId(), row.getChannelId()));
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static boolean removeChannel(MessageChannel channel, Guild guild, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.CHANNEL_BLACKLISTS)
					.where(ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong())
							.and(ChannelBlacklists.CHANNEL_BLACKLISTS.CHANNEL_ID.eq(channel.getIdLong())));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean removePhrase(Guild guild, String phrase, Monke monke)
	{
		try(Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.WORD_BLACKLISTS)
					.where(WordBlacklists.WORD_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()).and(WordBlacklists.WORD_BLACKLISTS.PHRASE.eq(phrase)));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
