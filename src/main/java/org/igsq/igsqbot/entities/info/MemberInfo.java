package org.igsq.igsqbot.entities.info;


import java.time.OffsetDateTime;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class MemberInfo
{
	private final Member member;

	public MemberInfo(Member member)
	{
		this.member = member;
	}

	public OffsetDateTime getTimeJoined()
	{
		return member.getTimeJoined();
	}

	public OffsetDateTime getTimeCreated()
	{
		return member.getTimeCreated();
	}

	public OffsetDateTime getBoostingSince()
	{
		return member.getTimeBoosted();
	}

	public String getAvatarURL()
	{
		return member.getUser().getEffectiveAvatarUrl();
	}

	public String getAsTag()
	{
		return member.getUser().getAsTag();
	}

	public List<Role> getCondensedRoles()
	{
		int size = member.getRoles().size();

		if(size < 5)
		{
			return member.getRoles();
		}
		else
		{
			return member.getRoles().subList(0, 5);
		}
	}

	public boolean isBoosting()
	{
		return member.getTimeBoosted() != null;
	}

	public String getUsername()
	{
		return member.getUser().getName();
	}

	public String getNickname()
	{
		return member.getEffectiveName();
	}
}
