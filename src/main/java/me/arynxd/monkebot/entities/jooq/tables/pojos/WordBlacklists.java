/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.entities.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WordBlacklists implements Serializable
{

	private static final long serialVersionUID = 1L;

	private final Long id;
	private final Long guildId;
	private final String phrase;

	public WordBlacklists(WordBlacklists value)
	{
		this.id = value.id;
		this.guildId = value.guildId;
		this.phrase = value.phrase;
	}

	public WordBlacklists(
			Long id,
			Long guildId,
			String phrase
	)
	{
		this.id = id;
		this.guildId = guildId;
		this.phrase = phrase;
	}

	/**
	 * Getter for <code>public.word_blacklists.id</code>.
	 */
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Getter for <code>public.word_blacklists.guild_id</code>.
	 */
	public Long getGuildId()
	{
		return this.guildId;
	}

	/**
	 * Getter for <code>public.word_blacklists.phrase</code>.
	 */
	public String getPhrase()
	{
		return this.phrase;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("WordBlacklists (");

		sb.append(id);
		sb.append(", ").append(guildId);
		sb.append(", ").append(phrase);

		sb.append(")");
		return sb.toString();
	}
}
