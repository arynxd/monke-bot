package me.arynxd.monkebot.commands.maincommands.developer;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class JsonCommand extends Command
{
	public JsonCommand()
	{
		super("Json", "Shows the JSON for a Discord message.", "[id]");
		addAliases("json", "getjson");
		addFlags(CommandFlag.DEVELOPER_ONLY, CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;

		JDA jda = event.getJDA();
		MessageChannel channel = event.getChannel();

		new RestActionImpl<>(jda, Route.Messages.GET_MESSAGE.compile(channel.getId(), args.get(0)),
				(response, request) ->
				{
					String json = StringUtils.prettyPrintJSON(response.getObject().toString());

					StringUtils.sendPartialMessages(json, event.getChannel());

					return null;
				}).queue(null, error -> failure.accept(new CommandInputException("Message " + args.get(0) + " was not found in this channel.")));
	}
}
