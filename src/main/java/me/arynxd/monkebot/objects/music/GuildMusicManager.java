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
	}

	public void playAll(VoiceChannel channel, List<AudioTrack> tracks)
	{
		AudioManager manager = channel.getGuild().getAudioManager();
		manager.openAudioConnection(channel);
		tracks.forEach(scheduler::queue);
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
}
