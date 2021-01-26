package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.IGSQBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Minecraft
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Minecraft.class);
	private final IGSQBot igsqBot;
	private final DatabaseHandler databaseHandler;
	private MinecraftSync sync;
	private MinecraftTwoFA twoFA;

	public Minecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		this.databaseHandler = new DatabaseHandler(igsqBot);
		start();
	}

	public MinecraftSync getSync()
	{
		return sync;
	}

	public void start()
	{
		LOGGER.debug("Starting minecraft");

		this.sync = new MinecraftSync(this);
		this.twoFA = new MinecraftTwoFA(this);
		LOGGER.debug("Starting sync");
		sync.start();
		LOGGER.debug("Starting 2FA");
		twoFA.start();
	}

	public DatabaseHandler getDatabaseHandler()
	{
		return databaseHandler;
	}

	public IGSQBot getIGSQBot()
	{
		return igsqBot;
	}

	public void close()
	{
		LOGGER.debug("Closing Minecraft.");
		sync.close();
		twoFA.close();
		databaseHandler.close();
	}
}
