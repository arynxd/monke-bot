/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.objects.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Verification implements Serializable
{

	private static final long serialVersionUID = 1L;

	private final Long id;
	private final Long guildId;
	private final String phrase;
	private final Long roleId;

	public Verification(Verification value)
	{
		this.id = value.id;
		this.guildId = value.guildId;
		this.phrase = value.phrase;
		this.roleId = value.roleId;
	}

	public Verification(
			Long id,
			Long guildId,
			String phrase,
			Long roleId
	)
	{
		this.id = id;
		this.guildId = guildId;
		this.phrase = phrase;
		this.roleId = roleId;
	}

	/**
	 * Getter for <code>public.verification.id</code>.
	 */
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Getter for <code>public.verification.guild_id</code>.
	 */
	public Long getGuildId()
	{
		return this.guildId;
	}

	/**
	 * Getter for <code>public.verification.phrase</code>.
	 */
	public String getPhrase()
	{
		return this.phrase;
	}

	/**
	 * Getter for <code>public.verification.role_id</code>.
	 */
	public Long getRoleId()
	{
		return this.roleId;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Verification (");

		sb.append(id);
		sb.append(", ").append(guildId);
		sb.append(", ").append(phrase);
		sb.append(", ").append(roleId);

		sb.append(")");
		return sb.toString();
	}
}
