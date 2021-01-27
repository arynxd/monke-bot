package me.arynxd.monkebot.entities.command;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.database.GuildConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.entities.Emoji;
import me.arynxd.monkebot.entities.bot.ConfigOption;
import me.arynxd.monkebot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class representing an event for a {@link Command command}.
 */
public class CommandEvent
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandEvent.class);
	private final MessageReceivedEvent event;
	private final Monke monke;
	private final Command command;
	private final List<String> args;

	/**
	 * Constructs a new {@link CommandEvent event}.
	 *
	 * @param event   The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
	 * @param monke The {@link me.arynxd.monkebot.Monke monke} instance to use.
	 * @param command The {@link Command command} to use.
	 * @param args    The {@link java.util.List<String> args} to use.
	 */
	public CommandEvent(MessageReceivedEvent event, Monke monke, Command command, List<String> args)
	{
		this.event = event;
		this.monke = monke;
		this.command = command;
		this.args = args;
	}

	/**
	 * @return The args.
	 */
	@Nonnull
	public List<String> getArgs()
	{
		return args;
	}

	/**
	 * @return The prefix. This varies per {@link net.dv8tion.jda.api.entities.Guild guild}.
	 */
	@Nonnull
	public String getPrefix()
	{
		if(!isFromGuild())
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		else
		{
			return new GuildConfig(this).getPrefix();
		}
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.Member self member}
	 */
	@Nonnull
	public Member getSelfMember()
	{
		return getGuild().getSelfMember();
	}

	/**
	 * Adds the {@link Emoji#FAILURE} reaction to the {@link net.dv8tion.jda.api.entities.Message message} sent by the author.
	 */
	public void addErrorReaction()
	{
		getMessage().addReaction(Emoji.FAILURE.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.FAILURE.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	/**
	 * Adds the {@link Emoji#SUCCESS} reaction to the {@link net.dv8tion.jda.api.entities.Message message} sent by the author.
	 */
	public void addSuccessReaction()
	{
		getMessage().addReaction(Emoji.SUCCESS.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.SUCCESS.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	/**
	 * @return The {@link me.arynxd.monkebot.Monke monke} instance.
	 */
	@Nonnull
	public Monke getMonke()
	{
		return monke;
	}

	/**
	 * @return The {@link Command command}.
	 */
	@Nonnull
	public Command getCommand()
	{
		return command;
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.MessageChannel channel}.
	 */
	@Nonnull
	public MessageChannel getChannel()
	{
		return event.getChannel();
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.Message message}.
	 */
	@Nonnull
	public Message getMessage()
	{
		return event.getMessage();
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.Guild guild} if the command was executed in a server.
	 * @throws java.lang.IllegalStateException If the command was not executed in a server.
	 */
	@Nonnull
	public Guild getGuild()
	{
		if(event.isFromGuild())
		{
			return event.getGuild();
		}
		throw new IllegalStateException("Cannot get the guild of a private channel.");
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.Guild#getIdLong() guildId}
	 */
	@Nonnull
	public Long getGuildIdLong()
	{
		return getGuild().getIdLong();
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.User author}.
	 */
	@Nonnull
	public User getAuthor()
	{
		return event.getAuthor();
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.JDA jda instance}.
	 */
	@Nonnull
	public JDA getJDA()
	{
		return event.getJDA();
	}

	/**
	 * @return Whether the attached {@link Command command} is a child.
	 */
	@Nonnull
	public Boolean isChild()
	{
		return command.getParent() != null;
	}

	/**
	 * @return The {@link net.dv8tion.jda.api.entities.ChannelType channel type}.
	 */
	@Nonnull
	public ChannelType getChannelType()
	{
		return event.getChannelType();
	}

	/**
	 * @return The raw {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event}.
	 */
	@Nonnull
	public MessageReceivedEvent getEvent()
	{
		return event;
	}

	/**
	 * The {@link net.dv8tion.jda.api.entities.Member member} for this {@link CommandEvent event}
	 * <p>
	 * If a command requires the {@link net.dv8tion.jda.api.entities.Member member}, it should have {@link CommandFlag#GUILD_ONLY} enabled.
	 *
	 * @return the member for this context
	 * @throws java.lang.NullPointerException if the member is null.
	 * @see Command#addFlags(CommandFlag...)
	 */
	@Nonnull
	public Member getMember()
	{
		return Objects.requireNonNull(event.getMember());
	}

	/**
	 * Replies to the user using the specified error message.
	 *
	 * @param errorText The message to use.
	 */
	public void replyError(String errorText)
	{
		addErrorReaction();
		EmbedUtils.sendError(getChannel(), errorText);
	}

	/**
	 * Replies to the user using the specified success message.
	 *
	 * @param successText The message to use.
	 */
	public void replySuccess(String successText)
	{
		addSuccessReaction();
		EmbedUtils.sendSuccess(getChannel(), successText);
	}

	/**
	 * @return If the {@link #getAuthor()} is a developer.
	 */
	@Nonnull
	public Boolean isDeveloper()
	{
		return List.of(monke.getConfig().getString(ConfigOption.PRIVILEGEDUSERS).split(",")).contains(getAuthor().getId());
	}

	/**
	 * @return Whether this {@link #getEvent()} is from a {@link net.dv8tion.jda.api.entities.Guild guild}.
	 */
	@Nonnull
	public Boolean isFromGuild()
	{
		return event.isFromGuild();
	}

	/**
	 * Checks whether the author has all the needed permissions for execution.
	 *
	 * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
	 * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getMemberRequiredPermissions() permissions}.
	 */
	@Nonnull
	public Boolean memberPermissionCheck(List<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	/**
	 * Checks whether the author has all the needed permissions for execution.
	 *
	 * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
	 * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getMemberRequiredPermissions() permissions}.
	 */
	@Nonnull
	public Boolean memberPermissionCheck(Permission... permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	/**
	 * Checks whether the {@link net.dv8tion.jda.api.entities.Member self member} has all the needed permissions for execution.
	 *
	 * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
	 * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getSelfRequiredPermissions()} () permissions}.
	 */
	@Nonnull
	public Boolean selfPermissionCheck(Permission... permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	/**
	 * Sends an embed to the user, adding the {@link Constants#EMBED_COLOUR default} {@link java.awt.Color colour} and footer.
	 * @param embed The {@link net.dv8tion.jda.api.EmbedBuilder embed} to send.
	 */
	public void sendMessage(EmbedBuilder embed)
	{
		addSuccessReaction();
		getChannel().sendMessage(embed.setColor(Constants.EMBED_COLOUR).setTimestamp(Instant.now()).build()).queue();
	}

	/**
	 * Checks whether the {@link net.dv8tion.jda.api.entities.Member self member} has all the needed permissions for execution.
	 *
	 * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
	 * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getSelfRequiredPermissions()} () permissions}.
	 */
	@Nonnull
	public Boolean selfPermissionCheck(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}
}