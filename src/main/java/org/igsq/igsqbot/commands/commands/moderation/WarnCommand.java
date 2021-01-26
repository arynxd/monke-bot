package org.igsq.igsqbot.commands.commands.moderation;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.commands.subcommands.warning.WarningRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.warning.WarningShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandHierarchyException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", "Handles the user warning system", "[user][reason] / [show] / [remove]");
		addAliases("warn");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addChildren(
				new WarningShowCommand(this),
				new WarningRemoveCommand(this));
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		User author = event.getAuthor();
		Guild guild = event.getGuild();

		new Parser(args.get(0), event).parseAsUser(user ->
		{
			if(user.isBot())
			{
				failure.accept(new CommandResultException("Bots cannot be warned."));
				return;
			}

			if(user.equals(author))
			{
				failure.accept(new CommandHierarchyException(this));
				return;
			}
			CommandUtils.interactionCheck(author, user, event, () ->
			{
				args.remove(0);
				String reason = String.join(" ", args);
				new Warning(guild, user, event.getIGSQBot()).add(reason);
				event.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);

				user.openPrivateChannel()
						.flatMap(privateChannel -> privateChannel.sendMessage(new EmbedBuilder()
								.setTitle("You have been warned in " + guild.getName())
								.addField("Reason", reason, true)
								.setColor(Constants.IGSQ_PURPLE)
								.setTimestamp(Instant.now())
								.build())).queue(null, error ->
				{});
			});
		});
	}
}