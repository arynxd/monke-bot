package me.arynxd.monkebot.commands.subcommands.info;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.info.BotInfo;
import net.dv8tion.jda.api.EmbedBuilder;
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
				.addField("Server count", String.valueOf(BotInfo.getTotalServers(event.getMonke().getShardManager())), true)
				.addBlankField(true));
	}
}
