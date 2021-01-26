package me.arynxd.monkebot.commands.subcommands.warning;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.Warning;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import me.arynxd.monkebot.entities.jooq.tables.pojos.Warnings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class WarningShowCommand extends Command
{
	public WarningShowCommand(Command parent)
	{
		super(parent, "show", "Shows a user's warnings.", "[user]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		new Parser(args.get(0), event).parseAsUser(user ->
		{
			if(user.isBot())
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
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
		});
	}
}
