package me.arynxd.monkebot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.json.RedditPost;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebUtils
{
	private WebUtils()
	{
		//Overrides the default, public, constructor
	}

	public static List<RedditPost> getPosts(Monke monke, String subreddit)
	{
		Request request = new Request.Builder()
				.url("https://www.reddit.com/r/" + subreddit + "/.json")
				.build();

		List<RedditPost> post = new ArrayList<>();
		try(Response response = monke.getOkHttpClient().newCall(request).execute())
		{
			ResponseBody responseBody = response.body();
			if(responseBody != null)
			{
				DataObject redditJson = DataObject.fromJson(responseBody.string());
				if(redditJson.hasKey("data") && redditJson.getObject("data").hasKey("children"))
				{
					DataArray memeArray = redditJson.getObject("data").getArray("children");

					for(int i = 0; i < memeArray.length(); i++)
					{
						DataObject meme = memeArray.getObject(i);
						if(meme.hasKey("data"))
						{
							post.add(new RedditPost(meme.getObject("data")));
						}
					}
				}
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An OKHTTP error has occurred.", exception);
		}
		return post;
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
