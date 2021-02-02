package me.arynxd.monkebot.commands.commands.music;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.music.GuildMusicManager;
import me.arynxd.monkebot.handlers.MusicHandler;
import me.arynxd.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MusicLeaveCommand extends Command
{
	public MusicLeaveCommand()
	{
		super("Leave", "Makes the bot leave the VC.", "[none]");
		addAliases("leave");
		addFlags(CommandFlag.GUILD_ONLY);
	}
	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.sharesVoice(event, failure)) return;
		if(CommandChecks.inVoice(event, failure)) return;

		MusicHandler musicHandler = event.getMonke().getMusicHandler();
		GuildMusicManager manager = musicHandler.getGuildMusicManager(event.getGuild());

		manager.getPlayer().destroy();
		manager.leave(event.getGuild());
		manager.getScheduler().clear();

		event.replySuccess("Bye!");
	}
}
