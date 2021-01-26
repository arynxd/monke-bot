package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.commands.subcommands.link.LinkAddCommand;
import me.arynxd.monkebot.commands.subcommands.link.LinkRemoveCommand;
import me.arynxd.monkebot.commands.subcommands.link.LinkShowCommand;
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
