CREATE TABLE IF NOT EXISTS levels
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id),
    role_id BIGINT NOT NULL,
    awarded_at INT NOT NULL
);