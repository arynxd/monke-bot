package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandSyntaxException;
import me.arynxd.monkebot.commands.subcommands.reactionrole.ReactionRoleAddCommand;
import me.arynxd.monkebot.commands.subcommands.reactionrole.ReactionRoleRemoveCommand;
import me.arynxd.monkebot.commands.subcommands.reactionrole.ReactionRoleShowCommand;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ReactionRoleCommand extends Command
{
	public ReactionRoleCommand()
	{
		super("Reaction Role", "Controls reaction roles.", "[add / remove / show]");
		addAliases("rr", "reactionrole");
		addFlags(CommandFlag.GUILD_ONLY);
		addChildren(
				new ReactionRoleAddCommand(this),
				new ReactionRoleRemoveCommand(this),
				new ReactionRoleShowCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}