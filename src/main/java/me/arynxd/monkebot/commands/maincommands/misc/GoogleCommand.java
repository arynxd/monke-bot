package me.arynxd.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import me.arynxd.monkebot.handlers.CooldownHandler;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandCooldownException;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


@SuppressWarnings("unused")
public class GoogleCommand extends Command
{
	private static final ExecutorService GOOGLE_EXECUTOR = Executors.newCachedThreadPool();
	public GoogleCommand()
	{
		super("Google", "Searches Google.", "[query]");
		addAliases("google", "g");
		setCooldown(5000L);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		if(CooldownHandler.isOnCooldown(event.getMember(), this))
		{
			failure.accept(new CommandCooldownException(this));
			return;
		}

		GOOGLE_EXECUTOR.submit(() -> //We require an executor as Jsoup is sync only
		{
			try
			{
				Document doc = Jsoup.connect("https://www.google.com/search?q=" +
						StringUtils.URLSanitize(String.join("%20", args))  //Remove any URL params the user inputs
						+ "&safe=active").get(); //Enable safe search
				CooldownHandler.addCooldown(event.getMember(), this);
				Elements links = doc.select(".yuRUbf").select("a"); //Select the first google link
				Elements names = doc.select(".yuRUbf").select("a").select("h3").select("span"); //Select its 'name'
				Elements descriptions = doc.select(".IsZvec").select("div").select(".aCOpRe").select("span"); //Select its description

				if(links.isEmpty() || names.isEmpty() || descriptions.isEmpty())
				{
					failure.accept(new CommandResultException("No results found."));
					return;
				}

				event.getChannel().sendMessage(
						links.get(0).attr("href") + //Grab the actual URL from the 'a' tag
								" --> *" + names.get(0).text() +
								"* \n" + descriptions.get(0).text()).queue();
			}
			catch(Exception exception)
			{
				failure.accept(new CommandResultException("Google search failed!"));
				event.getMonke().getLogger().error("A Google search exception as occurred", exception);
			}
		});
	}
}
