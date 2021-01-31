package me.arynxd.monkebot.commands.commands.moderation;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.cache.MessageCache;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandCooldownException;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandSyntaxException;
import me.arynxd.monkebot.handlers.CooldownHandler;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.Parser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ClearCommand extends Command
{
	public ClearCommand()
	{
		super("Clear", "Clears messages from the current channel", "[amount {50}]");
		addAliases("clear", "purge");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
		setCooldown(10000L);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		MessageChannel channel = event.getChannel();
		Member member = event.getMember();
		Guild guild = event.getGuild();
		OptionalInt amount = new Parser(args.get(0), event).parseAsUnsignedInt();

		if(amount.isPresent())
		{
			if(amount.getAsInt() > 50)
			{
				failure.accept(new CommandSyntaxException(this));
				return;
			}

			if(CooldownHandler.isOnCooldown(member, this))
			{
				failure.accept(new CommandCooldownException(this));
				return;
			}

			channel.getIterableHistory()
			.takeAsync(amount.getAsInt() + 1)
			.thenAccept(messages ->
			{
				CooldownHandler.addCooldown(member, this);
				channel.purgeMessages(messages);
				event.replySuccess("Deleted " + (messages.size() - 1) + " messages");
				MessageCache cache = MessageCache.getCache(guild);
				messages.stream().filter(cache::isInCache).forEach(cache::remove);
			});
		}
	}
}
