package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

public class BlacklistRemoveCommand extends Command
{
	public BlacklistRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a phrase from the blacklist.", "[phrase]");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE, CommandFlag.BLACKLIST_BYPASS);
		addMemberPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;
		String phrase = ArrayUtils.arrayCompile(args, " ");
		if(BlacklistUtils.removePhrase(event.getGuild(), phrase, event.getIGSQBot()))
		{
			event.replySuccess("Removed phrase ||" + phrase + "|| from the blacklist");
		}
		else
		{
			failure.accept(new CommandResultException("Phrase ||" + phrase + "|| not found in the blacklist."));
		}
	}
}
