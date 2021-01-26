package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ReactionRoleShowCommand extends Command
{
	public ReactionRoleShowCommand(Command parent)
	{
		super(parent, "show", "Shows reaction roles.", "[messageId]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		OptionalLong messageId = new Parser(args.get(0), event).parseAsUnsignedLong();

		if(messageId.isPresent())
		{
			List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(messageId.getAsLong(), event.getIGSQBot());
			StringBuilder text = new StringBuilder();

			for(ReactionRole reactionRole : reactionRoles)
			{
				text
						.append(StringUtils.getEmoteAsMention(reactionRole.getEmote()))
						.append("  ")
						.append(StringUtils.getRoleAsMention(reactionRole.getRoleId()))
						.append("  ")
						.append("\n");
			}

			event.sendMessage(new EmbedBuilder()
					.setTitle("Reaction roles for message " + messageId.getAsLong())
					.setDescription(text.length() == 0 ? "No reaction roles found" : text.toString()));
		}

	}
}
