CREATE TABLE IF NOT EXISTS reports
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    message_id BIGINT NOT NULL UNIQUE,
    report_message_id BIGINT NOT NULL UNIQUE,
    channel_id BIGINT NOT NULL,
    guild_id BIGINT NOT NULL REFERENCES guilds(guild_id) ON DELETE CASCADE,
    reporter_id BIGINT NOT NULL,
    reporttee_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT current_timestamp,
    report_text TEXT NOT NULL
);