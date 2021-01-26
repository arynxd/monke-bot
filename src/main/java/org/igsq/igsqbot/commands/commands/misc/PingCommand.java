package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PingCommand extends Command
{
	public PingCommand()
	{
		super("Ping", "Shows the bot's ping to Discord.", "[none]");
		addAliases("ping");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		JDA jda = event.getJDA();
		jda.getRestPing().queue(
				ping ->
				{
					int oCount = (int) (ping / 100);

					if(oCount > 256)
					{
						oCount = 256;
					}

					event.sendMessage(new EmbedBuilder()
							.setTitle("P" + "o".repeat(oCount) + "ng.")
							.setDescription("**Shard ID**: " + jda.getShardInfo().getShardId()
									+ "\n**REST Ping**: " + ping
									+ "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms")
							.setColor(Constants.IGSQ_PURPLE));
				});
	}
}
