package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ReactionRoleRemoveCommand extends Command
{
	public ReactionRoleRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes reaction roles.", "[messageId][channel][role][emote]");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MANAGE_SERVER);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 4, failure)) return;

		OptionalLong messageId = new Parser(args.get(0), event).parseAsUnsignedLong();
		String emote = event.getMessage().getEmotes().isEmpty() ? args.get(3) : event.getMessage().getEmotes().get(0).getId();
		new Parser(args.get(2), event).parseAsRole(role ->
		{
			if(messageId.isPresent())
			{
				new Parser(args.get(1), event).parseAsTextChannel(
						channel ->
						{
							channel.retrieveMessageById(messageId.getAsLong()).queue(
									message ->
									{
										ReactionRole reactionRole = new ReactionRole(messageId.getAsLong(), role.getIdLong(), event.getGuild().getIdLong(), emote, event.getIGSQBot());

										if(!reactionRole.isPresent())
										{
											failure.accept(new CommandResultException("That reaction role does not exist"));
											return;
										}

										reactionRole.remove();
										event.replySuccess("Removed reaction role for role " + StringUtils.getRoleAsMention(role.getIdLong()));
										message.clearReactions(emote).queue();
									},
									error -> failure.accept(new CommandInputException("Message " + messageId.getAsLong() + " does not exist")));
						});
			}
		});
	}
}
