package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.jetbrains.annotations.NotNull;

public class BlacklistShowCommand extends Command
{
	public BlacklistShowCommand(Command parent)
	{
		super(parent, "show", "Shows all blacklisted phrases for this server.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		List<String> blacklist = BlacklistUtils.getBlacklistedPhrases(event.getGuild(), event.getIGSQBot());
		StringBuilder text = new StringBuilder();

		for(String word : blacklist)
		{
			text
					.append("||")
					.append(word)
					.append("||")
					.append("\n");
		}

		event.sendMessage(new EmbedBuilder()
				.setTitle("Blacklisted words for server " + event.getGuild().getName())
				.setDescription(text.length() == 0 ? "No blacklisted words setup." : text.toString()));
	}
}
