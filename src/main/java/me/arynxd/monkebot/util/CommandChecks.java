package me.arynxd.monkebot.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.*;
import me.arynxd.monkebot.entities.json.RedditPost;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

public class CommandChecks
{
	private CommandChecks()
	{
		//Overrides the default, public, constructor
	}

	public static boolean sharesVoice(CommandEvent event, Consumer<CommandException> callback)
	{
		GuildVoiceState state = event.getMember().getVoiceState();
		GuildVoiceState selfState = event.getSelfMember().getVoiceState();

		if(state == null || selfState == null)
		{
			callback.accept(new CommandResultException("Something went wrong when finding your VC."));
			return true;
		}

		else if(state.getChannel() == null)
		{
			callback.accept(new CommandResultException("You are not in a voice channel."));
		}
		else if(selfState.inVoiceChannel() && !state.getChannel().getMembers().contains(event.getSelfMember()))
		{
			callback.accept(new CommandResultException("You are not in a voice channel with me."));
			return true;
		}
		else if(!selfState.inVoiceChannel() && !event.getSelfMember().hasPermission(state.getChannel(), Permission.VIEW_CHANNEL, Permission.VOICE_SPEAK))
		{
			callback.accept(new CommandException("I cannot join / speak in your channel."));
			return true;
		}
		return false;
	}

	public static boolean canSee(MessageChannel channel, Member selfMember, String name, Consumer<CommandException> callback)
	{
		if(!selfMember.hasPermission((GuildChannel) channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
		{
			callback.accept(new CommandResultException("I cannot access " + name));
			return true;
		}
		return false;
	}

	public static boolean canPost(CommandEvent event, RedditPost post, Consumer<CommandException> callback)
	{
		if(event.isFromGuild() && post.isNSFW() && !event.getTextChannel().isNSFW())
		{
			callback.accept(new CommandResultException("The selected post was marked as NSFW and cannot be shown here, please try again."));
			return true;
		}
		return false;
	}


	public static boolean channelConfigured(MessageChannel channel, String name, Consumer<CommandException> callback)
	{
		if(channel == null)
		{
			callback.accept(new MissingConfigurationException(name));
			return true;
		}
		return false;
	}

	public static boolean roleConfigured(Role role, String name, Consumer<CommandException> callback)
	{
		if(role == null)
		{
			callback.accept(new MissingConfigurationException(name));
			return true;
		}
		return false;
	}

	public static boolean userConfigured(User user, String name, Consumer<CommandException> callback)
	{
		if(user == null)
		{
			callback.accept(new MissingConfigurationException(name));
			return true;
		}
		return false;
	}

	public static boolean isURL(String url, CommandEvent ctx, Consumer<CommandException> callback)
	{
		try
		{
			URL obj = new URL(url);
			obj.toURI();
			return false;
		}
		catch(Exception exception)
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
	}

	public static boolean argsEmpty(CommandEvent ctx, Consumer<CommandException> callback)
	{
		if(ctx.getArgs().isEmpty())
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
		return false;
	}

	public static boolean argsSizeExceeds(CommandEvent ctx, int size, Consumer<CommandException> callback)
	{
		if(ctx.getArgs().size() > size)
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
		return false;
	}

	public static boolean argsSizeSubceeds(CommandEvent ctx, int size, Consumer<CommandException> callback)
	{
		if(ctx.getArgs().size() < size)
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
		return false;
	}

	public static boolean argsSizeSubceeds(List<String> args, CommandEvent ctx, int size, Consumer<CommandException> callback)
	{
		if(args.size() < size)
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
		return false;
	}

	public static boolean argsSizeMatches(CommandEvent ctx, int size, Consumer<CommandException> callback)
	{
		if(ctx.getArgs().size() != size)
		{
			callback.accept(new CommandSyntaxException(ctx));
			return true;
		}
		return false;
	}

	public static boolean argsEmbedCompatible(CommandEvent ctx, Consumer<CommandException> callback)
	{
		List<Character> chars = new ArrayList<>();
		ctx.getArgs().stream().map(arg -> arg.split("")).forEach(
				words ->
				{
					for(String word : words)
					{
						for(char character : word.toCharArray())
						{
							chars.add(character);
						}
					}
				});
		if(chars.size() > EmbedUtils.CHARACTER_LIMIT)
		{
			callback.accept(new CommandInputException("Input too large."));
			return true;
		}
		return false;
	}
}
