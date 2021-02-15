package me.arynxd.monkebot.commands.subcommands.level;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class LevelBotCommand extends Command
{
	public LevelBotCommand(Command parent)
	{
		super(parent, "bot", "Sets the bot to listen for.", "[user]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;

		new Parser(args.get(0), event).parseAsUser(
				user ->
				{
					GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke()).setLevelUpBot(user.getIdLong());
					event.replySuccess("New level up bot is " + user.getAsMention());
				});
	}
}
