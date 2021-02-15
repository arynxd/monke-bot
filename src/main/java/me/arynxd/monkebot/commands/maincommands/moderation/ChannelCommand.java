package me.arynxd.monkebot.commands.maincommands.moderation;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.commands.subcommands.channel.ChannelIgnoreCommand;
import me.arynxd.monkebot.commands.subcommands.channel.ChannelShowCommand;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandSyntaxException;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
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
