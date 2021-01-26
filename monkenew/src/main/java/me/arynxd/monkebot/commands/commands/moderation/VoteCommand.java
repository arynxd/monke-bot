package me.arynxd.monkebot.commands.commands.moderation;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.commands.subcommands.vote.VoteCloseCommand;
import me.arynxd.monkebot.commands.subcommands.vote.VoteCreateCommand;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandSyntaxException;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VoteCommand extends Command
{
	public VoteCommand()
	{
		super("Vote", "Controls voting", "[create / close]");
		addAliases("vote");
		addMemberPermissions(Permission.MANAGE_SERVER);
		addChildren(
				new VoteCreateCommand(this),
				new VoteCloseCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}
