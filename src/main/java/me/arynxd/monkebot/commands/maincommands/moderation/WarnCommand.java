package me.arynxd.monkebot.commands.maincommands.moderation;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.commands.subcommands.warn.WarnRemoveCommand;
import me.arynxd.monkebot.commands.subcommands.warn.WarningShowCommand;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.Warning;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandHierarchyException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.objects.exception.CommandSyntaxException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.CommandUtils;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", "Handles the user warning system", "[user][reason] / [show] / [remove]");
		addAliases("warn");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addChildren(
				new WarningShowCommand(this),
				new WarnRemoveCommand(this));
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(event, 2, failure)) return;

		User author = event.getAuthor();
		Guild guild = event.getGuild();

		new Parser(args.get(0), event).parseAsUser(user ->
		{
			if(user.isBot())
			{
				failure.accept(new CommandResultException("Bots cannot be warned."));
				return;
			}

			if(user.equals(author))
			{
				failure.accept(new CommandHierarchyException(this));
				return;
			}

			CommandUtils.interactionCheck(author, user, event, () ->
			{
				args.remove(0);
				String reason = StringUtils.markdownSanitize(String.join(" ", args));
				if(reason.isBlank())
				{
					failure.accept(new CommandSyntaxException(event));
					return;
				}

				new Warning(guild, user, event.getMonke()).add(reason);
				event.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);

				user.openPrivateChannel()
						.flatMap(privateChannel -> privateChannel.sendMessage(new EmbedBuilder()
								.setTitle("You have been warned in " + guild.getName())
								.addField("Reason", reason, true)
								.setColor(Constants.EMBED_COLOUR)
								.setTimestamp(Instant.now())
								.build())).queue(null, error -> { });
			});
		});
	}
}