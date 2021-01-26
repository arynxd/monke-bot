package org.igsq.igsqbot.commands.subcommands.verification;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.VerificationUtils;
import org.jetbrains.annotations.NotNull;

public class VerificationAddCommand extends Command
{
	public VerificationAddCommand(Command parent)
	{
		super(parent, "add", "Adds a new phrase/role pair.", "[role][phrase{2}]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeExceeds(event, 3, failure)) return;
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		new Parser(args.get(0), event).parseAsRole(
				role ->
				{
					String phrase = ArrayUtils.arrayCompile(args.subList(1, args.size()), " ");

					if(VerificationUtils.addMapping(phrase, event.getGuildIdLong(), role.getIdLong(), event.getIGSQBot()))
					{
						event.replySuccess("Added mapping " + phrase + " for role " + role.getAsMention());
					}
					else
					{
						event.replyError("Mapping " + phrase + " for role " + role.getAsMention() + " already exists.");
					}
				});
	}
}
