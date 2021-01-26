package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.records.VotesRecord;
import org.igsq.igsqbot.util.StringUtils;
import org.jooq.Result;

import static org.igsq.igsqbot.entities.jooq.tables.Votes.VOTES;

public class Vote
{
	private final List<Long> users;
	private final List<String> options;
	private final LocalDateTime expiry;
	private final int maxOptions;
	private final IGSQBot igsqBot;
	private final CommandEvent ctx;
	private final String subject;

	public Vote(List<Long> users, List<String> options, LocalDateTime expiry, String subject, CommandEvent ctx)
	{
		this.users = users;
		this.options = options;
		this.expiry = expiry;
		this.subject = subject;
		this.maxOptions = options.size();
		this.igsqBot = ctx.getIGSQBot();
		this.ctx = ctx;
	}

	public void start()
	{
		MessageChannel voteChannel = ctx.getGuild().getTextChannelById(new GuildConfig(ctx).getVoteChannel());

		if(voteChannel == null)
		{
			ctx.replyError("Vote channel not setup");
			return;
		}

		voteChannel.sendMessage(generateGuildEmbed().build()).queue(
				message ->
				{
					message.editMessage(generateGuildEmbed().setFooter("Vote ID: " + message.getIdLong()).build()).queue();
					for(Long userId : users)
					{
						igsqBot.getShardManager()
								.retrieveUserById(userId)
								.flatMap(User::openPrivateChannel)
								.flatMap(channel -> channel.sendMessage(generateDMEmbed().build()))
								.queue(dm -> addUserToDatabase(userId, message.getIdLong(), dm.getIdLong()), error -> addUserToDatabase(userId, message.getIdLong(), -1));
					}
				});
	}

	private void addUserToDatabase(long userId, long voteId, long dmId)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.insertInto(Tables.VOTES)
					.columns(VOTES.VOTE_ID, VOTES.GUILD_ID, VOTES.USER_ID, VOTES.DIRECT_MESSAGE_ID, VOTES.MAX_OPTIONS, VOTES.EXPIRY, VOTES.OPTION)
					.values(voteId, ctx.getGuild().getIdLong(), userId, dmId, maxOptions, expiry, -1);
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	private EmbedBuilder generateDMEmbed()
	{
		return generateOptions(new EmbedBuilder()
				.setTitle("You have been called to vote in " + ctx.getGuild().getName())
				.addField("Expires at", StringUtils.parseDateTime(expiry), false)
				.setColor(Constants.IGSQ_PURPLE)
				.setDescription("Subject : **" + subject + "**\n\nInline Reply (right click / press and hold, then tap / click reply) to this message with 1,2,3.. to cast your vote, or `abstain` to abstain, if you do not respond, you will be considered 'Not voted'."));
	}

	private EmbedBuilder generateGuildEmbed()
	{
		return generateOptions(new EmbedBuilder()
				.setTitle(subject)
				.addField("Users", parseUserList(), false)
				.addField("Expires at", StringUtils.parseDateTime(expiry), false)
				.setColor(Constants.IGSQ_PURPLE));

	}

	private EmbedBuilder generateOptions(EmbedBuilder embed)
	{
		for(int i = 1; i < options.size() + 1; i++)
		{
			embed.addField("Option " + i, options.get(i - 1), true);
		}
		return embed;
	}

	private String parseUserList()
	{
		StringBuilder result = new StringBuilder();

		for(long user : users)
		{
			result.append(StringUtils.getUserAsMention(user)).append(" -> ").append("Not voted").append("\n");
		}
		return result.toString();
	}

	public static Boolean closeById(long voteId, CommandEvent ctx)
	{
		try(Connection connection = ctx.getIGSQBot().getDatabaseHandler().getConnection())
		{
			MessageChannel voteChannel = ctx.getGuild().getTextChannelById(new GuildConfig(ctx).getVoteChannel());

			if(voteChannel == null)
			{
				ctx.replyError("Vote channel not setup.");
				return null;
			}

			var context = ctx.getIGSQBot().getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.VOTES).where(VOTES.VOTE_ID.eq(voteId));
			var result = query.fetch();

			boolean voteExists = !result.isEmpty();

			if(!voteExists)
			{
				return false;
			}
			editVoteMessage(voteId, voteChannel, generateUsers(result));
			context.deleteFrom(Tables.VOTES).where(VOTES.VOTE_ID.eq(voteId)).execute();
			return true;
		}
		catch(Exception exception)
		{
			ctx.getIGSQBot().getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	private static String generateUsers(Result<VotesRecord> result)
	{
		StringBuilder votes = new StringBuilder();
		for(var row : result)
		{
			votes.append(StringUtils.getUserAsMention(row.getUserId())).append(" -> ");

			if(row.getOption() == -1)
			{
				votes.append(" Never voted ");
			}
			else if(row.getDirectMessageId() == -1)
			{
				votes.append(" DM could not be sent ");
			}
			else if(row.getOption() == -2)
			{
				votes.append(" Abstained ");
			}
			else
			{
				votes.append(parseOption(row.getOption()));
			}
			votes.append("\n");
		}
		return votes.toString();
	}


	public static void closeById(long voteId, long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.VOTES)
					.where(VOTES.VOTE_ID.eq(voteId));

			var result = query.fetch();
			boolean voteExists = !result.isEmpty();

			if(!voteExists)
			{
				return;
			}

			MessageChannel voteChannel = igsqBot.getShardManager().getTextChannelById(new GuildConfig(guildId, igsqBot).getVoteChannel());

			if(voteChannel == null)
			{
				return;
			}

			for(var user : result)
			{
				clearDM(user.getUserId(), user.getDirectMessageId(), igsqBot);
			}

			editVoteMessage(voteId, voteChannel, generateUsers(result));

			context.deleteFrom(Tables.VOTES).where(VOTES.VOTE_ID.eq(voteId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	private static void editVoteMessage(long messageId, MessageChannel voteChannel, String newUsers)
	{
		voteChannel.retrieveMessageById(messageId).queue(message ->
				{
					List<MessageEmbed.Field> fields = new ArrayList<>(message.getEmbeds().get(0).getFields());

					fields.removeIf(field -> (field.getName() == null) || !field.getName().startsWith("Option"));

					EmbedBuilder newEmbed = new EmbedBuilder(message.getEmbeds().get(0))
							.clearFields()
							.addField("Users", newUsers, false);

					for(MessageEmbed.Field field : fields)
					{
						newEmbed.addField(field);
					}
					message.editMessage(newEmbed.build()).queue();
				}
				, error -> {});
	}

	private static void clearDM(long userId, long messageId, IGSQBot igsqBot)
	{
		igsqBot.getShardManager()
				.retrieveUserById(userId)
				.flatMap(User::openPrivateChannel)
				.flatMap(channel -> channel.retrieveMessageById(messageId))
				.flatMap(Message::delete)
				.queue(null);
	}

	public static boolean castById(long userId, long messageId, int vote, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);

			var query = context.selectFrom(Tables.VOTES).where(VOTES.DIRECT_MESSAGE_ID.eq(messageId)).fetch();

			if(query.isEmpty() || query.get(0).getHasVoted())
			{
				return false;
			}

			var updateQuery = context.update(Tables.VOTES)
					.set(VOTES.HAS_VOTED, true)
					.set(VOTES.OPTION, vote)
					.where(VOTES.DIRECT_MESSAGE_ID.eq(messageId));

			updateQuery.execute();
			clearDM(userId, messageId, igsqBot);
			return true;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static int getMaxVoteById(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);

			var query = context.selectFrom(Tables.VOTES).where(VOTES.DIRECT_MESSAGE_ID.eq(messageId));
			var result = query.fetch();
			if(result.isNotEmpty())
			{
				return result.get(0).getMaxOptions();
			}
			return -1;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	public static boolean isVoteRunning(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.VOTES).where(VOTES.DIRECT_MESSAGE_ID.eq(messageId));
			var result = query.fetch();
			return result.isNotEmpty();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	private static String parseOption(int number)
	{
		return StringUtils.parseToEmote(number).isBlank() ? "Abstained" : " Voted for option " + StringUtils.parseToEmote(number);
	}
}
