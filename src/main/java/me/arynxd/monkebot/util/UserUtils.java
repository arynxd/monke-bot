package me.arynxd.monkebot.util;

import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
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

	public static RestAction<Member> getMemberFromUser(User user, Guild guild)
	{
		return guild.retrieveMember(user);
	}
}
