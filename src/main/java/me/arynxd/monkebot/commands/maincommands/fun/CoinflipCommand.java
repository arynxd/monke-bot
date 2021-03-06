package me.arynxd.monkebot.commands.maincommands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class CoinflipCommand extends Command
{
	public CoinflipCommand()
	{
		super("Coinflip", "Flips a coin.", "[none]");
		addAliases("coinflip", "coin");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Random random = new Random();
		String result;
		int chance = random.nextInt(2);

		if (chance == 1)
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

