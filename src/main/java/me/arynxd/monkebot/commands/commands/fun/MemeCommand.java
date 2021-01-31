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
public class MemeCommand extends Command
{
	public MemeCommand()
	{
		super("RedditPost" ,"Shows the best memes from Reddit.", "[wholesome / dank / irl / facebook / karen]");
		addAliases("meme");
	}
	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Random random = new Random();
		String subreddit;

		if(args.isEmpty())
		{
			subreddit = "post";
		}
		else
		{
			subreddit = switch(args.get(0))
				{
					case "wholesome" -> "wholesomememes";
					case "dank" -> "dankmemes";
					case "irl" -> "me_irl";
					case "facebook" -> "terriblefacebookmemes";
					case "karen" -> "karen";
					default -> "post";
				};
		}


		List<RedditPost> memes = WebUtils.getPosts(event.getMonke(), subreddit)
				.stream()
				.filter(post -> !post.isPinned() && !post.isStickied())
				.collect(Collectors.toList());

		if(memes.isEmpty())
		{
			failure.accept(new CommandResultException("No post were found."));
			return;
		}

		RedditPost post = memes.get(random.nextInt(memes.size() - 1));
		WebUtils.checkAndSendPost(event, post, failure);
	}
}
