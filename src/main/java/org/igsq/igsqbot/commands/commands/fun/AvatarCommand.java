package org.igsq.igsqbot.commands.commands.fun;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;
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
		if(CommandChecks.argsSizeExceeds(event, 3, failure)) return;

		Message message = event.getMessage();
		User author = message.getAuthor();

		if(message.getMentionedMembers().isEmpty())
		{
			event.sendMessage(new EmbedBuilder()
					.setTitle(author.getAsTag() + "'s Avatar")
					.setImage(author.getAvatarUrl() + "?size=4096"));
		}
		else
		{
			message.getMentionedMembers().forEach(member ->
					event.sendMessage(new EmbedBuilder()
							.setTitle(member.getUser().getAsTag() + "'s Avatar")
							.setImage(member.getUser().getEffectiveAvatarUrl() + "?size=4096")));
		}
	}
}
