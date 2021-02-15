/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.objects.jooq.tables;


import java.util.Arrays;
import java.util.List;
import me.arynxd.monkebot.objects.jooq.Keys;
import me.arynxd.monkebot.objects.jooq.Public;
import me.arynxd.monkebot.objects.jooq.tables.records.ChannelBlacklistsRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class ChannelBlacklists extends TableImpl<ChannelBlacklistsRecord>
{

	/**
	 * The reference instance of <code>public.channel_blacklists</code>
	 */
	public static final ChannelBlacklists CHANNEL_BLACKLISTS = new ChannelBlacklists();
	private static final long serialVersionUID = 1L;
	/**
	 * The column <code>public.channel_blacklists.id</code>.
	 */
	public final TableField<ChannelBlacklistsRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");
	/**
	 * The column <code>public.channel_blacklists.guild_id</code>.
	 */
	public final TableField<ChannelBlacklistsRecord, Long> GUILD_ID = createField(DSL.name("guild_id"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.channel_blacklists.channel_id</code>.
	 */
	public final TableField<ChannelBlacklistsRecord, Long> CHANNEL_ID = createField(DSL.name("channel_id"), SQLDataType.BIGINT.nullable(false), this, "");

	private ChannelBlacklists(Name alias, Table<ChannelBlacklistsRecord> aliased)
	{
		this(alias, aliased, null);
	}

	private ChannelBlacklists(Name alias, Table<ChannelBlacklistsRecord> aliased, Field<?>[] parameters)
	{
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>public.channel_blacklists</code> table reference
	 */
	public ChannelBlacklists(String alias)
	{
		this(DSL.name(alias), CHANNEL_BLACKLISTS);
	}

	/**
	 * Create an aliased <code>public.channel_blacklists</code> table reference
	 */
	public ChannelBlacklists(Name alias)
	{
		this(alias, CHANNEL_BLACKLISTS);
	}

	/**
	 * Create a <code>public.channel_blacklists</code> table reference
	 */
	public ChannelBlacklists()
	{
		this(DSL.name("channel_blacklists"), null);
	}

	public <O extends Record> ChannelBlacklists(Table<O> child, ForeignKey<O, ChannelBlacklistsRecord> key)
	{
		super(child, key, CHANNEL_BLACKLISTS);
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ChannelBlacklistsRecord> getRecordType()
	{
		return ChannelBlacklistsRecord.class;
	}

	@Override
	public Schema getSchema()
	{
		return Public.PUBLIC;
	}

	@Override
	public Identity<ChannelBlacklistsRecord, Long> getIdentity()
	{
		return (Identity<ChannelBlacklistsRecord, Long>) super.getIdentity();
	}

	@Override
	public UniqueKey<ChannelBlacklistsRecord> getPrimaryKey()
	{
		return Keys.CHANNEL_BLACKLISTS_PKEY;
	}

	@Override
	public List<UniqueKey<ChannelBlacklistsRecord>> getKeys()
	{
		return Arrays.<UniqueKey<ChannelBlacklistsRecord>>asList(Keys.CHANNEL_BLACKLISTS_PKEY);
	}

	@Override
	public List<ForeignKey<ChannelBlacklistsRecord, ?>> getReferences()
	{
		return Arrays.<ForeignKey<ChannelBlacklistsRecord, ?>>asList(Keys.CHANNEL_BLACKLISTS__CHANNEL_BLACKLISTS_GUILD_ID_FKEY);
	}

	public Guilds guilds()
	{
		return new Guilds(this, Keys.CHANNEL_BLACKLISTS__CHANNEL_BLACKLISTS_GUILD_ID_FKEY);
	}

	@Override
	public ChannelBlacklists as(String alias)
	{
		return new ChannelBlacklists(DSL.name(alias), this);
	}

	@Override
	public ChannelBlacklists as(Name alias)
	{
		return new ChannelBlacklists(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public ChannelBlacklists rename(String name)
	{
		return new ChannelBlacklists(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public ChannelBlacklists rename(Name name)
	{
		return new ChannelBlacklists(name, null);
	}

	// -------------------------------------------------------------------------
	// Row3 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row3<Long, Long, Long> fieldsRow()
	{
		return (Row3) super.fieldsRow();
	}
}