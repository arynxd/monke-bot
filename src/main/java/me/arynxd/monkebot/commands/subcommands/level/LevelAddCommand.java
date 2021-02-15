package me.arynxd.monkebot.commands.subcommands.level;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Level;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import net.dv8tion.jda.api.entities.User;
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
		User levelBot = event.getMonke().getShardManager().getUserById(GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke()).getLevelUpBot());
		if (CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
		if (CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		OptionalInt level = new Parser(args.get(0), event).parseAsUnsignedInt();

		if (level.isPresent())
		{
			new Parser(args.get(1), event).parseAsRole(
					role ->
					{
						Level.addLevel(role, level.getAsInt(), event.getGuildIdLong(), event.getMonke());
						event.replySuccess("Added new level " + level.getAsInt() + " which awards " + role.getAsMention());
					});
		}
	}
}
