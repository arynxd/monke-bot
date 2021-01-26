package org.igsq.igsqbot.commands.commands.misc;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ChannelSuggestionCommand extends Command
{
	public ChannelSuggestionCommand()
	{
		super("Channel Suggest", "Suggests new channels", "[suggestion]");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("csuggest", "channelsuggest", "channesuggestion");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;
		if(CommandChecks.argsEmbedCompatible(event, failure)) return;

		User author = event.getAuthor();
		GuildConfig guildConfig = new GuildConfig(event);
		MessageChannel suggestionChannel = event.getGuild().getTextChannelById(guildConfig.getChannelSuggestionChannel());

		if(CommandChecks.channelConfigured(suggestionChannel, "Channel suggestion channel", failure)) return;

		suggestionChannel.sendMessage(new EmbedBuilder()
				.setTitle("Channel Suggestion:")
				.setDescription(ArrayUtils.arrayCompile(args, " "))
				.setColor(Constants.IGSQ_PURPLE)
				.setThumbnail(author.getAvatarUrl())
				.setFooter("Suggested by: " + event.getAuthor().getAsTag() + " | ")
				.setTimestamp(Instant.now())
				.build()).queue(
				message ->
				{
					message.addReaction(Emoji.THUMB_UP.getAsReaction()).queue();
					message.addReaction(Emoji.THUMB_DOWN.getAsReaction()).queue();
				});
	}
}
