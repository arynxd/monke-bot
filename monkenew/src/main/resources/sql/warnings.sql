CREATE TABLE IF NOT EXISTS warnings
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT current_timestamp,
    warn_text TEXT NOT NULL
);