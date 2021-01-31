package me.arynxd.monkebot.handlers;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import java.util.HashMap;
import java.util.Map;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicHandler 
{
	private final Monke monke;
	private final Map<Long, GuildMusicManager> musicManagers;
	private final AudioPlayerManager playerManager;

	public MusicHandler(Monke monke)
	{
		this.monke = monke;
		this.musicManagers = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();

		AudioSourceManagers.registerLocalSource(playerManager);
		AudioSourceManagers.registerRemoteSources(playerManager);
	}

	public GuildMusicManager getGuildMusicManager(Guild guild)
	{
		GuildMusicManager manager = musicManagers.get(guild.getIdLong());
		if(musicManagers.get(guild.getIdLong()) == null)
		{
			manager = new GuildMusicManager(playerManager);
			musicManagers.put(guild.getIdLong(), manager);
		}

		guild.getAudioManager().setSendingHandler(manager.getSendHandler());
		return manager;
	}


	public Monke getMonke()
	{
		return monke;
	}

	public Map<Long, GuildMusicManager> getMusicManagers()
	{
		return musicManagers;
	}

	public AudioPlayerManager getPlayerManager()
	{
		return playerManager;
	}

	public void cleanupPlayers()
	{
		monke.getShardManager().getGuilds().forEach(guild ->
		{
			GuildMusicManager manager = musicManagers.get(guild.getIdLong());
			VoiceChannel vc = guild.getAudioManager().getConnectedChannel();
			if(vc == null)
			{
				return; // if we arent connected theres no point in checking.
			}
			long humansInVC = vc.getMembers().stream().filter(member ->  !member.getUser().isBot()).count();
			if(humansInVC == 0)
			{
				manager.getPlayer().destroy();
				manager.leave(guild);
				manager.getScheduler().clear();
			}
		});
	}
}

