package me.arynxd.monkebot.web.invite;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.arynxd.monkebot.handlers.WebHandler;
import org.jetbrains.annotations.NotNull;

public class InviteBotRoute implements Handler
{
	private final WebHandler webHandler;

	public InviteBotRoute(WebHandler webHandler)
	{
		this.webHandler = webHandler;
	}

	@Override
	public void handle(@NotNull Context ctx)
	{
		ctx.redirect("https://discord.com/oauth2/authorize?client_id=" + webHandler.getMonke().getJDA().getSelfUser().getId() + "&permissions=8&scope=bot");
	}
}
