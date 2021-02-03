package me.arynxd.monkebot.commands.commands.music;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.music.GuildMusicHandler;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicShuffleCommand extends Command
{
	public MusicShuffleCommand()
	{
		super("Shuffle", "Shuffles the queue.", "[none]");
		addAliases("shuffle");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicHandler manager = musicHandler.getGuildMusicManager(event.getGuild());

		if(manager.getScheduler().hasNext())
		{
			manager.getScheduler().shuffle();
			event.replySuccess("Shuffled the queue.");
		}
		else
		{
			failure.accept(new CommandResultException("No more tracks queued."));
		}
	}
}
