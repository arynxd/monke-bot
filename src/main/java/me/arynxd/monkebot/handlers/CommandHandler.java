package me.arynxd.monkebot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.database.GuildConfig;
import me.arynxd.monkebot.util.BlacklistUtils;
import me.arynxd.monkebot.util.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandHandler
{
	public static final String COMMAND_PACKAGE = "me.arynxd.monkebot.commands.commands";

	private final ClassGraph classGraph = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
	private final Map<String, Command> commandMap;
	private final Monke monke;

	public CommandHandler(Monke monke)
	{
		this.monke = monke;
		commandMap = loadCommands();
	}

	private Map<String, Command> loadCommands()
	{
		Map<String, Command> commands = new LinkedHashMap<>();
		try(ScanResult result = classGraph.scan())
		{
			for(ClassInfo cls : result.getAllClasses())
			{
				Constructor<?>[] constructors = cls.loadClass().getDeclaredConstructors();
				if(constructors.length == 0)
				{
					monke.getLogger().warn("No valid constructors found for Command class (" + cls.getSimpleName() + ")!");
					continue;
				}
				if(constructors[0].getParameterCount() > 0)
				{
					continue;
				}
				Object instance = constructors[0].newInstance();
				if(!(instance instanceof Command))
				{
					monke.getLogger().warn("Non Command class (" + cls.getSimpleName() + ") found in commands package!");
					continue;
				}
				Command cmd = (Command) instance;
				commands.put(cmd.getName(), cmd);
				for(String alias : cmd.getAliases()) commands.put(alias, cmd);
			}
		}
		catch(Exception exception)
		{
			monke.getLogger().error("A command exception occurred", exception);
			System.exit(1);
		}

		return Collections.unmodifiableMap(commands);
	}

	public Map<String, Command> getCommandMap()
	{
		return commandMap;
	}

	public void handle(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot() || event.isWebhookMessage())
		{
			return;
		}

		Message referencedMessage = event.getMessage().getReferencedMessage();

		if(referencedMessage != null && referencedMessage.getAuthor().equals(monke.getSelfUser()))
		{
			return;
		}

		if(event.isFromGuild())
		{
			handleGuild(event);
		}
		else
		{
			handleDM(event);
		}
	}

	private void handleDM(MessageReceivedEvent event)
	{
		String prefix;
		String messageContent = event.getMessage().getContentRaw();

		if(isBotMention(event))
		{
			prefix = messageContent.substring(0, messageContent.indexOf(">"));
		}
		else
		{
			prefix = Constants.DEFAULT_BOT_PREFIX;
		}

		if(!messageContent.startsWith(prefix))
		{
			return;
		}

		messageContent = messageContent.substring(prefix.length());

		List<String> args = Arrays.stream(messageContent.split("\\s+")).collect(Collectors.toList());
		if(args.isEmpty())
		{
			return;
		}

		String command = args.get(0);
		findCommand(prefix, command, args, event);
	}

	private void deleteBlacklisted(MessageReceivedEvent event)
	{
		EmbedUtils.sendError(event.getChannel(), "Your message contained a blacklisted phrase.");
		if(event.getGuild().getSelfMember().hasPermission((GuildChannel) event.getChannel(), Permission.MESSAGE_MANAGE))
		{
			event.getMessage().delete().queue();
		}
	}

	private void handleGuild(MessageReceivedEvent event)
	{
		String prefix = new GuildConfig(event.getGuild(), monke).getPrefix();
		String messageContent = event.getMessage().getContentRaw();
		boolean containsBlacklist = BlacklistUtils.isBlacklistedPhrase(event, monke);

		if(isBotMention(event))
		{
			prefix = messageContent.substring(0, messageContent.indexOf(">") + 1);
		}

		if(containsBlacklist)
		{
			deleteBlacklisted(event);
			return;
		}

		messageContent = messageContent.substring(prefix.length());
		List<String> args = Arrays
				.stream(messageContent.split("\\s+"))
				.filter(arg -> !arg.isBlank())
				.collect(Collectors.toList());

		String command = args.get(0);
		
		findCommand(prefix, command.strip(), args, event);
	}

	private boolean isBotMention(MessageReceivedEvent event)
	{
		String content = event.getMessage().getContentRaw();
		long id = event.getJDA().getSelfUser().getIdLong();
		return content.startsWith("<@" + id + ">") || content.startsWith("<@!" + id + ">");
	}

	private void findCommand(String prefix, String command, List<String> args, MessageReceivedEvent event)
	{
		if(command.isBlank() || command.startsWith(prefix))
		{
			return;
		}

		Command cmd = commandMap.get(command);
		boolean containsBlacklist = BlacklistUtils.isBlacklistedPhrase(event, monke);

		if(cmd == null)
		{
			if(containsBlacklist)
			{
				deleteBlacklisted(event);
				return;
			}
			EmbedUtils.sendError(event.getChannel(), "Command `" + command + "` was not found.\n See " + prefix + "help for help.");
			return;
		}

		if(containsBlacklist && cmd.hasFlag(CommandFlag.BLACKLIST_BYPASS))
		{
			deleteBlacklisted(event);
			return;
		}

		args.remove(0);
		CommandEvent commandEvent = new CommandEvent(event, monke, cmd, args);

		if(!cmd.hasChildren())
		{
			cmd.process(commandEvent);
			return;
		}

		if(args.isEmpty())
		{
			cmd.process(commandEvent);
			return;
		}

		cmd.getChildren()
				.stream()
				.filter(child -> child.getName().equalsIgnoreCase(args.get(0)))
				.findFirst()
				.ifPresentOrElse(
						child -> child.process(new CommandEvent(event, monke, child, args.subList(1, args.size()))),
						() -> cmd.process(commandEvent));
	}
}
