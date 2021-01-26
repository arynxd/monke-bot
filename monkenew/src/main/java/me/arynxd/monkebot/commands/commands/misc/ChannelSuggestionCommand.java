package me.arynxd.monkebot.commands.commands.misc;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.entities.exception.CommandException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.entities.Emoji;
import me.arynxd.monkebot.util.ArrayUtils;
import me.arynxd.monkebot.util.CommandChecks;
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
