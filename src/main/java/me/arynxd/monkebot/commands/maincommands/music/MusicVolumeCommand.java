package me.arynxd.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.objects.music.GuildMusicManager;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
public class MusicVolumeCommand extends Command
{
	public MusicVolumeCommand()
	{
		super("Volume", "Sets the music volume", "<volume {100}>");
		addAliases("volume", "vol");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		if (CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
		if (CommandChecks.sharesVoice(event, failure)) return;

		if (args.isEmpty())
		{
			event.replySuccess("The volume is " + manager.getPlayer().getVolume() + "%");
			return;
		}

		OptionalInt volume = new Parser(args.get(0), event).parseAsUnsignedInt();

		if (volume.isPresent())
		{
			if (volume.getAsInt() > 100)
			{
				failure.accept(new CommandInputException("Volume must be 100 or lower."));
				return;
			}

			manager.setVolume(volume.getAsInt());
			event.replySuccess("Set the volume to " + volume.getAsInt() + "%");
		}
	}
}
