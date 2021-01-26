CREATE TABLE IF NOT EXISTS verification
(
    id bigserial PRIMARY KEY NOT NULL,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    phrase TEXT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(role_id, phrase)
);