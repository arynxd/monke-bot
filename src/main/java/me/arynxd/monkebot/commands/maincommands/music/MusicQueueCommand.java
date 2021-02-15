package me.arynxd.monkebot.commands.maincommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.objects.music.GuildMusicManager;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
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
		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		if(CommandChecks.boundToChannel(manager, event.getChannel(), failure)) return;
		if(CommandChecks.sharesVoice(event, failure)) return;

		AudioTrack currentTrack = manager.getPlayer().getPlayingTrack();

		List<String> tracks = manager.getScheduler().getQueue()
				.stream()
				.map(track -> "[" + track.getInfo().title + "](" + track.getInfo().uri + ")" + " by " + track.getInfo().author)
				.collect(Collectors.toList());

		int size = tracks.size();

		tracks = tracks.subList(0, Math.min(size, 5));

		String trackString = "";
		if(currentTrack != null)
		{
			trackString += "**Now Playing**: " + currentTrack.getInfo().title + " by " + currentTrack.getInfo().author + "\n\n";
		}

		if(!tracks.isEmpty())
		{
			trackString += "**In the queue**: \n" + String.join("\n\n", tracks);
		}

		if(size >= 6)
		{
			trackString += "\n\n[" + (size - 5) + StringUtils.plurify(" more track", size - 5) + "]";
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
