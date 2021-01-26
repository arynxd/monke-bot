package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.commands.subcommands.channel.ChannelIgnoreCommand;
import org.igsq.igsqbot.commands.subcommands.channel.ChannelShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ChannelCommand extends Command
{
	public ChannelCommand()
	{
		super("Channel", "Controls channel setup.", "[ignore / show]");
		addAliases("channel");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MANAGE_SERVER);
		addChildren(
				new ChannelIgnoreCommand(this),
				new ChannelShowCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}
