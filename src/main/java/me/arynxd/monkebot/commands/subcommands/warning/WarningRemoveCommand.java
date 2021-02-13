package me.arynxd.monkebot.commands.subcommands.warning;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Warning;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandHierarchyException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Warnings;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.CommandUtils;
import me.arynxd.monkebot.util.Parser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class WarningRemoveCommand extends Command
{
	public WarningRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a warning.", "[user][warningID]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
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
						failure.accept(new CommandInputException("Bots cannot have warnings."));
						return;
					}

					if(user.equals(author))
					{
						failure.accept(new CommandHierarchyException(this));
						return;
					}

					CommandUtils.interactionCheck(author, user, event, () ->
					{
						OptionalInt warningNumber = new Parser(args.get(1), event).parseAsUnsignedInt();
						if(warningNumber.isPresent())
						{
							Warnings warn = new Warning(event.getGuild(), user, event.getMonke()).getByWarnId(warningNumber.getAsInt());

							if(warn == null)
							{
								failure.accept(new CommandInputException("Invalid warning specified."));
								return;
							}

							new Warning(guild, user, event.getMonke()).remove(warningNumber.getAsInt());
							event.replySuccess("Removed warning: " + warn.getWarnText());
						}
					});
				}
		);
	}
}
