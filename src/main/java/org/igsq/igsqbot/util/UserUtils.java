package org.igsq.igsqbot.util;

import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

public class UserUtils
{
	private UserUtils()
	{
		//Overrides the default, public constructor
	}

	public static List<Long> getRoleIds(Member member)
	{
		return member.getRoles().stream().map(Role::getIdLong).collect(Collectors.toList());
	}

	public static boolean basicPermCheck(Member user, TextChannel channel)
	{
		return user.hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS);
	}


	public static RestAction<Member> getMemberFromUser(User user, Guild guild)
	{
		return guild.retrieveMember(user);
	}
}
