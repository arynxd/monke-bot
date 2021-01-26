package org.igsq.igsqbot.minecraft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.bot.ConfigOption;
import org.igsq.igsqbot.entities.bot.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseHandler(IGSQBot igsqBot)
	{
		LOGGER.debug("Starting remote database pool.");
		this.igsqBot = igsqBot;
		this.pool = initHikari();
	}

	public HikariDataSource getPool()
	{
		return pool;
	}

	public Connection getConnection()
	{
		try
		{
			return pool.getConnection();
		}
		catch(Exception exception)
		{
			return getConnection();
		}
	}

	public boolean isOffline()
	{
		return pool == null || !pool.isRunning();
	}

	private HikariDataSource initHikari()
	{
		LOGGER.debug("Starting minecraft HikariCP setup.");
		HikariConfig hikariConfig = new HikariConfig();
		Configuration configuration = igsqBot.getConfig();
		hikariConfig.setDriverClassName(configuration.getString(ConfigOption.REMOTEDRIVER));
		hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.REMOTEURL));

		hikariConfig.setUsername(igsqBot.getConfig().getString(ConfigOption.REMOTEUSERNAME));
		hikariConfig.setPassword(igsqBot.getConfig().getString(ConfigOption.REMOTEPASSWORD));

		hikariConfig.setMaximumPoolSize(30);
		hikariConfig.setMinimumIdle(10);
		hikariConfig.setConnectionTimeout(10000);
		LOGGER.debug("Minecraft HikariCP setup complete.");
		try
		{
			return new HikariDataSource(hikariConfig);
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("Minecraft database offline, connection failure.");
			return null;
		}
	}

	public void close()
	{
		LOGGER.debug("Closed minecraft database.");
		pool.close();
	}
}
