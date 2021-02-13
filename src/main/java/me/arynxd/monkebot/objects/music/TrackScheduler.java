package me.arynxd.monkebot.objects.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter
{
	private final AudioPlayer player;
	private final LinkedList<AudioTrack> queue;

	public TrackScheduler(AudioPlayer player)
	{
		this.player = player;
		this.queue = new LinkedList<>();
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
		player.startTrack(queue.poll(), false);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	{
		if(endReason.mayStartNext)
		{
			skipOne();
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
