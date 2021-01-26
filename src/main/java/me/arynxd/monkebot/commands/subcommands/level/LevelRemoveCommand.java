package me.arynxd.monkebot.commands.subcommands.level;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.entities.database.Level;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import net.dv8tion.jda.api.entities.User;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class LevelRemoveCommand extends Command
{
	public LevelRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a level entry.", "[level][role]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		User levelBot = event.getMonke().getShardManager().getUserById(new GuildConfig(event).getLevelUpBot());
		if(CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;
		OptionalInt level = new Parser(args.get(0), event).parseAsUnsignedInt();
		if(level.isPresent())
		{
			new Parser(args.get(1), event).parseAsRole(
					role ->
					{
						if(Level.removeLevel(role, level.getAsInt(), event.getGuildIdLong(), event.getMonke()))
						{
							event.replySuccess("Removed level " + level.getAsInt() + " which awarded " + role.getAsMention());
						}
						else
						{
							failure.accept(new CommandResultException("Level " + level.getAsInt() + " with role " + role.getAsMention() + " was not found."));
						}
					});
		}
	}
}
