package me.arynxd.monkebot.commands.maincommands.fun;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandSyntaxException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class UwUCommand extends Command
{
	public UwUCommand()
	{
		super("UwU", "UwU's the specified sentence.", "[text]");
		addAliases("uwu", "uwufy", "owo");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;
		if (CommandChecks.argsEmbedCompatible(event, failure)) return;

		List<String> chars = Arrays.stream(StringUtils.markdownSanitize(String.join(" ", args)).split("")).collect(Collectors.toList());
		StringBuilder builder = new StringBuilder();
		User author = event.getAuthor();

		if (chars.isEmpty())
		{
			failure.accept(new CommandSyntaxException(event));
			return;
		}

		for (String selectedChar : chars)
		{
			switch (selectedChar)
			{
				case "r", "l" -> builder.append("w");
				case "o" -> builder.append("wo");
				case "a" -> builder.append("aw");
				case "i" -> builder.append("iw");
				default -> builder.append(selectedChar);
			}
		}

		event.sendMessage(new EmbedBuilder()
				.setDescription(builder.toString())
				.setColor(Constants.EMBED_COLOUR)
				.setFooter("This sentence was UwU'd by: " + author.getAsTag() + " | "));

	}
}
