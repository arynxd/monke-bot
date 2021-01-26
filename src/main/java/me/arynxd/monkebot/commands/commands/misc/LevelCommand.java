package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandSyntaxException;
import me.arynxd.monkebot.commands.subcommands.level.LevelAddCommand;
import me.arynxd.monkebot.commands.subcommands.level.LevelBotCommand;
import me.arynxd.monkebot.commands.subcommands.level.LevelRemoveCommand;
import me.arynxd.monkebot.commands.subcommands.level.LevelShowCommand;
import org.jetbrains.annotations.NotNull;

public class LevelCommand extends Command
{
	public LevelCommand()
	{
		super("Level", "Controls automatic role granting.", "[add / show / remove / bot]");
		addAliases("level");
		addFlags(CommandFlag.GUILD_ONLY);
		addChildren(
				new LevelAddCommand(this),
				new LevelBotCommand(this),
				new LevelShowCommand(this),
				new LevelRemoveCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}
