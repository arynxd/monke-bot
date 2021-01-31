package me.arynxd.monkebot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.json.Meme;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MemeCommand extends Command
{
	public MemeCommand()
	{
		super("Meme" ,"Shows the best memes from Reddit.", "[wholesome / dank / irl / facebook / karen]");
		addAliases("meme");
	}
	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		String subreddit;

		if(args.isEmpty())
		{
			subreddit = "memes";
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
					default -> "memes";
				};
		}


		Random random = new Random();
		List<Meme> memes = getMemes(event.getMonke(), subreddit)
				.stream()
				.filter(meme -> !meme.isPinned() && !meme.isStickied())
				.collect(Collectors.toList());

		if(memes.isEmpty())
		{
			failure.accept(new CommandResultException("No memes were found."));
			return;
		}

		Meme meme = memes.get(random.nextInt(memes.size()));

		if(event.isFromGuild() && meme.isNSFW() && !event.getTextChannel().isNSFW())
		{
			failure.accept(new CommandResultException("The meme selected was marked as NSFW and cannot be shown here, please try again."));
			return;
		}

		event.sendMessage(new EmbedBuilder()
				.setTitle(meme.getTitle())
				.setDescription("Taken from " + meme.getSubreddit() + " by " + meme.getAuthor())
				.setFooter(meme.getUpvotes() + " upvotes : " + meme.getDownvotes() + " downvotes | ")
				.setImage(meme.getURL()));
	}

	private List<Meme> getMemes(Monke monke, String subreddit)
	{
		Request request = new Request.Builder()
				.url("https://www.reddit.com/r/" + subreddit + "/.json")
				.build();

		List<Meme> memes = new ArrayList<>();
		try(Response response = monke.getOkHttpClient().newCall(request).execute())
		{
			ResponseBody body = response.body();
			if(body != null)
			{
				DataObject dataObject = DataObject.fromJson(body.string());
				if(dataObject.hasKey("data") && dataObject.getObject("data").hasKey("children"))
				{
					DataArray dataArray = dataObject.getObject("data").getArray("children");

					for(int i = 0; i < dataArray.length(); i++)
					{
						DataObject meme = dataArray.getObject(i);
						if(meme.hasKey("data"))
						{
							memes.add(new Meme(meme.getObject("data")));
						}
					}
				}
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("An OKHTTP error has occurred.", exception);
		}
		return memes;
	}
}
