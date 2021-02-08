package me.arynxd.monkebot.entities.bot;

import io.prometheus.client.Counter;

public class Metrics
{
	public static final Counter COMMAND_COUNTER = Counter.build()
			.name("monkebot_commands")
			.help("Amounts of commands ran by name")
			.labelNames("name")
			.register();

	public static final Counter HTTP_REQUESTS = Counter.build()
			.name("monkebot_http_requests")
			.help("Successful HTTP Requests (JDA)")
			.register();

	public static final Counter HTTP_429_REQUESTS = Counter.build()
			.name("monkebot_http_ratelimit_requests")
			.help("429 HTTP Requests (JDA)")
			.register();

	private Metrics() {}

}
