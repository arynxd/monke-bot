package me.arynxd.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.commands.subcommands.morse.MorseDecodeCommand;
import me.arynxd.monkebot.commands.subcommands.morse.MorseEncodeCommand;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MorseCommand extends Command
{
	public static final List<String> ENGLISH = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
			"y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
			",", ".", "?", " ");

	public static final List<String> MORSE = List.of(".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
			".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
			"...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
			"..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
			"-----", "--..--", ".-.-.-", "..--..", "/");

	public MorseCommand()
	{
		super("Morse", "Encodes and decodes morse code.", "[encode / decode]");
		addAliases("morse");
		addChildren(
				new MorseEncodeCommand(this),
				new MorseDecodeCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		failure.accept(new CommandSyntaxException(event));
	}
}
