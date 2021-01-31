package me.arynxd.monkebot.commands.commands.music;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicSkipCommand extends Command
{
	public MusicSkipCommand()
	{
		super("Skip", "Skips the queue forward by the given amount or 1 if none is specified.", "[none]");
		addAliases("skip");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		if(manager.getScheduler().hasNext())
		{
			manager.getScheduler().skipOne();
			event.replySuccess("Skipped 1 track.");
		}
		else
		{
			event.replyError("No more tracks queued.");
		}
	}
}
