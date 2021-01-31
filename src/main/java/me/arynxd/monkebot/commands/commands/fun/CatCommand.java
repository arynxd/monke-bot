package me.arynxd.monkebot.commands.commands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.json.RedditPost;
import me.arynxd.monkebot.util.WebUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CatCommand extends Command
{
	public CatCommand()
	{
		super("Cat", "Shows a cute cat.", "[none]");
		addAliases("cat", "cutecat");
	}
	private static final List<String> SUBREDDITS = List.of("kittens", "Kitten", "cutecats", "catsnamedafterfood");

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Random random = new Random();
		List<RedditPost> cats = WebUtils.getPosts(event.getMonke(), SUBREDDITS.get(random.nextInt(SUBREDDITS.size())))
				.stream()
				.filter(post -> !post.isPinned() && !post.isStickied() && post.isMedia())
				.collect(Collectors.toList());



		if(cats.isEmpty())
		{
			failure.accept(new CommandResultException("Couldn't find any cats :pensive:"));
			return;
		}

		RedditPost post = cats.get(random.nextInt(cats.size() - 1));

		WebUtils.checkAndSendPost(event, post, failure);
	}
}
