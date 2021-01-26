package org.igsq.igsqbot.commands.commands.misc;

import info.debatty.java.stringsimilarity.JaroWinkler;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.commands.subcommands.verification.VerificationAddCommand;
import org.igsq.igsqbot.commands.subcommands.verification.VerificationRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.verification.VerificationShowCommand;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;
import org.igsq.igsqbot.util.VerificationUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VerificationCommand extends Command
{
	public VerificationCommand()
	{
		super("Verification", "Handles verification of new members.", "[user] / [add / remove / show]");
		addAliases("verify", "verification", "accept", "v");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MANAGE_ROLES);
		addChildren(
				new VerificationAddCommand(this),
				new VerificationShowCommand(this),
				new VerificationRemoveCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent commandEvent, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(commandEvent, failure)) return;

		MessageChannel channel = commandEvent.getChannel();
		Guild guild = commandEvent.getGuild();
		Role verifiedRole = guild.getRoleById(new GuildConfig(commandEvent).getVerifiedRole());
		Role unverifiedRole = guild.getRoleById(new GuildConfig(commandEvent).getUnverifiedRole());

		if(CommandChecks.roleConfigured(verifiedRole, "Verified role", failure)) return;

		new Parser(args.get(0), commandEvent).parseAsUser(
				target ->
					UserUtils.getMemberFromUser(target, guild).queue(
							member ->
							{
								if(target.isBot())
								{
									failure.accept(new CommandResultException("Bots cannot be verified."));
									return;
								}
								if(member.getRoles().contains(verifiedRole))
								{
									failure.accept(new CommandResultException("User " + target.getAsMention() + " is already verified."));
									return;
								}
								channel.getIterableHistory().takeAsync(10).thenAccept(
										input ->
										{
											List<Message> messages = filterMessages(input, target);

											if(messages.isEmpty())
											{
												failure.accept(new CommandResultException("No messages from " + target.getAsMention() + " were found in this channel."));
												return;
											}

											StringBuilder ctxMessage = new StringBuilder();
											StringBuilder welcomeMessage = new StringBuilder();

											List<Role> roles = new ArrayList<>();

											for(long role : getMatches(messages, commandEvent.getGuild(), commandEvent.getIGSQBot()))
											{
												Role fetch = guild.getRoleById(role);
												if(fetch != null)
												{
													roles.add(fetch);
												}
											}

											if(!roles.isEmpty())
											{
												for(Role role : roles)
												{
													ctxMessage.append(role.getAsMention()).append("\n");
													welcomeMessage.append(role.getAsMention());
												}
											}
											roles.addAll(member.getRoles());

											commandEvent.getChannel().sendMessage(new EmbedBuilder()
													.setTitle("Verification for " + target.getAsTag())
													.addField("Roles", ctxMessage.length() == 0 ? "No roles found" : ctxMessage.toString(), false)
													.setColor(Constants.IGSQ_PURPLE)
													.setTimestamp(Instant.now())
													.build()).queue(message ->
											{
												message.addReaction(Emoji.THUMB_UP.getAsReaction()).queue();
												commandEvent.getIGSQBot().getEventWaiter().waitForEvent(GuildMessageReactionAddEvent.class,
														event -> event.getMessageIdLong() == message.getIdLong() && event.getUserIdLong() == commandEvent.getAuthor().getIdLong(),
														event ->
														{
															message.delete().queue(null, error -> {});
															channel.purgeMessages(messages);

															MessageChannel welcomeChannel = guild.getTextChannelById(new GuildConfig(commandEvent).getWelcomeChannel());
															if(welcomeChannel != null)
															{
																welcomeChannel.sendMessage(new EmbedBuilder()
																		.setAuthor(target.getAsTag(), null, target.getEffectiveAvatarUrl())
																		.setDescription(target.getAsMention() + " has joined " + guild.getName() + ". Welcome!")
																		.addField("Roles", welcomeMessage.length() == 0 ? "No roles." : welcomeMessage.toString(), false)
																		.setColor(Constants.IGSQ_PURPLE)
																		.setTimestamp(Instant.now())
																		.build()).queue();
															}
															else
															{
																commandEvent.replyError("Welcome channel not setup, no welcome message will be sent.");
															}

															roles.add(verifiedRole);
															if(unverifiedRole != null)
															{
																roles.remove(unverifiedRole);
															}

															guild.modifyMemberRoles(member, roles).queue();

														},
														10000, TimeUnit.MILLISECONDS,
														() -> message.delete().queue(null, error -> {}));
											});
										});
							}));
	}

	private List<Message> filterMessages(List<Message> messages, User target)
	{
		return messages.stream().filter(message -> message.getAuthor().equals(target)).collect(Collectors.toList());
	}

	private List<Long> getMatches(List<Message> messages, Guild guild, IGSQBot igsqBot)
	{
		List<Long> result = new ArrayList<>();
		List<String> content = messages.stream().map(Message::getContentRaw).collect(Collectors.toList());

		Map<String, Long> mappings = VerificationUtils.getMappedPhrases(guild, igsqBot);
		JaroWinkler matcher = new JaroWinkler();

		if(mappings.isEmpty())
		{
			return result;
		}

		for(Map.Entry<String, Long> entry : mappings.entrySet())
		{
			String phrase = entry.getKey();
			long role = entry.getValue();

			for(String message : content)
			{
				List<String> words = new ArrayList<>(List.of(message.split(" ")));

				for(int i = 0; i < words.size(); i++)
				{
					String query;
					int queryNum = i;

					if(phrase.contains(" "))
					{
						if(++queryNum > words.size())
						{
							query = words.get(i);
						}
						else
						{
							query = words.get(i) + words.get(i + 1);
						}
					}
					else
					{
						query = words.get(i);
					}


					if(matcher.similarity(phrase, query) > 0.8)
					{
						if(!result.contains(role))
						{
							result.add(role);
						}
					}
				}
			}
		}
		return result;
	}

}
