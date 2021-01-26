package org.igsq.igsqbot.minecraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MinecraftChecks
{
	private MinecraftChecks()
	{
		//Overrides the default, public, constructor
	}

	public static boolean isOwnerOfAccount(String uuid, String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT id FROM linked_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1).equals(id);
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}

	public static boolean isAccountLinked(String uuid, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM linked_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1).equalsIgnoreCase(MinecraftUtils.LinkState.LINKED.toString());
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}

	public static boolean isAccountExist(String username, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM mc_accounts WHERE username = ?");
			statement.setString(1, username);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			return resultSet.next();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}

	public static boolean isPendingDiscord(String uuid, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM linked_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1).equalsIgnoreCase(MinecraftUtils.LinkState.DWAIT.toString());
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}

	public static boolean isUserLinked(String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM linked_accounts WHERE id = ?");
			statement.setString(1, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1).equalsIgnoreCase(MinecraftUtils.LinkState.LINKED.toString());
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}

	public static boolean isDuplicate(String uuid, String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM linked_accounts WHERE uuid = ? AND id = ?");
			statement.setString(1, uuid);
			statement.setString(2, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1).equalsIgnoreCase(MinecraftUtils.LinkState.MWAIT.toString());
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return false;
		}
	}
}
