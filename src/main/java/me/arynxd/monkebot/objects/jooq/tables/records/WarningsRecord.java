/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.objects.jooq.tables.records;


import java.time.LocalDateTime;
import me.arynxd.monkebot.objects.jooq.tables.Warnings;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class WarningsRecord extends UpdatableRecordImpl<WarningsRecord>
		implements Record5<Long, Long, Long, LocalDateTime, String>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Create a detached WarningsRecord
	 */
	public WarningsRecord()
	{
		super(Warnings.WARNINGS);
	}

	/**
	 * Create a detached, initialised WarningsRecord
	 */
	public WarningsRecord(Long id, Long guildId, Long userId, LocalDateTime timestamp, String warnText)
	{
		super(Warnings.WARNINGS);

		setId(id);
		setGuildId(guildId);
		setUserId(userId);
		setTimestamp(timestamp);
		setWarnText(warnText);
	}

	/**
	 * Getter for <code>public.warnings.id</code>.
	 */
	public Long getId()
	{
		return (Long) get(0);
	}

	/**
	 * Setter for <code>public.warnings.id</code>.
	 */
	public WarningsRecord setId(Long value)
	{
		set(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.warnings.guild_id</code>.
	 */
	public Long getGuildId()
	{
		return (Long) get(1);
	}

	/**
	 * Setter for <code>public.warnings.guild_id</code>.
	 */
	public WarningsRecord setGuildId(Long value)
	{
		set(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.warnings.user_id</code>.
	 */
	public Long getUserId()
	{
		return (Long) get(2);
	}

	/**
	 * Setter for <code>public.warnings.user_id</code>.
	 */
	public WarningsRecord setUserId(Long value)
	{
		set(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.warnings.timestamp</code>.
	 */
	public LocalDateTime getTimestamp()
	{
		return (LocalDateTime) get(3);
	}

	/**
	 * Setter for <code>public.warnings.timestamp</code>.
	 */
	public WarningsRecord setTimestamp(LocalDateTime value)
	{
		set(3, value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * Getter for <code>public.warnings.warn_text</code>.
	 */
	public String getWarnText()
	{
		return (String) get(4);
	}

	// -------------------------------------------------------------------------
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * Setter for <code>public.warnings.warn_text</code>.
	 */
	public WarningsRecord setWarnText(String value)
	{
		set(4, value);
		return this;
	}

	@Override
	public Record1<Long> key()
	{
		return (Record1) super.key();
	}

	@Override
	public Row5<Long, Long, Long, LocalDateTime, String> fieldsRow()
	{
		return (Row5) super.fieldsRow();
	}

	@Override
	public Row5<Long, Long, Long, LocalDateTime, String> valuesRow()
	{
		return (Row5) super.valuesRow();
	}

	@Override
	public Field<Long> field1()
	{
		return Warnings.WARNINGS.ID;
	}

	@Override
	public Field<Long> field2()
	{
		return Warnings.WARNINGS.GUILD_ID;
	}

	@Override
	public Field<Long> field3()
	{
		return Warnings.WARNINGS.USER_ID;
	}

	@Override
	public Field<LocalDateTime> field4()
	{
		return Warnings.WARNINGS.TIMESTAMP;
	}

	@Override
	public Field<String> field5()
	{
		return Warnings.WARNINGS.WARN_TEXT;
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
	public Long component3()
	{
		return getUserId();
	}

	@Override
	public LocalDateTime component4()
	{
		return getTimestamp();
	}

	@Override
	public String component5()
	{
		return getWarnText();
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
	public Long value3()
	{
		return getUserId();
	}

	@Override
	public LocalDateTime value4()
	{
		return getTimestamp();
	}

	@Override
	public String value5()
	{
		return getWarnText();
	}

	@Override
	public WarningsRecord value1(Long value)
	{
		setId(value);
		return this;
	}

	@Override
	public WarningsRecord value2(Long value)
	{
		setGuildId(value);
		return this;
	}

	@Override
	public WarningsRecord value3(Long value)
	{
		setUserId(value);
		return this;
	}

	@Override
	public WarningsRecord value4(LocalDateTime value)
	{
		setTimestamp(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	@Override
	public WarningsRecord value5(String value)
	{
		setWarnText(value);
		return this;
	}

	@Override
	public WarningsRecord values(Long value1, Long value2, Long value3, LocalDateTime value4, String value5)
	{
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}
}
