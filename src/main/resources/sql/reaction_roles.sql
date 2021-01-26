CREATE TABLE IF NOT EXISTS reaction_roles
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    message_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    emote_id VARCHAR(25) NULL,
    role_id BIGINT NULL
);