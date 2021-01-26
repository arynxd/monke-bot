package org.igsq.igsqbot.commands.commands.misc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class UptimeCommand extends Command
{
	public UptimeCommand()
	{
		super("Uptime", "Displays the bots uptime.", "[none]");
		addAliases("uptime");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Duration uptime = Duration.between(event.getIGSQBot().getStartTimestamp(), LocalDateTime.now());
		event.sendMessage(new EmbedBuilder()
				.setDescription(
						"Uptime: " + uptime.toDaysPart() +
								" days, " + uptime.toHoursPart() +
								" hours, " + uptime.toSecondsPart() +
								" seconds.")
				.setColor(Constants.IGSQ_PURPLE));
	}
}
