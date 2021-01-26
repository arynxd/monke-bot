package org.igsq.igsqbot.util;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.database.GuildConfig;

public class CommandUtils
{
	private CommandUtils()
	{
		// Override the default, public, constructor
	}

	public static boolean isValidCommand(String message, long guildId, IGSQBot igsqBot)
	{
		return message.startsWith(new GuildConfig(guildId, igsqBot).getPrefix()) || message.startsWith("<@" + igsqBot.getSelfUser().getId() + ">") || message.startsWith("<@!" + igsqBot.getSelfUser().getId() + ">");
	}

	public static void interactionCheck(User user1, User user2, CommandEvent ctx, Runnable onSuccess)
	{
		List<RestAction<?>> actions = new ArrayList<>();
		actions.add(ctx.getGuild().retrieveMember(user1));
		actions.add(ctx.getGuild().retrieveMember(user2));
		RestAction.allOf(actions).queue(results ->
				{
					Member member1 = (Member) results.get(0);
					Member member2 = (Member) results.get(1);

					if(member1.canInteract(member2))
					{
						onSuccess.run();
					}
					else
					{
						ctx.replyError("A hierarchy issue occurred when trying to execute command `" + ctx.getCommand().getName() + "`");
					}
				}
		);
	}

	public static int getLevelUp(MessageReceivedEvent event, IGSQBot igsqBot)
	{
		Guild guild = event.getGuild();
		Member member = event.getMember();
		long levelBot = new GuildConfig(guild.getIdLong(), igsqBot).getLevelUpBot();

		if(member == null || levelBot != member.getIdLong())
		{
			return -1;
		}

		if(event.getMessage().getMentionedMembers().isEmpty())
		{
			return -1;
		}

		String content = event.getMessage().getContentRaw();
		Member target = event.getMessage().getMentionedMembers().get(0);

		return extractLevel(content, target);
	}

	private static int extractLevel(String content, Member target)
	{
		String mention = target.getAsMention();

		content = content.replace(mention, "");

		for(String letter : content.split(""))
		{
			try
			{
				return Integer.parseInt(letter);
			}
			catch(Exception ignored) { }
		}
		return -1;
	}
}
