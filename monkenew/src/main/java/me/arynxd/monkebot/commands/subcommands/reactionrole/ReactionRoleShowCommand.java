package me.arynxd.monkebot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.ReactionRole;
import me.arynxd.monkebot.entities.exception.CommandException;
import net.dv8tion.jda.api.EmbedBuilder;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ReactionRoleShowCommand extends Command
{
	public ReactionRoleShowCommand(Command parent)
	{
		super(parent, "show", "Shows reaction roles.", "[messageId]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		OptionalLong messageId = new Parser(args.get(0), event).parseAsUnsignedLong();

		if(messageId.isPresent())
		{
			List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(messageId.getAsLong(), event.getMonke());
			StringBuilder text = new StringBuilder();

			for(ReactionRole reactionRole : reactionRoles)
			{
				text
						.append(StringUtils.getEmoteAsMention(reactionRole.getEmote()))
						.append("  ")
						.append(StringUtils.getRoleAsMention(reactionRole.getRoleId()))
						.append("  ")
						.append("\n");
			}

			event.sendMessage(new EmbedBuilder()
					.setTitle("Reaction roles for message " + messageId.getAsLong())
					.setDescription(text.length() == 0 ? "No reaction roles found" : text.toString()));
		}

	}
}
