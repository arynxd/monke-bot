package me.arynxd.monkebot.commands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.music.GuildMusicHandler;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicQueueCommand extends Command
{
	public MusicQueueCommand()
	{
		super("Queue", "Shows the queue.", "[none]");
		addAliases("queue", "q");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicHandler manager = musicHandler.getGuildMusicManager(event.getGuild());
		AudioTrack currentTrack = manager.getPlayer().getPlayingTrack();

		List<String> tracks = manager.getScheduler().getQueue()
				.stream()
				.map(track -> track.getInfo().title + " by " + track.getInfo().author)
				.collect(Collectors.toList());

		int size = tracks.size();

		tracks = tracks.subList(0, Math.min(size, 5));

		String trackString = "";
		if(currentTrack != null)
		{
			trackString += "Now Playing: " + currentTrack.getInfo().title + " by " + currentTrack.getInfo().author + "\n\n";
		}

		if(!tracks.isEmpty())
		{
			trackString += "In the queue: \n" + String.join("\n\n", tracks);
		}

		if(size > 0)
		{
			trackString += "\n\n[" + (size - 4) + " more tracks]";
		}

		if(trackString.isBlank())
		{
			failure.accept(new CommandResultException("Nothing is queued."));
			return;
		}

		event.sendMessage(new EmbedBuilder()
				.setTitle("Queue for " + event.getGuild().getName())
				.setDescription(trackString));
	}
}
