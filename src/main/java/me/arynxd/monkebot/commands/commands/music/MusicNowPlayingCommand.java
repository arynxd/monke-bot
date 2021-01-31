package me.arynxd.monkebot.commands.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
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
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		AudioTrack currentTrack = manager.getPlayer().getPlayingTrack();

		if(currentTrack == null)
		{
			failure.accept(new CommandResultException("Nothing is playing."));
			return;
		}

		event.sendMessage(new EmbedBuilder()
				.setTitle("Now playing for " + event.getGuild().getName())
				.setDescription(currentTrack.getInfo().title + " by " + currentTrack.getInfo().author));
	}
}
