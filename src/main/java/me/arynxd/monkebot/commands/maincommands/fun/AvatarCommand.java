package me.arynxd.monkebot.commands.maincommands.fun;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.Parser;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AvatarCommand extends Command
{
	public AvatarCommand()
	{
		super("Avatar", "Shows the avatar for the specified user(s).", "<users {3}>");
		addAliases("avatar", "avi", "pfp");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(args.isEmpty())
		{
			event.sendMessage(new EmbedBuilder()
					.setTitle(event.getAuthor().getAsTag() + "'s Avatar")
					.setImage(event.getAuthor().getAvatarUrl() + "?size=4096"));
			return;
		}
		new Parser(args.get(0), event).parseAsUser(user ->
				event.sendMessage(new EmbedBuilder()
						.setTitle(user.getAsTag() + "'s Avatar")
						.setImage(user.getAvatarUrl() + "?size=4096")));
	}
}
