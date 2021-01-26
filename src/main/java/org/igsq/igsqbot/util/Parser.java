package org.igsq.igsqbot.util;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.entities.command.CommandEvent;

public class Parser
{
	public static final Pattern ID_REGEX = Pattern.compile("(\\d{17,18})");
	public static final Pattern periodPattern = Pattern.compile(
			"([0-9]+) ?-?\\.?,?(" +
					"mo|mnth|month|months" +
					"|w|wk|wks|weeks" +
					"|h|hrs|hours" +
					"|d|day|days" +
					"|m|min|mins|minutes" +
					"|s|sec|secs|seconds" +
					")");

	private final String arg;
	private final CommandEvent event;

	public Parser(String arg, CommandEvent event)
	{
		this.arg = arg;
		this.event = event;
	}

	public List<String> parseAsSlashArgs()
	{
		return Arrays.stream(arg.split("/")).collect(Collectors.toList());
	}

	public OptionalLong parseAsUnsignedLong()
	{
		try
		{
			return OptionalLong.of(Long.parseUnsignedLong(arg));
		}
		catch(Exception exception)
		{
			event.replyError("Invalid ID entered.");
			return OptionalLong.empty();
		}
	}

	public Optional<Boolean> parseAsBoolean()
	{
		try
		{
			return Optional.of(Boolean.parseBoolean(arg));
		}
		catch(Exception exception)
		{
			event.replyError("Invalid true / false entered.");
			return Optional.empty();
		}
	}

	public Optional<Guild> parseAsGuild()
	{
		if(arg.equalsIgnoreCase("this") || arg.equalsIgnoreCase("here"))
		{
			return Optional.of(event.getGuild());
		}
		return event.getIGSQBot().getShardManager().getGuilds().stream().filter(guild -> guild.getId().equals(arg)).findFirst();
	}

	public LocalDateTime parseAsDuration()
	{
		Matcher matcher = periodPattern.matcher(arg.toLowerCase());
		LocalDateTime offset = LocalDateTime.now();
		while(matcher.find())
		{
			try
			{
				int num = Integer.parseInt(matcher.group(1));
				if(num == 0)
				{
					continue;
				}

				String typ = matcher.group(2);
				switch(typ)
				{
					case "s", "sec", "secs", "seconds" -> offset = offset.plusSeconds(num);
					case "m", "min", "mins", "minutes" -> offset = offset.plusMinutes(num);
					case "h", "hrs", "hours" -> offset = offset.plusHours(num);
					case "d", "day", "days" -> offset = offset.plusDays(num);
					case "w", "wk", "wks", "weeks" -> offset = offset.plusWeeks(num);
					case "mo", "mnth", "month", "months" -> offset = offset.plusMonths(num);
				}
			}
			catch(Exception ignored) { }

		}
		if(offset.isBefore(LocalDateTime.now()))
		{
			return null;
		}
		return offset;
	}

	public OptionalInt parseAsUnsignedInt()
	{
		try
		{
			OptionalInt value = OptionalInt.of(Integer.parseUnsignedInt(arg));
			if(value.getAsInt() == 0)
			{
				event.replyError("Enter a whole number greater than 0, eg: 1");
			}
			return value;
		}
		catch(NumberFormatException exception)
		{
			event.replyError("Enter a whole number greater than 0, eg: 1");
			return OptionalInt.empty();
		}
	}

	public void parseAsTextChannel(Consumer<TextChannel> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept((TextChannel) mentionable), Message.MentionType.CHANNEL);
	}

	public void parseAsRole(Consumer<Role> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept(((Role) mentionable)), Message.MentionType.ROLE);
	}

	public void parseAsUser(Consumer<User> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept((User) mentionable), Message.MentionType.USER);
	}

	private void parseAsMentionable(Consumer<IMentionable> consumer, Message.MentionType type)
	{
		Message message = event.getMessage();
		Guild guild = event.getGuild();
		User author = event.getAuthor();
		String typeName = type.name().toLowerCase();
		Matcher idMatcher = ID_REGEX.matcher(arg);
		JDA jda = event.getJDA();
		SelfUser selfUser = jda.getSelfUser();

		if(type.getPattern().matcher(arg).matches()) //Direct mention
		{
			IMentionable mention = message.getMentions(type).get(0);
			consumer.accept(mention);
			return;
		}

		if(idMatcher.matches()) //ID
		{
			long mentionableId = Long.parseLong(idMatcher.group());
			if(type == Message.MentionType.USER)
			{
				if(mentionableId == author.getIdLong())
				{
					consumer.accept(author);
				}
				else if(mentionableId == selfUser.getIdLong())
				{
					consumer.accept(selfUser);
				}
				else
				{
					jda.retrieveUserById(mentionableId).queue(consumer, failure -> event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found."));
					return;
				}
			}
			else if(type == Message.MentionType.CHANNEL)
			{
				MessageChannel channel = jda.getTextChannelById(mentionableId);
				if(channel != null)
				{
					consumer.accept((IMentionable) channel);
				}
				else
				{
					event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found or i dont have permission to see it.");
				}
				return;
			}
			else if(type == Message.MentionType.ROLE)
			{
				Role role = jda.getRoleById(mentionableId);
				if(role != null)
				{
					consumer.accept(role);
				}
				else
				{
					event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found.");
				}
				return;
			}
		}

		if(arg.length() >= 2 && arg.length() <= 32) //Named users
		{
			if(type == Message.MentionType.USER)
			{
				if(arg.equalsIgnoreCase(event.getMember().getEffectiveName()))
				{
					consumer.accept(author);
					return;
				}
				message.getGuild().retrieveMembersByPrefix(arg, 10)
						.onSuccess(members ->
						{
							if(members.isEmpty())
							{
								event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found.");
								return;
							}

							consumer.accept(members.get(0).getUser());
						});
				return;
			}
			var rolesChannelsList = type == Message.MentionType.CHANNEL ? guild.getTextChannelsByName(arg, true) : guild.getRolesByName(arg, true);
			if(rolesChannelsList.isEmpty()) //Role / Channel
			{
				event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found.");
				return;
			}
			else
			{
				consumer.accept(rolesChannelsList.get(0));
				return;
			}
		}

		event.replyError("No " + typeName.toLowerCase() + "s with name " + arg + " found.");
	}
}
