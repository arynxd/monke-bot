package org.igsq.igsqbot.entities.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.exception.*;
import org.igsq.igsqbot.util.EmbedUtils;

/**
 * A class representing a command for use in the {@link org.igsq.igsqbot.handlers.CommandHandler CommandHandler}.
 *
 * @see #process(CommandEvent)
 * @see #run(java.util.List, CommandEvent, java.util.function.Consumer)
 */
public abstract class Command
{
	private final Command parent;
	private final String name;
	private final String description;
	private final String syntax;
	private final List<Command> children;
	private final List<String> aliases;
	/**
	 * The {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member author} to execute this {@link Command command}.
	 */
	private final List<Permission> memberRequiredPermissions;
	/**
	 * The {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self member} to execute this {@link Command command}.
	 */
	private final List<Permission> selfRequiredPermissions;
	private final List<CommandFlag> flags;
	private boolean isDisabled;
	private long cooldown;

	/**
	 * Constructs a new Command.
	 *
	 * @param parent      The parent of this Command.
	 * @param name        The name of this command. This will be the one, and only, invoking {@link java.lang.String String} for a subcommand.
	 * @param description The description of this command.
	 * @param syntax      The syntax of this command.
	 */
	protected Command(@Nullable Command parent, @Nonnull String name, @Nonnull String description, @Nonnull String syntax)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isDisabled = false;
		this.memberRequiredPermissions = new ArrayList<>();
		this.selfRequiredPermissions = new ArrayList<>();
		this.cooldown = 0;
		this.flags = new ArrayList<>();
	}

	/**
	 * Constructs a new Command.
	 *
	 * @param name        The name of this command. This will be the one, and only, invoking {@link java.lang.String String} for a subcommand.
	 * @param description The description of this command.
	 * @param syntax      The syntax of this command.
	 */
	protected Command(@Nonnull String name, @Nonnull String description, @Nonnull String syntax)
	{
		this.parent = null;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isDisabled = false;
		this.memberRequiredPermissions = new ArrayList<>();
		this.selfRequiredPermissions = new ArrayList<>();
		this.cooldown = 0;
		this.flags = new ArrayList<>();
	}

	/**
	 * Processes this {@link org.igsq.igsqbot.entities.command.Command command} for execution.
	 * <p>
	 * This will consider the {@link org.igsq.igsqbot.entities.command.CommandFlag flags}, {@link #selfRequiredPermissions} and {@link #memberRequiredPermissions} of this {@link org.igsq.igsqbot.entities.command.Command command}
	 * <p>
	 * This will only {@link #run(java.util.List, CommandEvent, java.util.function.Consumer) run} the command if all checks pass.
	 * @param event The command event to process with.
	 */
	public void process(CommandEvent event)
	{
		if(isDisabled() || hasFlag(CommandFlag.DISABLED))
		{
			EmbedUtils.sendDisabledError(event);
		}
		else if(hasFlag(CommandFlag.GUILD_ONLY) && !event.isFromGuild())
		{
			event.replyError("This command must be executed in a server.");
		}
		else if(!getMemberRequiredPermissions().isEmpty() && !event.memberPermissionCheck(getMemberRequiredPermissions()))
		{
			EmbedUtils.sendMemberPermissionError(event);
		}
		else if(!getSelfRequiredPermissions().isEmpty() && !event.selfPermissionCheck(getSelfRequiredPermissions()))
		{
			EmbedUtils.sendSelfPermissionError(event);
		}
		else if(hasFlag(CommandFlag.DEVELOPER_ONLY) && !event.isDeveloper())
		{
			event.replyError("This command is for developers only.");
		}
		else
		{
			if(hasFlag(CommandFlag.AUTO_DELETE_MESSAGE) && event.selfPermissionCheck(Permission.MESSAGE_MANAGE))
			{
				event.getMessage().delete().queue();
			}

			run(event.getArgs(), event, exception ->
			{
				if(exception instanceof CommandCooldownException)
				{
					event.replyError(event.getAuthor().getAsMention() + " is on cooldown from command `" + getName() + "`");
				}
				else if(exception instanceof CommandResultException)
				{
					event.replyError("An error occurred. " + exception.getText());
				}
				else if(exception instanceof CommandInputException)
				{
					event.replyError("Your input was invalid. " + exception.getText());
				}
				else if(exception instanceof CommandSyntaxException)
				{
					EmbedUtils.sendSyntaxError(event);
				}
				else if(exception instanceof CommandHierarchyException)
				{
					event.replyError("A hierarchy error occurred when trying to run command `" + getName() + "`");
				}
				else if(exception instanceof CommandUserPermissionException)
				{
					EmbedUtils.sendMemberPermissionError(event);
				}
				else if(exception instanceof MissingConfigurationException)
				{
					event.replyError("`" + exception.getText() + "` is not setup.");
				}
				else
				{
					event.replyError("An unhandled error occurred " + exception.getText());
				}
			});
		}
	}

	/**
	 * @return Whether this {@link org.igsq.igsqbot.entities.command.Command command} has any children attached to it.
	 * @see #getChildren()
	 * @see #addChildren(Command...)
	 */
	@Nonnull
	public Boolean hasChildren()
	{
		return !getChildren().isEmpty();
	}

	/**
	 * @return Whether this {@link org.igsq.igsqbot.entities.command.Command command} has a parent attached to it.
	 */
	@Nonnull
	public Boolean hasParent()
	{
		return parent != null;
	}

	/**
	 * Submit this command for execution. This implies all {@link #process(CommandEvent) proccessing} steps have completed successfully.
	 *
	 * @param args    The arguments supplied to the {@link org.igsq.igsqbot.entities.command.Command command}.
	 * @param event   The {@link org.igsq.igsqbot.entities.command.CommandEvent event} for this {@link org.igsq.igsqbot.entities.command.Command command}
	 * @param failure The {@link java.util.function.Consumer callback} to use in the case of execution failure.
	 */
	public abstract void run(@Nonnull List<String> args, @Nonnull CommandEvent event, @Nonnull Consumer<CommandException> failure);

	/**
	 * @return The cooldown for this {@link org.igsq.igsqbot.entities.command.Command command}.
	 * <p>
	 * Will return 0 if none is set.
	 * @see #setCooldown(Long)
	 */
	@Nonnull
	public Long getCooldown()
	{
		return cooldown;
	}

	/**
	 * Sets the cooldown for this {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param millis The new cooldown.
	 * @see #getCooldown()
	 */
	public void setCooldown(@Nonnull Long millis)
	{
		this.cooldown = millis;
	}

	/**
	 * @return Whether this {@link org.igsq.igsqbot.entities.command.Command command} is disabled.
	 * @see #setDisabled(Boolean)
	 */
	public boolean isDisabled()
	{
		return isDisabled;
	}

	/**
	 * Sets the disabled state of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param state The new state.
	 */
	public void setDisabled(@Nonnull Boolean state)
	{
		isDisabled = state;
	}

	/**
	 * @return The parent to this {@link org.igsq.igsqbot.entities.command.Command command} or <code>null</code> if it does not exist.
	 * @see #hasParent()
	 */
	@Nullable
	public Command getParent()
	{
		return parent;
	}

	/**
	 * @return The name of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 */
	@Nonnull
	public String getName()
	{
		return name;
	}

	/**
	 * @return The description of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 */
	@Nonnull
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return The syntax of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 */
	@Nonnull
	public String getSyntax()
	{
		return syntax;
	}

	/**
	 * @return The children of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 * @see #addChildren(Command...)
	 * @see #hasChildren()
	 */
	@Nonnull
	public List<Command> getChildren()
	{
		return children;
	}

	/**
	 * @return The aliases of this {@link org.igsq.igsqbot.entities.command.Command command}.
	 * @see #addAliases(String...)
	 */
	@Nonnull
	public List<String> getAliases()
	{
		return aliases;
	}

	/**
	 * Adds children to this {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param children The children to add.
	 * @see #getChildren()
	 * @see #hasChildren()
	 */
	public void addChildren(@Nonnull Command... children)
	{
		this.children.addAll(List.of(children));
	}

	/**
	 * Adds aliases to this {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param aliases The aliases to add.
	 * @see #getAliases()
	 */
	public void addAliases(@Nonnull String... aliases)
	{
		this.aliases.addAll(List.of(aliases));
	}

	/**
	 * Gets the {@link net.dv8tion.jda.api.Permission permissions} required by the {@link org.igsq.igsqbot.entities.command.Command command} {@link net.dv8tion.jda.api.entities.Member author} to execute.
	 *
	 * @return The permissions.
	 * @see #addMemberPermissions(net.dv8tion.jda.api.Permission...)
	 */
	@Nonnull
	public List<Permission> getMemberRequiredPermissions()
	{
		return memberRequiredPermissions;
	}

	/**
	 * Adds {@link net.dv8tion.jda.api.Permission permissions} required by the {@link org.igsq.igsqbot.entities.command.Command command} {@link net.dv8tion.jda.api.entities.Member author} to execute.
	 *
	 * @param permissions The permissions to add.
	 * @see #getMemberRequiredPermissions()
	 */
	public void addMemberPermissions(@Nonnull Permission... permissions)
	{
		this.memberRequiredPermissions.addAll(List.of(permissions));
	}

	/**
	 * Adds {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self user} to execute the {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param permissions The permissions to add.
	 * @see #getSelfRequiredPermissions()
	 */
	public void addSelfPermissions(@Nonnull Permission... permissions)
	{
		this.selfRequiredPermissions.addAll(List.of(permissions));
	}

	/**
	 * Gets the {@link net.dv8tion.jda.api.Permission permissions} required by the {@link net.dv8tion.jda.api.entities.Member self user} to execute the {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @see #getSelfRequiredPermissions()
	 */
	@Nonnull
	public List<Permission> getSelfRequiredPermissions()
	{
		return selfRequiredPermissions;
	}

	/**
	 * @return The {@link org.igsq.igsqbot.entities.command.CommandFlag flags} for this {@link org.igsq.igsqbot.entities.command.Command command}.
	 * @see #addFlags(CommandFlag...)
	 * @see #hasFlag(CommandFlag)
	 */
	@Nonnull
	public List<CommandFlag> getFlags()
	{
		return flags;
	}

	/**
	 * Adds {@link org.igsq.igsqbot.entities.command.CommandFlag flags} to this {@link org.igsq.igsqbot.entities.command.Command command}.
	 *
	 * @param flags The flags to add.
	 * @see #getFlags()
	 * @see #hasFlag(CommandFlag)
	 * @see CommandFlag#getDefaultValue()
	 */
	public void addFlags(@Nonnull CommandFlag... flags)
	{
		this.flags.addAll(List.of(flags));
	}

	/**
	 * @param flag The flag to check for.
	 * @return Whether this {@link org.igsq.igsqbot.entities.command.Command command} has the flag.
	 * @see #getFlags()
	 * @see #addFlags(CommandFlag...)
	 */
	public boolean hasFlag(@Nonnull CommandFlag flag)
	{
		return this.flags.contains(flag);
	}
}
