package me.arynxd.monkebot.commands.commands.developer;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestCommand extends Command
{
	public TestCommand()
	{
		super("Test", "Tests the bots basic functionality.", "[none]");
		addAliases("test");
		addFlags(CommandFlag.DEVELOPER_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		event.replySuccess("Success");
		event.replyError("Error");
		failure.accept(new CommandException("Exception"));
	}
}
