package me.arynxd.monkebot.handlers;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.web.info.InfoRoute;
import me.arynxd.monkebot.web.invite.InviteBotRoute;
import me.arynxd.monkebot.web.invite.InviteDiscordRoute;
import me.arynxd.monkebot.web.shards.ShardsRoute;
import net.dv8tion.jda.api.utils.data.DataObject;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class WebHandler
{
	private final Monke monke;
	private final Javalin javalin;

	public WebHandler(Monke monke)
	{
		this.monke = monke;
		this.javalin = Javalin
				.create(JavalinConfig::enableCorsForAllOrigins)
				.routes(() ->
				{
					path("/shards", () -> get(new ShardsRoute(this)));
					path("/info", () -> get(new InfoRoute(this)));

					path("/invite", () ->
					{
						get(new InviteBotRoute(this));
						path("/bot", () -> get(new InviteBotRoute(this)));
						path("/discord", () -> get(new InviteDiscordRoute()));
					});

					path("/health", () -> get(ctx -> ctx.result("Healthy")));
				}).start(4444);
	}

	public Monke getMonke()
	{
		return monke;
	}

	public Javalin getJavalin()
	{
		return javalin;
	}

	public void ok(Context context, DataObject payload)
	{
		result(context, 200, payload);
	}

	public void result(Context ctx, int code, DataObject data)
	{
		ctx.header("Content-Type", "application/json");
		ctx.status(code);
		ctx.result(data.toString());
	}
}
