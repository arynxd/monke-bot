package org.igsq.igsqbot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PollCommand extends Command
{
	public PollCommand()
	{
		super("Poll", "Starts a poll for users to vote in.", "[title]/[option1]/[option2]{20}");
		addAliases("poll");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeMatches(event, 1, failure)) return;

		StringBuilder options = new StringBuilder();
		MessageChannel channel = event.getChannel();
		User author = event.getAuthor();
		List<String> reactions = new ArrayList<>();
		List<String> slashArgs = new Parser(args.get(0), event).parseAsSlashArgs();

		if(CommandChecks.argsSizeSubceeds(slashArgs, event, 3, failure)) return;
		String topic = slashArgs.get(0);

		List<Emoji> emojis = Emoji.getPoll();

		if(slashArgs.size() > 10)
		{
			for(int i = 1; i < slashArgs.size() && i < EmbedUtils.REACTION_LIMIT + 1; i++)
			{
				options.append(slashArgs.get(i)).append(" ").append(emojis.get(i - 1).getAsChat()).append("\n");
				reactions.add(emojis.get(i - 1).getUnicode());
			}
		}
		else
		{
			for(int i = 1; i < slashArgs.size() && i < EmbedUtils.REACTION_LIMIT + 1; i++)
			{
				options.append(slashArgs.get(i)).append(" ").append(emojis.get(i - 1).getAsChat()).append("\n\n");
				reactions.add(emojis.get(i - 1).getUnicode());
			}
		}

		channel.sendMessage(new EmbedBuilder()
				.setTitle("Poll:")
				.setDescription(topic)
				.addField("Options:", options.toString(), false)
				.setThumbnail(author.getEffectiveAvatarUrl())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue(message -> reactions.forEach(reaction -> message.addReaction(reaction).queue(null, error -> {})));
	}
}
