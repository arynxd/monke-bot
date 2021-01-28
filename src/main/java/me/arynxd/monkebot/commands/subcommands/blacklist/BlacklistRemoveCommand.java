package me.arynxd.monkebot.commands.subcommands.blacklist;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.util.ArrayUtils;
import me.arynxd.monkebot.util.BlacklistUtils;
import me.arynxd.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.Permission;
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
		if(BlacklistUtils.removePhrase(event.getGuild(), phrase, event.getMonke()))
		{
			event.replySuccess("Removed phrase ||" + phrase + "|| from the blacklist");
		}
		else
		{
			failure.accept(new CommandResultException("Phrase ||" + phrase + "|| not found in the blacklist."));
		}
	}
}
