package me.arynxd.monkebot.commands.commands.music;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicVolumeCommand extends Command
{
	public MusicVolumeCommand()
	{
		super("Volume", "Sets the music volume", "[volume {100}]");
		addAliases("volume");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;
		if(CommandChecks.argsEmpty(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		OptionalInt volume = new Parser(args.get(0), event).parseAsUnsignedInt();

		if(volume.isPresent())
		{
			if(volume.getAsInt() > 100)
			{
				failure.accept(new CommandInputException("Volume must be 100 or lower."));
				return;
			}

			manager.getPlayer().setVolume(volume.getAsInt());
			event.replySuccess("Set the volume to " + volume.getAsInt());
		}
	}
}
