package me.arynxd.monkebot.entities.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.arynxd.monkebot.entities.exception.*;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.Permission;

public abstract class Command
{
	private final Command parent;
	private final String name;
	private final String description;
	private final String syntax;
	private final List<Command> children;
	private final List<String> aliases;

	private final List<Permission> memberRequiredPermissions;

	private final List<Permission> selfRequiredPermissions;
	private final List<CommandFlag> flags;
	private boolean isDisabled;
	private long cooldown;

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

	public void process(@Nonnull CommandEvent event)
	{
		if(hasFlag(CommandFlag.GUILD_ONLY) && !event.isFromGuild())
		{
			event.replyError("This command must be executed in a server.");
		}
		else if(event.isDeveloper())
		{
			execute(event);
		}
		else if(isDisabled() || hasFlag(CommandFlag.DISABLED))
		{
			EmbedUtils.sendDisabledError(event);
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
			execute(event);
		}
	}

	private void execute(@Nonnull CommandEvent event)
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
				event.replyError("Something went wrong. " + exception.getText());
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
				event.replyError(exception.getText());
			}
		});
	}

	public @Nonnull
	Boolean hasChildren()
	{
		return !getChildren().isEmpty();
	}

	public @Nonnull
	Boolean hasParent()
	{
		return parent != null;
	}

	public abstract void run(@Nonnull List<String> args, @Nonnull CommandEvent event, @Nonnull Consumer<CommandException> failure);

	public @Nonnull
	Long getCooldown()
	{
		return cooldown;
	}

	public void setCooldown(@Nonnull Long millis)
	{
		this.cooldown = millis;
	}

	public boolean isDisabled()
	{
		return isDisabled;
	}

	public void setDisabled(@Nonnull Boolean newState)
	{
		isDisabled = newState;
	}

	public @Nullable
	Command getParent()
	{
		return parent;
	}

	public @Nonnull
	String getName()
	{
		return name;
	}

	public @Nonnull
	String getDescription()
	{
		return description;
	}

	public @Nonnull
	String getSyntax()
	{
		return syntax;
	}

	public @Nonnull
	List<Command> getChildren()
	{
		return children;
	}

	public @Nonnull
	List<String> getAliases()
	{
		return aliases;
	}

	public void addChildren(@Nonnull Command... children)
	{
		this.children.addAll(List.of(children));
	}

	public void addAliases(@Nonnull String... aliases)
	{
		this.aliases.addAll(List.of(aliases));
	}

	public @Nonnull
	List<Permission> getMemberRequiredPermissions()
	{
		return memberRequiredPermissions;
	}

	public void addMemberPermissions(@Nonnull Permission... permissions)
	{
		this.memberRequiredPermissions.addAll(List.of(permissions));
	}

	public void addSelfPermissions(@Nonnull Permission... permissions)
	{
		this.selfRequiredPermissions.addAll(List.of(permissions));
	}

	@Nonnull
	public List<Permission> getSelfRequiredPermissions()
	{
		return selfRequiredPermissions;
	}

	@Nonnull
	public List<CommandFlag> getFlags()
	{
		return flags;
	}

	public void addFlags(@Nonnull CommandFlag... flags)
	{
		this.flags.addAll(List.of(flags));
	}

	public boolean hasFlag(@Nonnull CommandFlag flag)
	{
		return this.flags.contains(flag);
	}
}
