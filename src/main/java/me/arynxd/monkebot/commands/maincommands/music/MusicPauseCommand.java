package me.arynxd.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.music.GuildMusicManager;
import me.arynxd.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicPauseCommand extends Command
{
	public MusicPauseCommand()
	{
		super("Pause", "Pauses the music player.", "[none]");
		addAliases("pause", "stop");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
		if (CommandChecks.sharesVoice(event, failure)) return;

		manager.togglePause();
		event.replySuccess(manager.getPaused() ? "Paused the player." : "Unpaused the player.");
	}
}
