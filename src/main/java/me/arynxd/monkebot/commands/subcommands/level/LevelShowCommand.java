package me.arynxd.monkebot.commands.subcommands.level;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Level;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Levels;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class LevelShowCommand extends Command
{
	public LevelShowCommand(Command parent)
	{
		super(parent, "show", "Shows information about the levels.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		User levelBot = event.getMonke().getShardManager().getUserById(GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke()).getLevelUpBot());
		if (CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
		List<Levels> levelList = Level.getLevels(event.getGuildIdLong(), event.getMonke());

		StringBuilder text = new StringBuilder();

		for (Levels level : levelList)
		{
			text
					.append("Role ")
					.append(StringUtils.getRoleAsMention(level.getRoleId()))
					.append(" awarded at ")
					.append(levelBot.getAsMention())
					.append(" level ")
					.append(level.getAwardedAt())
					.append("\n");
		}

		event.sendMessage(new EmbedBuilder()
				.setTitle("Configured levels for " + event.getGuild().getName())
				.setDescription(text.length() == 0 ? "No levels configured" : text.toString()));
	}
}
