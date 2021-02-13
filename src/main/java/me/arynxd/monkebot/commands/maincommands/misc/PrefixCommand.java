package me.arynxd.monkebot.commands.maincommands.misc;

import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.objects.exception.CommandInputException;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PrefixCommand extends Command
{
	public PrefixCommand()
	{
		super("Prefix", "Gets and sets the prefix for this server.", "<newPrefix {5}> / <reset> / <none>");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("prefix");
		addChildren(new PrefixResetCommand(this));
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		MessageChannel channel = event.getChannel();
		GuildSettingsCache config = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());

		if(args.isEmpty())
		{
			EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
					.setDescription("My prefix for this server is `" + config.getPrefix() + "`.")
					.setColor(Constants.EMBED_COLOUR), 30000);
			return;
		}

		if(args.size() > 1 || args.get(0).length() > 5)
		{
			failure.accept(new CommandInputException("Prefix was too long or contained spaces."));
			return;
		}

		if(event.isDeveloper())
		{
			config.setPrefix(args.get(0));
			event.replySuccess("My new prefix is `" + args.get(0) + "`");
			return;
		}

		if(!event.memberPermissionCheck(Permission.MANAGE_SERVER))
		{
			failure.accept(new CommandException("You do not have the following required permissions: *Manage Server*"));
			return;
		}

		config.setPrefix(args.get(0));
		event.replySuccess("My new prefix is `" + args.get(0) + "`");
	}

	public static class PrefixResetCommand extends Command
	{
		public PrefixResetCommand(Command parent)
		{
			super(parent, "reset", "Resets the prefix", "[none]");
			addFlags(CommandFlag.GUILD_ONLY);
			addMemberPermissions(Permission.MANAGE_SERVER);
		}

		@Override
		public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
		{
			GuildSettingsCache config = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());
			event.replySuccess("Reset my prefix to `" + Constants.DEFAULT_BOT_PREFIX + "`");
			config.setPrefix(Constants.DEFAULT_BOT_PREFIX);
		}
	}
}