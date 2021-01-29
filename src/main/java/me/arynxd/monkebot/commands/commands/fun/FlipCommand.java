package me.arynxd.monkebot.commands.commands.fun;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class FlipCommand extends Command
{
	public FlipCommand()
	{
		super("Flip", "Flips monke.", "[none]");
		addAliases("flip", "flip()");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		cmd.getChannel().sendMessage("https://tenor.com/view/monki-flip-monkey-monkey-flip-gif-18319480").queue();
	}
}
