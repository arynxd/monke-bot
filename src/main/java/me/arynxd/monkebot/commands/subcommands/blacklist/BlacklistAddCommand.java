package me.arynxd.monkebot.commands.subcommands.blacklist;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.util.BlacklistUtils;
import me.arynxd.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class BlacklistAddCommand extends Command
{
	public BlacklistAddCommand(Command parent)
	{
		super(parent, "add", "Adds a phrase to the blacklist.", "[phrase]");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
		addMemberPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;
		String phrase = String.join(" ", args);

		if(event.getMonke().getCommandHandler().getCommandMap().get(phrase) != null)
		{
			failure.accept(new CommandInputException("You cannot blacklist my commands."));
			return;
		}

		BlacklistUtils.addPhrase(event.getGuild(), phrase, event.getMonke());
		event.replySuccess("Added phrase ||" + phrase + "|| to the blacklist");
	}
}
