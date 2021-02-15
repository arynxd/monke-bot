package me.arynxd.monkebot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.database.ReactionRole;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.objects.exception.CommandResultException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

public class ReactionRoleRemoveCommand extends Command
{
	public ReactionRoleRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes reaction roles.", "[messageId][channel][role][emote]");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MANAGE_SERVER);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if (CommandChecks.argsSizeSubceeds(event, 4, failure)) return;

		OptionalLong messageId = new Parser(args.get(0), event).parseAsUnsignedLong();
		String emote = event.getMessage().getEmotes().isEmpty() ? args.get(3) : event.getMessage().getEmotes().get(0).getId();
		new Parser(args.get(2), event).parseAsRole(role ->
		{
			if (messageId.isPresent())
			{
				new Parser(args.get(1), event).parseAsTextChannel(
						channel -> channel.retrieveMessageById(messageId.getAsLong()).queue(
								message ->
								{
									ReactionRole reactionRole = new ReactionRole(messageId.getAsLong(), role.getIdLong(), event.getGuild().getIdLong(), emote, event.getMonke());

									if (!reactionRole.isPresent())
									{
										failure.accept(new CommandResultException("That reaction role does not exist"));
										return;
									}

									reactionRole.remove();
									event.replySuccess("Removed reaction role for role " + StringUtils.getRoleAsMention(role.getIdLong()));
									message.clearReactions(emote).queue();
								},
								error -> failure.accept(new CommandInputException("Message " + messageId.getAsLong() + " does not exist"))));
			}
		});
	}
}
