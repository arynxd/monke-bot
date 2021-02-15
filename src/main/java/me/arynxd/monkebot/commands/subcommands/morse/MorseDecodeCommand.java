package me.arynxd.monkebot.commands.subcommands.morse;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.commands.maincommands.misc.MorseCommand;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MorseDecodeCommand extends Command
{
	public MorseDecodeCommand(Command parent)
	{
		super(parent, "decode", "Decodes morse into text.", "[morse]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;
		String[] text = String.join(" ", args).split(" ");
		StringBuilder builder = new StringBuilder();
		boolean isValid = true;

		for (String character : text)
		{
			int index = MorseCommand.MORSE.indexOf(character);

			if (index == -1)
			{
				isValid = false;
				continue;
			}
			builder.append(MorseCommand.ENGLISH.get(index));
		}

		if (builder.length() == 0)
		{
			event.replyError("Some of your characters could not be decoded, there may be issues with the result!");
			return;
		}

		StringUtils.sendPartialMessages(builder.toString(), event.getChannel());
		if (!isValid)
		{
			event.replyError("Some of your characters could not be decoded, there may be issues with the result!");
		}
	}
}