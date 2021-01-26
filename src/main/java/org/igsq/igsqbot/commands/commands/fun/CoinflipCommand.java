package org.igsq.igsqbot.commands.commands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CoinflipCommand extends Command
{
	public CoinflipCommand()
	{
		super("Coinflip", "Flips a coin.", "[none]");
		addAliases("coinflip");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Random random = new Random();
		String result;
		int chance = random.nextInt(2);

		if(chance == 1)
		{
			result = "Heads";
		}
		else
		{
			result = "Tails";
		}

		event.getChannel().sendMessage(result).queue();
	}
}

