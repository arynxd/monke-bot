package org.igsq.igsqbot.entities.info;

import java.lang.management.ManagementFactory;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.sharding.ShardManager;

public class BotInfo
{
	private BotInfo()
	{
		//Overrides the default, public, constructor
	}

	public static String getJDAVersion()
	{
		return JDAInfo.VERSION;
	}

	public static String getJavaVersion()
	{
		return System.getProperty("java.version");
	}

	public static String getJavaVendor()
	{
		return System.getProperty("java.vendor");
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

	public static long getAvailableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
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
}
