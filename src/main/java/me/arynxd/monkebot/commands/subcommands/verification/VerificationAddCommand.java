package me.arynxd.monkebot.commands.subcommands.verification;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.VerificationUtils;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class VerificationAddCommand extends Command
{
	public VerificationAddCommand(Command parent)
	{
		super(parent, "add", "Adds a new phrase/role pair.", "[role][phrase{2}]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeExceeds(event, 3, failure)) return;
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		new Parser(args.get(0), event).parseAsRole(
				role ->
				{
					String phrase = String.join(" ", args.subList(1, args.size()));

					if(VerificationUtils.addMapping(phrase, event.getGuildIdLong(), role.getIdLong(), event.getMonke()))
					{
						event.replySuccess("Added mapping " + phrase + " for role " + role.getAsMention());
					}
					else
					{
						event.replyError("Mapping " + phrase + " for role " + role.getAsMention() + " already exists.");
					}
				});
	}
}
