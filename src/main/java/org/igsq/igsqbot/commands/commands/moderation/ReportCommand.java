package org.igsq.igsqbot.commands.commands.moderation;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Report;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandHierarchyException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.util.*;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ReportCommand extends Command
{
	public ReportCommand()
	{
		super("Report", "Reports a member for a reason", "[user] [reason]");
		addAliases("report");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		MessageChannel channel = event.getChannel();
		User author = event.getAuthor();
		Guild guild = event.getGuild();
		MessageChannel reportChannel = guild.getTextChannelById(new GuildConfig(event).getReportChannel());

		if(CommandChecks.channelConfigured(reportChannel, "Report channel", failure)) return;
		if(CommandChecks.canSee(reportChannel, event.getSelfMember(), "Report channel", failure)) return;

		new Parser(args.get(0), event).parseAsUser(user ->
		{
			args.remove(0);
			String reason = ArrayUtils.arrayCompile(args, " ");
			String messageLink = StringUtils.getMessageLink(event.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong());

			if(user.isBot())
			{
				failure.accept(new CommandInputException("You may not report bots."));
				return;
			}

			UserUtils.getMemberFromUser(user, guild).queue(
					member ->
					{
						if(member.isOwner())
						{
							failure.accept(new CommandHierarchyException(this));
							return;
						}

						reportChannel.sendMessage(new EmbedBuilder()
								.setTitle("New report by: " + author.getAsTag())
								.addField("Reporting user:", user.getAsMention(), false)
								.addField("Description:", reason, false)
								.addField("Channel:", StringUtils.getChannelAsMention(channel.getId()), false)
								.addField("Message Link:", "[Jump to message](" + messageLink + ")", false)
								.setColor(member.getColor())
								.setFooter("This report is unhandled and can only be dealt by members higher than " + member.getRoles().get(0).getName())
								.build()).queue
								(
										message ->
										{
											author.openPrivateChannel()
													.flatMap(privateChannel ->
															privateChannel.sendMessage(generateDM(member, user, reason).build()))
													.queue(null, error ->
													{});

											Report.add(message.getIdLong(), event.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong(), user.getIdLong(), author.getIdLong(), reason, event.getIGSQBot());
											message.addReaction(Emoji.THUMB_UP.getUnicode()).queue();
										}
								);
					}
			);
		});
	}

	private EmbedBuilder generateDM(Member author, User reported, String reason)
	{
		return new EmbedBuilder()
				.setTitle("Your report in " + author.getGuild().getName())
				.addField("Reporting User", reported.getAsMention(), true)
				.addField("Reason", reason, true)
				.setColor(Constants.IGSQ_PURPLE)
				.setTimestamp(Instant.now());
	}
}
