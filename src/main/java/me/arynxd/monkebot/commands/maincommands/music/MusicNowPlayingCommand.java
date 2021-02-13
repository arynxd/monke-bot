package me.arynxd.monkebot.commands.maincommands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.objects.music.GuildMusicHandler;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicNowPlayingCommand extends Command
{
	public MusicNowPlayingCommand()
	{
		super("Now Playing", "Shows whats playing currently", "[none]");
		addAliases("nowplaying", "np");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicHandler manager = musicHandler.getGuildMusicManager(event.getGuild());

		AudioTrack currentTrack = manager.getPlayer().getPlayingTrack();

		if(currentTrack == null)
		{
			failure.accept(new CommandResultException("Nothing is playing."));
			return;
		}
		Duration length = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(currentTrack.getDuration() / 1000));
		Duration passed = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(currentTrack.getPosition() / 1000));


		event.sendMessage(new EmbedBuilder()
				.setTitle("Now playing for " + event.getGuild().getName())
				.setDescription(
						"[" + currentTrack.getInfo().title + "](" + currentTrack.getInfo().uri + ")" +
								"\n**Author**: " + currentTrack.getInfo().author +
								"\n**Position**: " + StringUtils.parseDuration(passed) +
								"\n**Length**: " + StringUtils.parseDuration(length)));
	}
}
