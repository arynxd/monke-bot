package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class HelpCommand extends Command
{
	public HelpCommand()
	{
		super("Help", "Shows the help menu for this bot.", "[page / command]");
		addAliases("help", "?", "howto", "commands");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		OptionalInt page;
		if(args.isEmpty())
		{
			page = OptionalInt.of(1);
		}
		else
		{
			Command command = event.getIGSQBot().getCommandHandler().getCommandMap().get(args.get(0));
			if(command == null)
			{
				page = new Parser(args.get(0), event).parseAsUnsignedInt();
			}
			else
			{
				event.sendMessage(generateHelpPerCommand(command, event.getPrefix()));
				return;
			}
		}


		if(page.isPresent())
		{
			if(page.getAsInt() + 1 > event.getIGSQBot().getHelpPages().size() + 1)
			{
				failure.accept(new CommandInputException("Page `" + args.get(0) + "` does not exist."));
				return;
			}

			event.sendMessage(event.getIGSQBot().getHelpPages().get(page.getAsInt() - 1));
		}
	}

	private EmbedBuilder generateHelpPerCommand(Command command, String prefix)
	{
		EmbedBuilder result = new EmbedBuilder()
				.setTitle("**Help for " + command.getName() + "**")
				.setFooter("<> Optional;  [] Required; {} Maximum Quantity | ");
		result.addField(prefix + command.getAliases().get(0), command.getDescription() + "\n`Syntax: " + command.getSyntax() + "`", false);
		if(command.hasChildren())
		{
			command.getChildren().forEach(
					child ->
							result.addField(prefix + command.getAliases().get(0) + " " + child.getName(), child.getDescription() + "\n`Syntax: " + child.getSyntax() + "`", false));
		}
		return result;
	}
}
