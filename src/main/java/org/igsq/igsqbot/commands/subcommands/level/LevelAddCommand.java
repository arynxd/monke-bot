package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Level;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class LevelAddCommand extends Command
{
	public LevelAddCommand(Command parent)
	{
		super(parent, "add", "Adds a new level entry.", "[level][role]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		User levelBot = event.getIGSQBot().getShardManager().getUserById(new GuildConfig(event).getLevelUpBot());
		if(CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;
		OptionalInt level = new Parser(args.get(0), event).parseAsUnsignedInt();

		if(level.isPresent())
		{
			new Parser(args.get(1), event).parseAsRole(
					role ->
					{
						Level.addLevel(role, level.getAsInt(), event.getGuildIdLong(), event.getIGSQBot());
						event.replySuccess("Added new level " + level.getAsInt() + " which awards " + role.getAsMention());
					});
		}
	}
}
