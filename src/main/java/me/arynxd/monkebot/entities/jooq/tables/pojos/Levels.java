/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.entities.jooq.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Levels implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long    id;
    private final Long    guildId;
    private final Long    roleId;
    private final Integer awardedAt;

    public Levels(Levels value) {
        this.id = value.id;
        this.guildId = value.guildId;
        this.roleId = value.roleId;
        this.awardedAt = value.awardedAt;
    }

    public Levels(
        Long    id,
        Long    guildId,
        Long    roleId,
        Integer awardedAt
    ) {
        this.id = id;
        this.guildId = guildId;
        this.roleId = roleId;
        this.awardedAt = awardedAt;
    }

    /**
     * Getter for <code>public.levels.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.levels.guild_id</code>.
     */
    public Long getGuildId() {
        return this.guildId;
    }

    /**
     * Getter for <code>public.levels.role_id</code>.
     */
    public Long getRoleId() {
        return this.roleId;
    }

    /**
     * Getter for <code>public.levels.awarded_at</code>.
     */
    public Integer getAwardedAt() {
        return this.awardedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Levels (");

        sb.append(id);
        sb.append(", ").append(guildId);
        sb.append(", ").append(roleId);
        sb.append(", ").append(awardedAt);

        sb.append(")");
        return sb.toString();
    }
}
