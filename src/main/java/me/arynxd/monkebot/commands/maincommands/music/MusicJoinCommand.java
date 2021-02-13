package me.arynxd.monkebot.commands.maincommands.music;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.music.GuildMusicHandler;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicJoinCommand extends Command
{
	public MusicJoinCommand()
	{
		super("Join", "Makes the bot join your VC.", "[none]");
		addAliases("join");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicHandler manager = musicHandler.getGuildMusicManager(event.getGuild());
		VoiceChannel channel = event.getMember().getVoiceState().getChannel();
		manager.join(channel);
		event.replySuccess("Joined " + channel.getName());
	}
}
