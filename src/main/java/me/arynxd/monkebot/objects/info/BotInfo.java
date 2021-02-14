package me.arynxd.monkebot.objects.info;

import java.lang.management.ManagementFactory;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;

public class BotInfo
{
	private BotInfo()
	{
		//Overrides the default, public, constructor
	}

	public static long getGuildCount(ShardManager shardManager)
	{
		return shardManager.getShardCache().size();
	}

	public static long getUserCount(ShardManager shardManager)
	{
		return shardManager.getGuildCache().applyStream(guildStream -> guildStream.mapToInt(Guild::getMemberCount)).sum();
	}

	public static String getJDAVersion()
	{
		return JDAInfo.VERSION;
	}

	public static String getJavaVersion()
	{
		return System.getProperty("java.version");
	}

	public static long getMaxMemory()
	{
		return Runtime.getRuntime().maxMemory();
	}

	public static long getFreeMemory()
	{
		return Runtime.getRuntime().freeMemory();
	}

	public static long getTotalMemory()
	{
		return Runtime.getRuntime().totalMemory();
	}

	public static long getTotalShards(ShardManager shardManager)
	{
		return shardManager.getShardsTotal();
	}

	public static long getThreadCount()
	{
		return ManagementFactory.getThreadMXBean().getThreadCount();
	}

	public static long getTotalServers(ShardManager shardManager)
	{
		return shardManager.getGuildCache().size();
	}

	public static String getMemoryFormatted()
	{
		return (getTotalMemory() - getFreeMemory() >> 20) + "MB / " + (getMaxMemory() >> 20) + "MB";
	}

	public static String getMemoryPercent()
	{
		return String.valueOf((int) (getTotalMemory() - getFreeMemory()) / (getMaxMemory()) * 100);
	}
}
