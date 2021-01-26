package org.igsq.igsqbot.minecraft;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftTwoFA
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftTwoFA.class);
	private final Minecraft minecraft;
	private final IGSQBot igsqBot;

	public MinecraftTwoFA(Minecraft minecraft)
	{
		LOGGER.debug("Minecraft 2FA started.");
		this.minecraft = minecraft;
		this.igsqBot = minecraft.getIGSQBot();
	}

	public void start()
	{
		if(minecraft.getDatabaseHandler().isOffline())
		{
			igsqBot.getLogger().warn("Minecraft 2FA stopped. Database not online.");
		}
		else
		{
			igsqBot.getTaskHandler().addRepeatingTask(this::twofa, "minecraft2FA", TimeUnit.SECONDS, 5);
		}
	}

	private void twofa()
	{
		for(String user : getPendingUsers())
		{
			messageUser(user);
		}
	}

	private void messageUser(String userId)
	{
		igsqBot.getShardManager().retrieveUserById(userId).flatMap(User::openPrivateChannel).queue(
				privateChannel ->
				{
					String code = generateCode();
					privateChannel.sendMessage(new EmbedBuilder()
							.setDescription("Your Minecraft 2FA code is `" + code + "`\n If you did not expect this, please change your Minecraft password immediately!")
							.setColor(Constants.IGSQ_PURPLE)
							.build()).queue(
							message ->
							{
								MinecraftUtils.addCode(userId, code, minecraft);
								igsqBot.getTaskHandler().addTask(() ->
								{
									message.editMessage(new EmbedBuilder()
											.setDescription("Your Minecraft 2FA code: **EXPIRED**")
											.setColor(Constants.IGSQ_PURPLE)
											.build()).queue();

									MinecraftUtils.removeCode(userId, minecraft);

								}, TimeUnit.SECONDS, 60);
							}
					);
				},
				error -> new ErrorHandler().ignore(ErrorResponse.CANNOT_SEND_TO_USER)
		);
	}

	private List<String> getPendingUsers()
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			Statement statement = connection.createStatement();
			statement.execute("SELECT * FROM discord_2fa WHERE current_status = 'pending' && code IS NULL");

			ResultSet resultSet = statement.getResultSet();
			List<String> result = new ArrayList<>();

			while(resultSet.next())
			{
				result.add(MinecraftUtils.getUserId(resultSet.getString(1), minecraft));
			}

			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error has occurred", exception);
			return Collections.emptyList();
		}
	}

	private String generateCode()
	{
		Random random = new Random();
		return String.format("%06d", random.nextInt(999999));
	}

	public void close()
	{
		igsqBot.getTaskHandler().cancelTask("minecraft2FA", false);
		igsqBot.getLogger().info("Minecraft 2FA stopped.");
		LOGGER.debug("Minecraft 2FA closed.");
	}

	public void close(String message)
	{
		igsqBot.getTaskHandler().cancelTask("minecraft2FA", false);
		igsqBot.getLogger().info("Minecraft 2FA stopped. " + message);
		LOGGER.debug("Minecraft 2FA closed.");
	}
}
