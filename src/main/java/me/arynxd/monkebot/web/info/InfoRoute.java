package me.arynxd.monkebot.web.info;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.arynxd.monkebot.handlers.WebHandler;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class InfoRoute implements Handler
{
	private final WebHandler webHandler;

	public InfoRoute(WebHandler webHandler)
	{
		this.webHandler = webHandler;
	}

	@Override
	public void handle(@NotNull Context ctx)
	{
		ShardManager shardManager = webHandler.getMonke().getShardManager();
		webHandler.ok(ctx, DataObject.empty()
				.put("shards", shardManager.getShardCache().size())
				.put("guilds", shardManager.getGuildCache().size())
				.put("users", shardManager.getGuildCache().applyStream(guildStream -> guildStream.mapToInt(Guild::getMemberCount).sum()))
				.put("jda_version", JDAInfo.VERSION)
				.put("players", webHandler.getMonke().getMusicHandler().getPlayers())
				.put("commands", webHandler.getMonke().getCommandHandler().getCommandMap().values().stream().distinct().count())
		);
	}
}
