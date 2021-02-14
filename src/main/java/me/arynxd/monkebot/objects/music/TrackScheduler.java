package me.arynxd.monkebot.objects.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class TrackScheduler extends AudioEventAdapter
{
	private final AudioPlayer player;
	private final LinkedList<AudioTrack> queue;
	private final GuildMusicManager handler;

	public TrackScheduler(AudioPlayer player, GuildMusicManager handler)
	{
		this.player = player;
		this.queue = new LinkedList<>();
		this.handler = handler;
	}

	public void queue(AudioTrack track)
	{
		if(!player.startTrack(track, true))
		{
			synchronized(queue)
			{
				queue.offer(track);
			}
		}
	}

	public List<AudioTrack> getQueue()
	{
		return queue;
	}

	public void skipOne()
	{
		synchronized(queue)
		{
			player.startTrack(queue.poll(), false);
		}
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	{
		if(endReason.mayStartNext)
		{
			skipOne();
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track)
	{
		Duration length = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(track.getDuration() / 1000));
		Duration passed = Duration.between(LocalDateTime.now(), LocalDateTime.now().plusSeconds(track.getPosition() / 1000));

		if(handler != null && handler.getChannel() != null)
		{
			handler.getChannel().sendMessage(new EmbedBuilder()
					.setTitle("Now playing")
					.setDescription(
							"[" + track.getInfo().title + "](" + track.getInfo().uri + ")" +
									"\n**Author**: " + track.getInfo().author +
									"\n**Position**: " + StringUtils.parseDuration(passed) +
									"\n**Length**: " + StringUtils.parseDuration(length))
					.setColor(Constants.EMBED_COLOUR)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
	{
		if(handler != null && handler.getChannel() != null)
		{
			handler.getChannel().sendMessage(new EmbedBuilder()
					.setTitle("Something went wrong")
					.setDescription("An error occurred while playing " + track.getInfo().title)
					.setColor(Constants.EMBED_COLOUR)
					.setTimestamp(Instant.now())
					.build()).queue();
		}
	}

	public boolean hasNext()
	{
		return queue.peek() != null;
	}

	public void clear()
	{
		queue.clear();
	}

	public void shuffle()
	{
		Collections.shuffle(queue);
	}
}
