package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import me.arynxd.monkebot.util.Parser;
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
			Command command = event.getMonke().getCommandHandler().getCommandMap().get(args.get(0));
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
			if(page.getAsInt() + 1 > event.getMonke().getHelpPages().size() + 1)
			{
				failure.accept(new CommandInputException("Page `" + args.get(0) + "` does not exist."));
				return;
			}

			event.sendMessage(event.getMonke().getHelpPages().get(page.getAsInt() - 1));
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
