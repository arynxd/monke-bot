package me.arynxd.monkebot.handlers;

import io.prometheus.client.exporter.HTTPServer;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.entities.bot.Metrics;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PrometheusHandler extends ListenerAdapter
{
	public PrometheusHandler(Monke monke)
	{
		try
		{
			new HTTPServer(4444);
		}
		catch(Exception exception)
		{
			monke.getLogger().error("A prometheus error has occurred.", exception);
		}
	}

	@Override
	public void onHttpRequest(@NotNull HttpRequestEvent event)
	{
		if(event.isRateLimit())
		{
			Metrics.HTTP_429_REQUESTS.inc();
		}
		Metrics.HTTP_REQUESTS.inc();
	}
}
