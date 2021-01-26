package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.commands.subcommands.link.LinkAddCommand;
import org.igsq.igsqbot.commands.subcommands.link.LinkRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.link.LinkShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class LinkCommand extends Command
{
	public LinkCommand()
	{
		super("Link", "Controls Minecraft links.", "[add/remove][mcName] / [show][user]");
		addAliases("link", "mclink");
		addChildren(
				new LinkAddCommand(this),
				new LinkRemoveCommand(this),
				new LinkShowCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		this.getChildren().get(2).run(args, event, failure);
	}
}
