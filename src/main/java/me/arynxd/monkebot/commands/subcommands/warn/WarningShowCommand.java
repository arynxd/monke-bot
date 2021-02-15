package me.arynxd.monkebot.commands.subcommands.warn;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Warning;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.objects.jooq.tables.pojos.Warnings;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class WarningShowCommand extends Command
{
	public WarningShowCommand(Command parent)
	{
		super(parent, "show", "Shows a user's warnings.", "[user]");
		addAliases("list");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsEmpty(event, failure)) return;

		new Parser(args.get(0), event).parseAsUser(user ->
		{
			if (user.isBot())
			{
				failure.accept(new CommandInputException("Bots cannot have warnings."));
				return;
			}

			Guild guild = event.getGuild();
			MessageChannel channel = event.getChannel();
			List<Warnings> warnings = new Warning(guild, user, event.getMonke()).get();
			StringBuilder stringBuilder = new StringBuilder();

			warnings.forEach(warn -> stringBuilder
					.append("**ID: ")
					.append(warn.getId())
					.append("** ")
					.append(warn.getWarnText())
					.append(" - ")
					.append(StringUtils.parseDateTime(warn.getTimestamp()))
					.append("\n"));

			channel.sendMessage(new EmbedBuilder()
					.setTitle("Warnings for " + user.getAsTag())
					.setDescription(stringBuilder.length() == 0 ? "This user has no warnings" : stringBuilder.toString())
					.setColor(Constants.EMBED_COLOUR)
					.build()).queue();
		});
	}
}
