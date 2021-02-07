package me.arynxd.monkebot.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.jooq.Tables;
import me.arynxd.monkebot.entities.jooq.tables.ChannelBlacklists;
import me.arynxd.monkebot.entities.jooq.tables.WordBlacklists;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
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
		String content = event.getMessage().getContentRaw().toLowerCase();
		Member member = event.getMember();

		if(member == null)
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
