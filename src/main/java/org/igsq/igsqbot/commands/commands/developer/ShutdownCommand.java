package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ShutdownCommand extends Command
{
	public ShutdownCommand()
	{
		super("Shutdown", "Shuts the bot down gracefully.", "[none]");
		addFlags(CommandFlag.DEVELOPER_ONLY);
		addAliases("shutdown", "die");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		event.getIGSQBot().getDatabaseHandler().close();
		event.getIGSQBot().getMinecraft().close();
		event.getIGSQBot().getTaskHandler().close();

		event.getJDA().shutdown();

		event.getIGSQBot().getLogger().warn("-- IGSQBot was shutdown using shutdown command.");
		event.getIGSQBot().getLogger().warn("-- Issued by: " + event.getAuthor().getAsTag());
		if(event.getGuild() != null)
		{
			event.getIGSQBot().getLogger().warn("-- In guild: " + event.getGuild().getName());
		}
		else
		{
			event.getIGSQBot().getLogger().warn("-- In guild: " + "Shutdown in DMs.");
		}
		System.exit(0);
	}
}
