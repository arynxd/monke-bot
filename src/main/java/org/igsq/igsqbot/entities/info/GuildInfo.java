package org.igsq.igsqbot.entities.info;

import java.time.OffsetDateTime;
import java.util.Locale;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;

public class GuildInfo
{
	private final Guild guild;

	public GuildInfo(Guild guild)
	{
		this.guild = guild;
	}

	public boolean isPartner()
	{
		return guild.getFeatures().contains("PARTNERED");
	}

	public boolean isPublic()
	{
		return guild.getFeatures().contains("PUBLIC");
	}

	public boolean isVerified()
	{
		return guild.getFeatures().contains("VERIFIED");
	}

	public String getDescription()
	{
		return guild.getDescription();
	}

	public Locale getLocale()
	{
		return guild.getLocale();
	}

	public String getBannerURL()
	{
		return guild.getBannerUrl();
	}

	public String getIconURL()
	{
		return guild.getIconUrl();
	}

	public int getBoosts()
	{
		return guild.getBoostCount();
	}

	public String getName()
	{
		return guild.getName();
	}

	public long getId()
	{
		return guild.getIdLong();
	}

	public OffsetDateTime getTimeCreated()
	{
		return guild.getTimeCreated();
	}

	public Region getVoiceRegion()
	{
		return guild.getRegion();
	}

	public int getMaxMemberCount()
	{
		return guild.getMaxMembers();
	}

	public int getMemberCount()
	{
		return guild.getMemberCount();
	}
}
