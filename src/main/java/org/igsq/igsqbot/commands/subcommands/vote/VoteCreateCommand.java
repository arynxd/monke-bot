package org.igsq.igsqbot.commands.subcommands.vote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class VoteCreateCommand extends Command
{
	public VoteCreateCommand(Command parent)
	{
		super(parent, "create", "Creates a vote.", "[role1/role2{3}][option1/option2{6}][duration][subject]");
		addMemberPermissions(Permission.MANAGE_CHANNEL);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 3, failure)) return;

		List<String> options = new Parser(args.get(1), event).parseAsSlashArgs();
		LocalDateTime expiry = new Parser(args.get(2), event).parseAsDuration();
		String subject = ArrayUtils.arrayCompile(args.subList(3, args.size()), " ");
		Guild guild = event.getGuild();
		List<Role> roles = new ArrayList<>();
		List<Long> users = new ArrayList<>();

		int argCount = new Parser(args.get(0), event).parseAsSlashArgs().size();
		int roleCount = event.getMessage().getMentionedRoles().size();
		int memberCount = event.getMessage().getMentionedMembers().size();

		if(roleCount == argCount)
		{
			roles = event.getMessage().getMentionedRoles();
		}
		else if(memberCount == argCount)
		{
			users = event.getMessage().getMentionedUsers().stream().map(User::getIdLong).collect(Collectors.toList());
		}
		else
		{
			roles = new Parser(args.get(0), event)
					.parseAsSlashArgs()
					.stream()
					.map(arg -> arg.replaceAll("[^a-z]/gi", ""))
					.map(guild::getRoleById)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());


			users = new Parser(args.get(0), event)
					.parseAsSlashArgs()
					.stream()
					.map(arg -> arg.replaceAll("[^a-z]/gi", ""))
					.map(guild::getMemberById)
					.filter(Objects::nonNull)
					.filter(member -> !member.getUser().isBot())
					.map(Member::getIdLong)
					.collect(Collectors.toList());
		}


		if(options.isEmpty() || options.size() > 6 || expiry == null || roles.size() > 3 || users.size() > 10)
		{
			failure.accept(new CommandSyntaxException(event));
			return;
		}

		if(users.isEmpty() && !roles.isEmpty())
		{
			List<Role> finalRoles = roles;
			guild.findMembers(member -> member.getRoles().stream().anyMatch(finalRoles::contains)).onSuccess(
					members ->
					{
						members = members.stream().filter(member -> !member.getUser().isBot()).collect(Collectors.toList());
						if(members.isEmpty())
						{
							failure.accept(new CommandInputException("No members found for roles " + finalRoles
									.stream()
									.map(Role::getAsMention)
									.collect(Collectors.joining(" "))));
							return;
						}

						if(members.size() > 20)
						{
							failure.accept(new CommandInputException("Too many members found for roles " + finalRoles
									.stream()
									.map(Role::getAsMention)
									.collect(Collectors.joining(" "))));
							return;
						}


						Vote vote = new Vote(members.stream().map(Member::getIdLong).collect(Collectors.toList()), options, expiry, subject, event);
						vote.start();
					});
		}
		else if(roles.isEmpty() && !users.isEmpty())
		{
			Vote vote = new Vote(users, options, expiry, subject, event);
			vote.start();
		}
		else
		{
			failure.accept(new CommandSyntaxException(event));
		}
	}
}