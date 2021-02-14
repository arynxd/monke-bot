package me.arynxd.monkebot.objects.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class GuildMusicManager
{
	private final AudioPlayer player;
	private final TrackScheduler scheduler;
	private MessageChannel channel;
	private int volume = 30;

	public GuildMusicManager(AudioPlayerManager manager)
	{
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player, this);
		player.addListener(scheduler);
	}

	public MessageChannel getChannel()
	{
		return channel;
	}

	public AudioPlayer getPlayer()
	{
		return player;
	}

	public TrackScheduler getScheduler()
	{
		return scheduler;
	}

	public AudioPlayerSendHandler getSendHandler()
	{
		return new AudioPlayerSendHandler(player);
	}

	public void play(VoiceChannel channel, AudioTrack track)
	{
		AudioManager manager = channel.getGuild().getAudioManager();
		manager.openAudioConnection(channel);
		scheduler.queue(track);
		player.setVolume(volume);
	}

	public void playAll(VoiceChannel channel, List<AudioTrack> tracks)
	{
		AudioManager manager = channel.getGuild().getAudioManager();
		manager.openAudioConnection(channel);
		tracks.forEach(scheduler::queue);
		player.setVolume(volume);
	}

	public void togglePause()
	{
		player.setPaused(!player.isPaused());
	}

	public boolean getPaused()
	{
		return player.isPaused();
	}

	public void leave(Guild guild)
	{
		AudioManager manager = guild.getAudioManager();
		manager.closeAudioConnection();
	}

	public void join(VoiceChannel channel)
	{
		AudioManager manager = channel.getGuild().getAudioManager();
		manager.openAudioConnection(channel);
		player.setVolume(volume);
	}

	public void kill(Guild guild)
	{
		leave(guild);
		player.destroy();
		scheduler.clear();
	}

	public void bind(MessageChannel channel)
	{
		if(this.channel == null)
		{
			this.channel = channel;
		}
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}
}
