package me.arynxd.monkebot.commands.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
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
import me.arynxd.monkebot.util.IOUtils;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicPlayCommand extends Command
{
	public MusicPlayCommand()
	{
		super("Play", "Plays music from Youtube or Soundcloud.", "[song]");
		addAliases("play");
	}
	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());
		VoiceChannel channel = event.getMember().getVoiceState().getChannel();
		String query = String.join("", args);
		addFlags(CommandFlag.GUILD_ONLY);

		if(!IOUtils.isURL(query))
		{
			query = "ytsearch:" + query;
		}

		musicHandler.getPlayerManager().loadItemOrdered(manager, query, new AudioLoadResultHandler()
		{
			@Override
			public void trackLoaded(AudioTrack track)
			{
				event.replySuccess("Added **" + track.getInfo().title + "** to the queue.");
				manager.play(channel, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist)
			{
				if(playlist.isSearchResult())
				{
					event.replySuccess("Added **" + playlist.getTracks().get(0).getInfo().title + "** to the queue.");
					manager.play(channel, playlist.getTracks().get(0));
				}
				else
				{
					event.replySuccess("Added " + playlist.getTracks().size() + " tracks to the queue.");
					manager.playAll(channel, playlist.getTracks());
				}
			}

			@Override
			public void noMatches()
			{
				failure.accept(new CommandResultException("Couldn't find anything matching your query."));
			}

			@Override
			public void loadFailed(FriendlyException exception)
			{
				failure.accept(new CommandResultException("An error occurred while loading the song."));
			}
		});
	}
}
