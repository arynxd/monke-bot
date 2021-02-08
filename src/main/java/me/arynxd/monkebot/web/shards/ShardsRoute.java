package me.arynxd.monkebot.web.shards;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.stream.Collectors;
import me.arynxd.monkebot.handlers.WebHandler;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

public class ShardsRoute implements Handler
{
	private final WebHandler webHandler;

	public ShardsRoute(WebHandler webHandler)
	{
		this.webHandler = webHandler;
	}

	@Override
	public void handle(@NotNull Context ctx)
	{
		webHandler.ok(ctx, DataObject.empty()
				.put("shards", DataArray.fromCollection(
						webHandler.getMonke().getShardManager().getShardCache().stream().map(
								shard -> DataObject.empty()
										.put("id", shard.getShardInfo().getShardId())
										.put("guilds", shard.getGuildCache().size())
										.put("status", shard.getStatus().name())
										.put("ping", shard.getGatewayPing())

						).collect(Collectors.toList())))
		);
	}
}
