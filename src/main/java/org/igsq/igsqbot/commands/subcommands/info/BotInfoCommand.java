package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.info.BotInfo;
import org.jetbrains.annotations.NotNull;

public class BotInfoCommand extends Command
{
	public BotInfoCommand(Command parent)
	{
		super(parent, "bot", "Shows information about the bot.", "[none]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		event.sendMessage(new EmbedBuilder()
				.setTitle(event.getJDA().getSelfUser().getAsTag() + " information")
				.addField("JVM Version", BotInfo.getJavaVersion(), true)
				.addField("JDA Version", BotInfo.getJDAVersion(), true)
				.addBlankField(true)
				.addField("Thread Count", String.valueOf(BotInfo.getThreadCount()), true)
				.addField("Memory Usage", BotInfo.getMemoryFormatted(), true)
				.addBlankField(true)
				.addField("Shard info", event.getJDA().getShardInfo().getShardString(), true)
				.addField("Server count", String.valueOf(BotInfo.getTotalServers(event.getIGSQBot().getShardManager())), true)
				.addBlankField(true));
	}
}
