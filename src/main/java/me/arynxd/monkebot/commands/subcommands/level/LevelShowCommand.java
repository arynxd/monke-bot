package me.arynxd.monkebot.commands.subcommands.level;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.entities.database.Level;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.jooq.tables.pojos.Levels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
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
		User levelBot = event.getMonke().getShardManager().getUserById(new GuildConfig(event).getLevelUpBot());
		if(CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
		List<Levels> levelList = Level.getLevels(event.getGuildIdLong(), event.getMonke());

		StringBuilder text = new StringBuilder();

		for(Levels level : levelList)
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