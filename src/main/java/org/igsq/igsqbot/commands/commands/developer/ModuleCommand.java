package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.igsq.igsqbot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ModuleCommand extends Command
{
	public ModuleCommand()
	{
		super("Module", "Disables / Enables the specified module.", "[enable/disable] [module]");
		addFlags(CommandFlag.DEVELOPER_ONLY);
		addAliases("module", "command");
		addChildren(
				new ModuleEnableCommand(this),
				new ModuleDisableCommand(this)
		);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}

	public static class ModuleEnableCommand extends Command
	{
		public ModuleEnableCommand(Command parent)
		{
			super(parent, "enable", "Enables a module", "[module-name]");
			addFlags(CommandFlag.DEVELOPER_ONLY);
		}

		@Override
		public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
		{
			if(CommandChecks.argsEmpty(event, failure))
return;
			String moduleName = args.get(0);
			Command command = event.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
			if(command == null)
			{
				failure.accept(new CommandResultException("Module " + moduleName + " was not found"));
				return;
			}

			if(!command.isDisabled())
			{
				failure.accept(new CommandResultException("Module " + command.getName() + " was already enabled."));
				return;
			}
			command.setDisabled(false);
			event.replySuccess("Enabled module: `" + command.getName() + "`.");
			event.getIGSQBot().getLogger().warn("Module " + command.getName() + " was enabled.");
		}
	}

	public static class ModuleDisableCommand extends Command
	{
		public ModuleDisableCommand(Command parent)
		{
			super(parent, "disable", "Disables a module", "[module-name]");
			addFlags(CommandFlag.DEVELOPER_ONLY);
		}

		@Override
		public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
		{
			if(CommandChecks.argsEmpty(event, failure))
return;
			String moduleName = args.get(0);
			Command command = event.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
			if(command == null)
			{
				failure.accept(new CommandResultException("Module " + moduleName + " was not found"));
				return;
			}
			if(command.isDisabled())
			{
				failure.accept(new CommandResultException("Module " + command.getName() + " was already disabled."));
				return;
			}

			command.setDisabled(true);
			event.replySuccess("Disabled module: `" + command.getName() + "`.");
			event.getIGSQBot().getLogger().warn("Module " + command.getName() + " was disabled.");
		}
	}
}
