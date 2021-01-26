CREATE TABLE IF NOT EXISTS votes
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    vote_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    direct_message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    option INT NOT NULL DEFAULT -1,
    max_options INT NOT NULL,
    expiry TIMESTAMP NOT NULL,
    has_voted BOOLEAN NOT NULL DEFAULT FALSE
);