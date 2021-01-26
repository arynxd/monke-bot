/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.entities.jooq.tables;


import java.util.Arrays;
import java.util.List;

import me.arynxd.monkebot.entities.jooq.tables.records.VerificationRecord;
import me.arynxd.monkebot.entities.jooq.Keys;
import me.arynxd.monkebot.entities.jooq.Public;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Verification extends TableImpl<VerificationRecord>
{

	/**
	 * The reference instance of <code>public.verification</code>
	 */
	public static final Verification VERIFICATION = new Verification();
	private static final long serialVersionUID = 1L;
	/**
	 * The column <code>public.verification.id</code>.
	 */
	public final TableField<VerificationRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");
	/**
	 * The column <code>public.verification.guild_id</code>.
	 */
	public final TableField<VerificationRecord, Long> GUILD_ID = createField(DSL.name("guild_id"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.verification.phrase</code>.
	 */
	public final TableField<VerificationRecord, String> PHRASE = createField(DSL.name("phrase"), SQLDataType.CLOB.nullable(false), this, "");
	/**
	 * The column <code>public.verification.role_id</code>.
	 */
	public final TableField<VerificationRecord, Long> ROLE_ID = createField(DSL.name("role_id"), SQLDataType.BIGINT.nullable(false), this, "");

	private Verification(Name alias, Table<VerificationRecord> aliased)
	{
		this(alias, aliased, null);
	}

	private Verification(Name alias, Table<VerificationRecord> aliased, Field<?>[] parameters)
	{
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>public.verification</code> table reference
	 */
	public Verification(String alias)
	{
		this(DSL.name(alias), VERIFICATION);
	}

	/**
	 * Create an aliased <code>public.verification</code> table reference
	 */
	public Verification(Name alias)
	{
		this(alias, VERIFICATION);
	}

	/**
	 * Create a <code>public.verification</code> table reference
	 */
	public Verification()
	{
		this(DSL.name("verification"), null);
	}

	public <O extends Record> Verification(Table<O> child, ForeignKey<O, VerificationRecord> key)
	{
		super(child, key, VERIFICATION);
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<VerificationRecord> getRecordType()
	{
		return VerificationRecord.class;
	}

	@Override
	public Schema getSchema()
	{
		return Public.PUBLIC;
	}

	@Override
	public Identity<VerificationRecord, Long> getIdentity()
	{
		return (Identity<VerificationRecord, Long>) super.getIdentity();
	}

	@Override
	public UniqueKey<VerificationRecord> getPrimaryKey()
	{
		return Keys.VERIFICATION_PKEY;
	}

	@Override
	public List<UniqueKey<VerificationRecord>> getKeys()
	{
		return Arrays.<UniqueKey<VerificationRecord>>asList(Keys.VERIFICATION_PKEY, Keys.VERIFICATION_ROLE_ID_PHRASE_KEY);
	}

	@Override
	public List<ForeignKey<VerificationRecord, ?>> getReferences()
	{
		return Arrays.<ForeignKey<VerificationRecord, ?>>asList(Keys.VERIFICATION__VERIFICATION_GUILD_ID_FKEY);
	}

	public Guilds guilds()
	{
		return new Guilds(this, Keys.VERIFICATION__VERIFICATION_GUILD_ID_FKEY);
	}

	@Override
	public Verification as(String alias)
	{
		return new Verification(DSL.name(alias), this);
	}

	@Override
	public Verification as(Name alias)
	{
		return new Verification(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Verification rename(String name)
	{
		return new Verification(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Verification rename(Name name)
	{
		return new Verification(name, null);
	}

	// -------------------------------------------------------------------------
	// Row4 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row4<Long, Long, String, Long> fieldsRow()
	{
		return (Row4) super.fieldsRow();
	}
}
