package me.arynxd.monkebot.web.invite;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class InviteDiscordRoute implements Handler
{
	@Override
	public void handle(@NotNull Context ctx)
	{
		ctx.redirect("https://discord.gg/KPWfT9Gq66");
	}
}
