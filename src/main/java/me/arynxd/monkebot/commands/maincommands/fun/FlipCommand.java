package me.arynxd.monkebot.commands.maincommands.fun;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class FlipCommand extends Command
{
	public FlipCommand()
	{
		super("Flip", "Flips monke.", "[none]");
		addAliases("flip", "flip()");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent cmd, @NotNull Consumer<CommandException> failure)
	{
		cmd.getChannel().sendMessage("https://tenor.com/view/monki-flip-monkey-monkey-flip-gif-18319480").queue();
	}
}
