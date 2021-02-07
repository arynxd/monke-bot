/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.entities.jooq.tables.records;


import me.arynxd.monkebot.entities.jooq.tables.WordBlacklists;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WordBlacklistsRecord extends UpdatableRecordImpl<WordBlacklistsRecord>
		implements Record3<Long, Long, String>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Create a detached WordBlacklistsRecord
	 */
	public WordBlacklistsRecord()
	{
		super(WordBlacklists.WORD_BLACKLISTS);
	}

	/**
	 * Create a detached, initialised WordBlacklistsRecord
	 */
	public WordBlacklistsRecord(Long id, Long guildId, String phrase)
	{
		super(WordBlacklists.WORD_BLACKLISTS);

		setId(id);
		setGuildId(guildId);
		setPhrase(phrase);
	}

	/**
	 * Getter for <code>public.word_blacklists.id</code>.
	 */
	public Long getId()
	{
		return (Long) get(0);
	}

	/**
	 * Setter for <code>public.word_blacklists.id</code>.
	 */
	public WordBlacklistsRecord setId(Long value)
	{
		set(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.word_blacklists.guild_id</code>.
	 */
	public Long getGuildId()
	{
		return (Long) get(1);
	}

	/**
	 * Setter for <code>public.word_blacklists.guild_id</code>.
	 */
	public WordBlacklistsRecord setGuildId(Long value)
	{
		set(1, value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * Getter for <code>public.word_blacklists.phrase</code>.
	 */
	public String getPhrase()
	{
		return (String) get(2);
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * Setter for <code>public.word_blacklists.phrase</code>.
	 */
	public WordBlacklistsRecord setPhrase(String value)
	{
		set(2, value);
		return this;
	}

	@Override
	public Record1<Long> key()
	{
		return (Record1) super.key();
	}

	@Override
	public Row3<Long, Long, String> fieldsRow()
	{
		return (Row3) super.fieldsRow();
	}

	@Override
	public Row3<Long, Long, String> valuesRow()
	{
		return (Row3) super.valuesRow();
	}

	@Override
	public Field<Long> field1()
	{
		return WordBlacklists.WORD_BLACKLISTS.ID;
	}

	@Override
	public Field<Long> field2()
	{
		return WordBlacklists.WORD_BLACKLISTS.GUILD_ID;
	}

	@Override
	public Field<String> field3()
	{
		return WordBlacklists.WORD_BLACKLISTS.PHRASE;
	}

	@Override
	public Long component1()
	{
		return getId();
	}

	@Override
	public Long component2()
	{
		return getGuildId();
	}

	@Override
	public String component3()
	{
		return getPhrase();
	}

	@Override
	public Long value1()
	{
		return getId();
	}

	@Override
	public Long value2()
	{
		return getGuildId();
	}

	@Override
	public String value3()
	{
		return getPhrase();
	}

	@Override
	public WordBlacklistsRecord value1(Long value)
	{
		setId(value);
		return this;
	}

	@Override
	public WordBlacklistsRecord value2(Long value)
	{
		setGuildId(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	@Override
	public WordBlacklistsRecord value3(String value)
	{
		setPhrase(value);
		return this;
	}

	@Override
	public WordBlacklistsRecord values(Long value1, Long value2, String value3)
	{
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}
}
