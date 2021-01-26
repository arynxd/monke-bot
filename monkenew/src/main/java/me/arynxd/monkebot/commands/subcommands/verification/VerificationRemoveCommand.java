package me.arynxd.monkebot.commands.subcommands.verification;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import net.dv8tion.jda.api.Permission;
import me.arynxd.monkebot.util.ArrayUtils;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.VerificationUtils;
import org.jetbrains.annotations.NotNull;

public class VerificationRemoveCommand extends Command
{
	public VerificationRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a phrase/role pair.", "[phrase{2}][role]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeExceeds(event, 3, failure)) return;

		new Parser(args.get(0), event).parseAsRole(
				role ->
				{
					String phrase = ArrayUtils.arrayCompile(args.subList(1, args.size()), " ");

					if(VerificationUtils.removeMapping(phrase, event.getGuildIdLong(), role.getIdLong(), event.getMonke()))
					{
						event.replySuccess("Remove mapping " + phrase + " for role " + role.getAsMention());
					}
					else
					{
						event.replyError("Mapping " + phrase + " for role " + role.getAsMention() + " does not exist.");
					}
				});
	}
}
