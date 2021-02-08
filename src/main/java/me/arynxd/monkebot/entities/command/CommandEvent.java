package me.arynxd.monkebot.entities.command;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandEvent
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandEvent.class);
	private final MessageReceivedEvent event;
	private final Monke monke;
	private final Command command;
	private final List<String> args;

	public CommandEvent(@Nonnull MessageReceivedEvent event, @Nonnull Monke monke, @Nonnull Command command, @Nonnull List<String> args)
	{
		this.event = event;
		this.monke = monke;
		this.command = command;
		this.args = args;
	}

	public @Nonnull List<String> getArgs()
	{
		return args;
	}

	public @Nonnull String getPrefix()
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

	public @Nonnull
	Member getSelfMember()
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

	public @Nonnull
	Monke getMonke()
	{
		return monke;
	}

	public @Nonnull
	Command getCommand()
	{
		return command;
	}

	public @Nonnull
	MessageChannel getChannel()
	{
		return event.getChannel();
	}

	public @Nonnull
	Message getMessage()
	{
		return event.getMessage();
	}

	public @Nonnull
	Guild getGuild()
	{
		if(event.isFromGuild())
		{
			return event.getGuild();
		}
		throw new IllegalStateException("Cannot get the guild of a private channel.");
	}

	public @Nonnull
	Long getGuildIdLong()
	{
		return getGuild().getIdLong();
	}

	public @Nonnull
	User getAuthor()
	{
		return event.getAuthor();
	}

	public @Nonnull
	JDA getJDA()
	{
		return event.getJDA();
	}

	public @Nonnull
	Boolean isChild()
	{
		return command.getParent() != null;
	}

	public @Nonnull
	ChannelType getChannelType()
	{
		return event.getChannelType();
	}

	public @Nonnull
	MessageReceivedEvent getEvent()
	{
		return event;
	}

	public @Nonnull
	Member getMember()
	{
		return Objects.requireNonNull(event.getMember());
	}

	public void replyError(String errorText)
	{
		addErrorReaction();
		EmbedUtils.sendError(getChannel(), errorText);
	}

	public @Nonnull
	TextChannel getTextChannel()
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

	public @Nonnull
	Boolean isDeveloper()
	{
		return List.of(monke.getConfiguration().getString(ConfigOption.PRIVILEGEDUSERS).split(",")).contains(getAuthor().getId());
	}

	public @Nonnull
	Boolean isFromGuild()
	{
		return event.isFromGuild();
	}

	public @Nonnull
	Boolean memberPermissionCheck(List<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public @Nonnull
	Boolean memberPermissionCheck(Permission... permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public @Nonnull
	Boolean selfPermissionCheck(Permission... permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	public void sendMessage(EmbedBuilder embed)
	{
		addSuccessReaction();
		getChannel().sendMessage(embed.setColor(Constants.EMBED_COLOUR).setTimestamp(Instant.now()).build()).queue();
	}

	public @Nonnull Boolean selfPermissionCheck(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}
}
