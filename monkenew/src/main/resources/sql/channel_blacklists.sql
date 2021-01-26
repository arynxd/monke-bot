CREATE TABLE IF NOT EXISTS channel_blacklists
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id),
    channel_id BIGINT NOT NULL
);