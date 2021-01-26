package org.igsq.igsqbot.minecraft;

public class MinecraftUser
{
	private String id;
	private String username;
	private String nickname;
	private String role;
	private int founder;
	private int birthday;
	private int nitroboost;
	private int supporter;
	private int developer;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public int getFounder()
	{
		return founder;
	}

	public void setFounder(int founder)
	{
		this.founder = founder;
	}

	public int getBirthday()
	{
		return birthday;
	}

	public void setBirthday(int birthday)
	{
		this.birthday = birthday;
	}

	public int getNitroboost()
	{
		return nitroboost;
	}

	public void setNitroboost(int nitroboost)
	{
		this.nitroboost = nitroboost;
	}

	public int getSupporter()
	{
		return supporter;
	}

	public void setSupporter(int supporter)
	{
		this.supporter = supporter;
	}

	public int getDeveloper()
	{
		return developer;
	}

	public void setDeveloper(int developer)
	{
		this.developer = developer;
	}
}
