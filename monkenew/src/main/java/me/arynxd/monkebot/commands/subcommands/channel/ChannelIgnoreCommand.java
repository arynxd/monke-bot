package me.arynxd.monkebot.commands.subcommands.channel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.util.BlacklistUtils;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class ChannelIgnoreCommand extends Command
{
	public ChannelIgnoreCommand(Command parent)
	{
		super(parent, "ignore", "Controls ignored channels.", "[channel][true / false]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		new Parser(args.get(0), event).parseAsTextChannel(
				channel ->
				{
					Optional<Boolean> bool = new Parser(args.get(1), event).parseAsBoolean();

					if(bool.isPresent())
					{
						if(bool.get())
						{
							if(BlacklistUtils.addChannel(channel, event.getGuild(), event.getMonke()))
							{
								event.replySuccess("Blacklisted channel " + channel.getAsMention());
							}
							else
							{
								failure.accept(new CommandResultException("Channel " + channel.getAsMention() + " is already blacklisted."));
							}
						}
						else
						{
							if(BlacklistUtils.removeChannel(channel, event.getGuild(), event.getMonke()))
							{
								event.replySuccess("Removed blacklist for channel " + channel.getAsMention());
							}
							else
							{
								failure.accept(new CommandResultException("Channel " + channel.getAsMention() + " is not blacklisted."));
							}
						}
					}
				});
	}
}
