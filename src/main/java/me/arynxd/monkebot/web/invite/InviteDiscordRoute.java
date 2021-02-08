package me.arynxd.monkebot.web.invite;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.arynxd.monkebot.handlers.WebHandler;
import org.jetbrains.annotations.NotNull;

public class InviteDiscordRoute implements Handler
{
	private final WebHandler webHandler;

	public InviteDiscordRoute(WebHandler webHandler)
	{
		this.webHandler = webHandler;
	}

	@Override
	public void handle(@NotNull Context ctx)
	{
		ctx.redirect("https://discord.gg/KPWfT9Gq66");
	}
}
