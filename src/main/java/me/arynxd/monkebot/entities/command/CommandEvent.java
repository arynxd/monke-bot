package me.arynxd.monkebot.entities.command;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.Emoji;
import me.arynxd.monkebot.entities.bot.ConfigOption;
import me.arynxd.monkebot.entities.cache.GuildSettingsCache;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandEvent
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandEvent.class);
	private final MessageReceivedEvent event;
	private final Monke monke;
	private final Command command;
	private final List<String> args;

	public CommandEvent(@NotNull MessageReceivedEvent event, @NotNull Monke monke, @NotNull Command command, @NotNull List<String> args)
	{
		this.event = event;
		this.monke = monke;
		this.command = command;
		this.args = args;
	}

	public @NotNull List<String> getArgs()
	{
		return args;
	}

	public @NotNull String getPrefix()
	{
		if(!isFromGuild())
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		else
		{
			return GuildSettingsCache.getCache(getGuildIdLong(), monke).getPrefix();
		}
	}

	public @NotNull Member getSelfMember()
	{
		return getGuild().getSelfMember();
	}

	public void addErrorReaction()
	{
		getMessage().addReaction(Emoji.FAILURE.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.FAILURE.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	public void addSuccessReaction()
	{
		getMessage().addReaction(Emoji.SUCCESS.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.SUCCESS.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	public @NotNull Monke getMonke()
	{
		return monke;
	}

	public @NotNull Command getCommand()
	{
		return command;
	}

	public @NotNull MessageChannel getChannel()
	{
		return event.getChannel();
	}

	public @NotNull Message getMessage()
	{
		return event.getMessage();
	}

	public @NotNull Guild getGuild()
	{
		if(event.isFromGuild())
		{
			return event.getGuild();
		}
		throw new IllegalStateException("Cannot get the guild of a private channel.");
	}

	public @NotNull Long getGuildIdLong()
	{
		return getGuild().getIdLong();
	}

	public @NotNull User getAuthor()
	{
		return event.getAuthor();
	}

	public @NotNull JDA getJDA()
	{
		return event.getJDA();
	}

	public @NotNull Boolean isChild()
	{
		return command.getParent() != null;
	}

	public @NotNull ChannelType getChannelType()
	{
		return event.getChannelType();
	}

	public @NotNull MessageReceivedEvent getEvent()
	{
		return event;
	}

	public @NotNull Member getMember()
	{
		return Objects.requireNonNull(event.getMember());
	}

	public void replyError(String errorText)
	{
		addErrorReaction();
		EmbedUtils.sendError(getChannel(), errorText);
	}

	public @NotNull TextChannel getTextChannel()
	{
		if(!isFromGuild())
		{
			throw new IllegalStateException("Event did not occur in a text channel.");
		}
		return event.getTextChannel();
	}

	public void replySuccess(String successText)
	{
		addSuccessReaction();
		EmbedUtils.sendSuccess(getChannel(), successText);
	}

	public @NotNull Boolean isDeveloper()
	{
		return List.of(monke.getConfiguration().getString(ConfigOption.PRIVILEGEDUSERS).split(",")).contains(getAuthor().getId());
	}

	public @NotNull Boolean isFromGuild()
	{
		return event.isFromGuild();
	}

	public @NotNull Boolean memberPermissionCheck(List<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public @NotNull Boolean memberPermissionCheck(Permission... permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public @NotNull Boolean selfPermissionCheck(Permission... permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	public void sendMessage(EmbedBuilder embed)
	{
		addSuccessReaction();
		getChannel().sendMessage(embed.setColor(Constants.EMBED_COLOUR).setTimestamp(Instant.now()).build()).queue();
	}

	public void sendDeletingMessage(EmbedBuilder embed)
	{
		addSuccessReaction();
		EmbedUtils.sendDeletingEmbed(getChannel(), embed.setColor(Constants.EMBED_COLOUR).setTimestamp(Instant.now()));
	}


	public @NotNull Boolean selfPermissionCheck(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}
}
