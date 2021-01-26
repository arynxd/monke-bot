/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.entities.jooq.tables;


import java.util.Arrays;
import java.util.List;

import me.arynxd.monkebot.entities.jooq.tables.records.ReactionRolesRecord;
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
public class ReactionRoles extends TableImpl<ReactionRolesRecord>
{

	/**
	 * The reference instance of <code>public.reaction_roles</code>
	 */
	public static final ReactionRoles REACTION_ROLES = new ReactionRoles();
	private static final long serialVersionUID = 1L;
	/**
	 * The column <code>public.reaction_roles.id</code>.
	 */
	public final TableField<ReactionRolesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");
	/**
	 * The column <code>public.reaction_roles.message_id</code>.
	 */
	public final TableField<ReactionRolesRecord, Long> MESSAGE_ID = createField(DSL.name("message_id"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reaction_roles.guild_id</code>.
	 */
	public final TableField<ReactionRolesRecord, Long> GUILD_ID = createField(DSL.name("guild_id"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reaction_roles.emote_id</code>.
	 */
	public final TableField<ReactionRolesRecord, String> EMOTE_ID = createField(DSL.name("emote_id"), SQLDataType.VARCHAR(25), this, "");
	/**
	 * The column <code>public.reaction_roles.role_id</code>.
	 */
	public final TableField<ReactionRolesRecord, Long> ROLE_ID = createField(DSL.name("role_id"), SQLDataType.BIGINT, this, "");

	private ReactionRoles(Name alias, Table<ReactionRolesRecord> aliased)
	{
		this(alias, aliased, null);
	}

	private ReactionRoles(Name alias, Table<ReactionRolesRecord> aliased, Field<?>[] parameters)
	{
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>public.reaction_roles</code> table reference
	 */
	public ReactionRoles(String alias)
	{
		this(DSL.name(alias), REACTION_ROLES);
	}

	/**
	 * Create an aliased <code>public.reaction_roles</code> table reference
	 */
	public ReactionRoles(Name alias)
	{
		this(alias, REACTION_ROLES);
	}

	/**
	 * Create a <code>public.reaction_roles</code> table reference
	 */
	public ReactionRoles()
	{
		this(DSL.name("reaction_roles"), null);
	}

	public <O extends Record> ReactionRoles(Table<O> child, ForeignKey<O, ReactionRolesRecord> key)
	{
		super(child, key, REACTION_ROLES);
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ReactionRolesRecord> getRecordType()
	{
		return ReactionRolesRecord.class;
	}

	@Override
	public Schema getSchema()
	{
		return Public.PUBLIC;
	}

	@Override
	public Identity<ReactionRolesRecord, Long> getIdentity()
	{
		return (Identity<ReactionRolesRecord, Long>) super.getIdentity();
	}

	@Override
	public UniqueKey<ReactionRolesRecord> getPrimaryKey()
	{
		return Keys.REACTION_ROLES_PKEY;
	}

	@Override
	public List<UniqueKey<ReactionRolesRecord>> getKeys()
	{
		return Arrays.<UniqueKey<ReactionRolesRecord>>asList(Keys.REACTION_ROLES_PKEY);
	}

	@Override
	public ReactionRoles as(String alias)
	{
		return new ReactionRoles(DSL.name(alias), this);
	}

	@Override
	public ReactionRoles as(Name alias)
	{
		return new ReactionRoles(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public ReactionRoles rename(String name)
	{
		return new ReactionRoles(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public ReactionRoles rename(Name name)
	{
		return new ReactionRoles(name, null);
	}

	// -------------------------------------------------------------------------
	// Row5 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row5<Long, Long, Long, String, Long> fieldsRow()
	{
		return (Row5) super.fieldsRow();
	}
}
