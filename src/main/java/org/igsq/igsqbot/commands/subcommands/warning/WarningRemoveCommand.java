package org.igsq.igsqbot.commands.subcommands.warning;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandHierarchyException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class WarningRemoveCommand extends Command
{
	public WarningRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a warning.", "[user][warningID]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		User author = event.getAuthor();
		Guild guild = event.getGuild();
		new Parser(args.get(0), event).parseAsUser(user ->
				{
					if(user.isBot())
					{
						failure.accept(new CommandInputException("Bots cannot have warnings."));
						return;
					}

					if(user.equals(author))
					{
						failure.accept(new CommandHierarchyException(this));
						return;
					}

					CommandUtils.interactionCheck(author, user, event, () ->
					{
						OptionalInt warningNumber = new Parser(args.get(1), event).parseAsUnsignedInt();
						if(warningNumber.isPresent())
						{
							Warnings warn = new Warning(event.getGuild(), user, event.getIGSQBot()).getByWarnId(warningNumber.getAsInt());

							if(warn == null)
							{
								failure.accept(new CommandInputException("Invalid warning specified."));
								return;
							}

							new Warning(guild, user, event.getIGSQBot()).remove(warningNumber.getAsInt());
							event.replySuccess("Removed warning: " + warn.getWarnText());
						}
					});
				}
		);
	}
}
