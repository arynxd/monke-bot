package me.arynxd.monkebot.commands.commands.developer;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;

@SuppressWarnings("unused")
public class JsonCommand extends Command
{
	public JsonCommand()
	{
		super("Json", "Shows the JSON for a Discord message.", "[id]");
		addAliases("json", "getjson");
		addFlags(CommandFlag.DEVELOPER_ONLY, CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@Nonnull List<String> args, @Nonnull CommandEvent event, @Nonnull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		JDA jda = event.getJDA();
		MessageChannel channel = event.getChannel();

		new RestActionImpl<>(jda, Route.Messages.GET_MESSAGE
				.compile(channel.getId(), args.get(0)), (response, request) ->
		{
			channel.sendMessage(StringUtils.prettyPrintJSON(response.getObject().toString()))
					.allowedMentions(Collections.emptyList())
					.queue();

			return null;
		}).queue(null, error -> failure.accept(new CommandInputException("Message " + args.get(0) + " was not found in this channel.")));
	}
}
