package me.arynxd.monkebot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.FileUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class StealCommand extends Command
{
	public StealCommand()
	{
		super("Steal", "Steals an image URL and adds it as an emoji. Emoji names must be A-Z with underscores.", "[name] [imageURL]");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("steal");
		addMemberPermissions(Permission.MANAGE_EMOTES);
		addSelfPermissions(Permission.MANAGE_EMOTES);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure) || CommandChecks.stringIsURL(args.get(1), event, failure))
			return;

		if(!args.get(0).matches("([A-Z]|[a-z]|_)\\w+"))
		{
			failure.accept(new CommandInputException("Emoji names must be A-Z with underscores (_)"));
			return;
		}

		Icon icon = FileUtils.getIcon(args.get(1));
		if(icon == null)
		{
			failure.accept(new CommandResultException("The image / gif provided could not be loaded."));
		}
		else
		{
			event.getGuild().createEmote(args.get(0), icon).queue(
					emote -> event.replySuccess("Added emote " + emote.getAsMention() + " successfully!"),
					error -> event.replyError("An error occurred while adding the emote."));
		}
	}
}