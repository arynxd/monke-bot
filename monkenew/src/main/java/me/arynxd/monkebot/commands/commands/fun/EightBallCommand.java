package me.arynxd.monkebot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EightBallCommand extends Command
{
	public EightBallCommand()
	{
		super("8Ball", "Query the 8Ball.", "[question]");
		addAliases("8ball");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Random random = new Random();
		List<String> options = new ArrayList<>(List.of("op1", "op2"));

		int choice = random.nextInt(args.size() + 1);
		event.getChannel().sendMessage(options.get(Math.abs(choice - 1))).queue();
	}
}

