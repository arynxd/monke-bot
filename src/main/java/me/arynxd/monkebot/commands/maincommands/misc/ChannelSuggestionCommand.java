package me.arynxd.monkebot.commands.maincommands.misc;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.objects.Emoji;
import me.arynxd.monkebot.objects.cache.GuildSettingsCache;
import me.arynxd.monkebot.objects.command.Command;
import me.arynxd.monkebot.objects.command.CommandEvent;
import me.arynxd.monkebot.objects.command.CommandFlag;
import me.arynxd.monkebot.objects.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings ("unused")
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
		if (CommandChecks.argsEmpty(event, failure)) return;
		if (CommandChecks.argsEmbedCompatible(event, failure)) return;

		User author = event.getAuthor();
		GuildSettingsCache guildConfig = GuildSettingsCache.getCache(event.getGuildIdLong(), event.getMonke());
		MessageChannel suggestionChannel = event.getGuild().getTextChannelById(guildConfig.getChannelSuggestionChannel());

		if (CommandChecks.channelConfigured(suggestionChannel, "Channel suggestion channel", failure)) return;
		if (CommandChecks.canSee(suggestionChannel, event.getSelfMember(), "Suggestion channel", failure)) return;

		suggestionChannel.sendMessage(new EmbedBuilder()
				.setTitle("Channel Suggestion:")
				.setDescription(StringUtils.markdownSanitize(String.join(" ", args)))
				.setColor(Constants.EMBED_COLOUR)
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
