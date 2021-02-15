package me.arynxd.monkebot.commands.maincommands.moderation;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.commands.subcommands.blacklist.BlacklistAddCommand;
import me.arynxd.monkebot.commands.subcommands.blacklist.BlacklistRemoveCommand;
import me.arynxd.monkebot.commands.subcommands.blacklist.BlacklistShowCommand;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandSyntaxException;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class BlacklistCommand extends Command
{
	public BlacklistCommand()
	{
		super("Blacklist", "Controls the blacklists.", "[add / remove / show]");
		addAliases("blacklist");
		addMemberPermissions(Permission.MANAGE_SERVER);
		addChildren(
				new BlacklistAddCommand(this),
				new BlacklistRemoveCommand(this),
				new BlacklistShowCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}