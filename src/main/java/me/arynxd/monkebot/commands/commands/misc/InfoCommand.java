package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.commands.subcommands.info.BotInfoCommand;
import me.arynxd.monkebot.commands.subcommands.info.RoleInfoCommand;
import me.arynxd.monkebot.commands.subcommands.info.ServerInfoCommand;
import me.arynxd.monkebot.commands.subcommands.info.UserInfoCommand;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class InfoCommand extends Command
{
	public InfoCommand()
	{
		super("Info", "Provides information about things.", "[user/server/bot/role]");
		addAliases("info");
		addFlags(CommandFlag.GUILD_ONLY);
		addChildren(
				new UserInfoCommand(this),
				new BotInfoCommand(this),
				new RoleInfoCommand(this),
				new ServerInfoCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		this.getChildren().get(0).run(args, event, failure);
	}
}
