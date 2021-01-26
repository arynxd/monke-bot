package org.igsq.igsqbot.commands.commands.fun;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
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
		if(CommandChecks.argsEmpty(event, failure)) return;
		if(CommandChecks.argsEmbedCompatible(event, failure)) return;

		List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		StringBuilder finalSentence = new StringBuilder();
		User author = event.getAuthor();

		for(String selectedChar : chars)
		{
			switch(selectedChar)
			{
				case "r", "l" -> finalSentence.append("w");
				case "o" -> finalSentence.append("wo");
				case "a" -> finalSentence.append("aw");
				case "i" -> finalSentence.append("iw");
				default -> finalSentence.append(selectedChar);
			}
		}

		event.sendMessage(new EmbedBuilder()
				.setDescription(finalSentence.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.setFooter("This sentence was UwU'd by: " + author.getAsTag() + " | "));

	}
}
