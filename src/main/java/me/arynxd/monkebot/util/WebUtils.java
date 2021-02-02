package me.arynxd.monkebot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.json.RedditPost;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

public class WebUtils
{
	private WebUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void getPosts(CommandEvent event, String subreddit, Consumer<List<RedditPost>> success, Consumer<CommandException> failure)
	{
		Request request = new Request.Builder()
				.url("https://www.reddit.com/r/" + subreddit + "/.json")
				.build();


		event.getMonke().getOkHttpClient().newCall(request).enqueue(new Callback()
		{
			@Override public void onFailure(@NotNull Call call, @NotNull IOException exception)
			{
				event.getMonke().getLogger().error("An OKHTTP error has occurred", exception);
				failure.accept(new CommandResultException("Failed to fetch posts."));
			}

			@Override public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
			{
				try (ResponseBody responseBody = response.body())
				{
					List<RedditPost> post = new ArrayList<>();

					if(responseBody == null)
					{
						failure.accept(new CommandResultException("The retrieved posts were empty."));
						return;
					}

					DataObject redditJson = DataObject.fromJson(responseBody.string());


					if(!redditJson.hasKey("data") && !redditJson.getObject("data").hasKey("children"))
					{
						failure.accept(new CommandResultException("The data Reddit provided was corrupt."));
						return;
					}

					DataArray memeArray = redditJson.getObject("data").getArray("children");

					for(int i = 0; i < memeArray.length(); i++)
					{
						DataObject meme = memeArray.getObject(i);
						if(meme.hasKey("data"))
						{
							post.add(new RedditPost(meme.getObject("data")));
						}
					}

					if(post.isEmpty())
					{
						failure.accept(new CommandResultException("Reddit provided no posts."));
						return;
					}

					success.accept(post);
				}
			}
		});
	}

	public static void checkAndSendPost(CommandEvent event, RedditPost post, Consumer<CommandException> failure)
	{
		if(CommandChecks.canPost(event, post, failure)) return;

		event.sendMessage(new EmbedBuilder()
				.setTitle(post.getTitle())
				.setDescription("Taken from " + post.getSubreddit() + " by " + post.getAuthor())
				.setFooter(post.getUpvotes() + " upvotes : " + post.getDownvotes() + " downvotes | ")
				.setImage(post.getURL()));
	}
}
