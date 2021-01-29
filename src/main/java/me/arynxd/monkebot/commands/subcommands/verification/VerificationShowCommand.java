package me.arynxd.monkebot.commands.subcommands.verification;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.util.StringUtils;
import me.arynxd.monkebot.util.VerificationUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

public class VerificationShowCommand extends Command
{
	public VerificationShowCommand(Command parent)
	{
		super(parent, "show", "Shows the configured phrases and roles.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Map<String, Long> phrases = VerificationUtils.getMappedPhrases(event.getGuild(), event.getMonke());
		StringBuilder text = new StringBuilder();

		phrases.forEach(
				(phrase, role) -> text
						.append(StringUtils.getRoleAsMention(role))
						.append(" -> ")
						.append(phrase)
						.append("\n"));

		event.sendMessage(new EmbedBuilder()
				.setTitle("Aliases setup for " + event.getGuild().getName())
				.setDescription(text.length() == 0 ? "No aliases setup" : text.toString()));
	}
}
