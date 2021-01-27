package me.arynxd.monkebot.commands.commands.misc;

import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.entities.Emoji;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandInputException;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

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
		GuildConfig guildConfig = new GuildConfig(event);

		if(args.isEmpty())
		{
			EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
					.setDescription("My prefix for this server is `" + guildConfig.getPrefix() + "`.")
					.setColor(Constants.EMBED_COLOUR), 30000);
			return;
		}

		if(args.size() > 1 || args.get(0).length() > 5)
		{
			failure.accept(new CommandInputException("Prefix was too long or contained spaces."));
			return;
		}

		if(!event.memberPermissionCheck(Permission.MANAGE_SERVER))
		{
			failure.accept(new CommandException(Emoji.FAILURE.getAsChat() +
					" You do not have the following required permissions: *Manage Server*"));
			return;
		}

		guildConfig.setPrefix(args.get(0));
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
			GuildConfig guildConfig = new GuildConfig(event);
			event.replySuccess("Reset my prefix to `" + Constants.DEFAULT_BOT_PREFIX + "`");
			guildConfig.setPrefix(Constants.DEFAULT_BOT_PREFIX);
		}
	}
}