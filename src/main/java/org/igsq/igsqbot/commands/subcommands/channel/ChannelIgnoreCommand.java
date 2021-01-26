package org.igsq.igsqbot.commands.subcommands.channel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
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
							if(BlacklistUtils.addChannel(channel, event.getGuild(), event.getIGSQBot()))
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
							if(BlacklistUtils.removeChannel(channel, event.getGuild(), event.getIGSQBot()))
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
