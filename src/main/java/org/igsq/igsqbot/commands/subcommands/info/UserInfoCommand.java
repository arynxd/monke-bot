package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.entities.info.MemberInfo;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;
import org.jetbrains.annotations.NotNull;

public class UserInfoCommand extends Command
{
	public UserInfoCommand(Command parent)
	{
		super(parent, "user", "Shows information about a user.", "[user]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(args.isEmpty())
		{
			showInfo(event.getMember(), event);
		}
		else
		{
			Guild guild = event.getGuild();
			String arg = ArrayUtils.arrayCompile(args.subList(0, args.size()), " ");
			new Parser(arg, event).parseAsUser(
					user -> UserUtils.getMemberFromUser(user, guild).queue(
							member -> showInfo(member, event),
							error -> failure.accept(new CommandResultException("Member " + arg + " not found, may not be in a shared server with me."))));
		}
	}

	private void showInfo(Member member, CommandEvent ctx)
	{
		MemberInfo memberInfo = new MemberInfo(member);

		ctx.sendMessage(new EmbedBuilder()
				.setTitle("Information for user **" + memberInfo.getAsTag() + "**")
				.addField("Boosting since", memberInfo.getBoostingSince() == null ? "Not boosting" : StringUtils.parseDateTime(memberInfo.getBoostingSince()), true)
				.addField("Joined at", StringUtils.parseDateTime(memberInfo.getTimeJoined()), true)
				.addField("Created account at", StringUtils.parseDateTime(memberInfo.getTimeCreated()), true)
				.addField("Roles", memberInfo.getCondensedRoles().stream().map(Role::getAsMention).collect(Collectors.joining(" ")), true)
				.setThumbnail(memberInfo.getAvatarURL()));
	}
}
