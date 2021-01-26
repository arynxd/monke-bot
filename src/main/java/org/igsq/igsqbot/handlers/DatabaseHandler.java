package org.igsq.igsqbot.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.bot.ConfigOption;
import org.igsq.igsqbot.entities.bot.Configuration;
import org.igsq.igsqbot.util.FileUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseHandler(IGSQBot igsqBot)
	{
		LOGGER.debug("Starting local database pool.");
		this.igsqBot = igsqBot;
		this.pool = initHikari();
		initTables();
		System.getProperties().setProperty("org.jooq.no-logo", "true");
	}

	private void initTables()
	{
		LOGGER.debug("Initialise table guilds.");
		initTable("guilds");

		LOGGER.debug("Initialise table roles.");
		initTable("roles");

		LOGGER.debug("Initialise table tempbans.");
		initTable("tempbans");

		LOGGER.debug("Initialise table reaction_roles.");
		initTable("reaction_roles");

		LOGGER.debug("Initialise table reminders.");
		initTable("reminders");

		LOGGER.debug("Initialise table reports.");
		initTable("reports");

		LOGGER.debug("Initialise table warnings.");
		initTable("warnings");

		LOGGER.debug("Initialise table votes.");
		initTable("votes");

		LOGGER.debug("Initialise table word_blacklists.");
		initTable("word_blacklists");

		LOGGER.debug("Initialise table channel_blacklists.");
		initTable("channel_blacklists");

		LOGGER.debug("Initialise table levels.");
		initTable("levels");

		LOGGER.debug("Initialise table verification.");
		initTable("verification");

		LOGGER.debug("Table setup complete.");
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

	private void initTable(String table)
	{
		try
		{
			InputStream file = DatabaseHandler.class.getClassLoader().getResourceAsStream("sql/" + table + ".sql");
			if(file == null)
			{
				throw new NullPointerException("File for table '" + table + "' not found");
			}
			getConnection().createStatement().execute(FileUtils.convertToString(file));
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("Error initializing table: '" + table + "'", exception);
		}
	}

	private HikariDataSource initHikari()
	{
		LOGGER.debug("Starting local HikariCP setup.");
		HikariConfig hikariConfig = new HikariConfig();
		Configuration configuration = igsqBot.getConfig();
		hikariConfig.setDriverClassName("org.postgresql.Driver");
		hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.LOCALURL));

		hikariConfig.setUsername(igsqBot.getConfig().getString(ConfigOption.LOCALUSERNAME));
		hikariConfig.setPassword(igsqBot.getConfig().getString(ConfigOption.LOCALPASSWORD));

		hikariConfig.setMaximumPoolSize(30);
		hikariConfig.setMinimumIdle(10);
		hikariConfig.setConnectionTimeout(10000);
		LOGGER.debug("Local HikariCP setup complete.");
		try
		{
			return new HikariDataSource(hikariConfig);
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("Local database offline, connection failure.");
			System.exit(1);
			return null;
		}
	}

	public DSLContext getContext()
	{
		return getContext(getConnection());
	}

	public DSLContext getContext(Connection connection)
	{
		return DSL.using(connection, SQLDialect.POSTGRES);
	}

	public void close()
	{
		LOGGER.debug("Closed local database.");
		pool.close();
	}
}
