package org.igsq.igsqbot.minecraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinecraftUtils
{
	private MinecraftUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void insertMember(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("" +
					"INSERT INTO discord_accounts (id, username, nickname, role, founder, birthday, nitroboost, supporter, developer) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

			fillStatement(user, preparedStatement);
			preparedStatement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void updateMember(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE discord_accounts SET " +
					"id = ?," +
					"username = ?," +
					"nickname = ?," +
					"role = ?," +
					"founder = ?," +
					"birthday = ?," +
					"nitroboost = ?," +
					"supporter = ?," +
					"developer = ? " +
					"WHERE id = ?");

			fillStatement(user, preparedStatement);
			preparedStatement.setString(10, user.getId());
			preparedStatement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	private static void fillStatement(MinecraftUser user, PreparedStatement preparedStatement) throws SQLException
	{
		preparedStatement.setString(1, user.getId());
		preparedStatement.setString(2, user.getUsername());
		preparedStatement.setString(3, user.getNickname());
		preparedStatement.setString(4, user.getRole());

		preparedStatement.setInt(5, user.getFounder());
		preparedStatement.setInt(6, user.getBirthday());
		preparedStatement.setInt(7, user.getNitroboost());
		preparedStatement.setInt(8, user.getSupporter());
		preparedStatement.setInt(9, user.getDeveloper());
	}

	public static int isMemberPresent(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM discord_accounts WHERE id = ?");

			preparedStatement.setString(1, user.getId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getInt(1);
			}
			return -1;
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return -1;
		}
	}

	public static List<Long> getAllMembers(Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			List<Long> result = new ArrayList<>();
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM discord_accounts");

			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();
			while(resultSet.next())
			{
				result.add(resultSet.getLong(1));
			}
			return result;
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return Collections.emptyList();
		}
	}


	public static void removeCode(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("UPDATE discord_2fa SET code = null, current_status = ? WHERE uuid = ?");
			TwoFactorState twoFactorState = getTwoFAStatus(userId, minecraft);

			if(twoFactorState == TwoFactorState.ACCEPTED)
			{
				statement.setString(1, "accepted");
			}
			else
			{
				statement.setString(1, "expired");
			}
			statement.setString(2, getUUID(userId, minecraft));
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void addCode(String userId, String code, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("UPDATE discord_2fa SET code = ? WHERE uuid = ?");
			statement.setString(1, code);
			statement.setString(2, getUUID(userId, minecraft));
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static String getUserId(String uuid, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT id FROM linked_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1);
			}
			return "";
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return "";
		}
	}

	public static String getUUID(String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM linked_accounts WHERE id = ?");
			statement.setString(1, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				return resultSet.getString(1);
			}
			return "";
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return "";
		}
	}

	public static TwoFactorState getTwoFAStatus(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM discord_2fa WHERE uuid = ?");
			statement.setString(1, getUUID(userId, minecraft));
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return switch(resultSet.getString(1).toUpperCase())
					{
						case "ACCEPTED" -> TwoFactorState.valueOf("ACCEPTED");
						case "EXPIRED" -> TwoFactorState.valueOf("EXPIRED");
						case "PENDING" -> TwoFactorState.valueOf("PENDING");
						default -> TwoFactorState.valueOf("UNKNOWN");
					};
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static List<Link> getLinks(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM linked_accounts WHERE id = ?");
			List<Link> result = new ArrayList<>();
			statement.setString(1, userId);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			while(resultSet.next())
			{
				result.add(new Link(resultSet.getString(3), resultSet.getString(2), switch(resultSet.getString(4).toUpperCase())
						{
							case "MWAIT" -> LinkState.valueOf("MWAIT");
							case "DWAIT" -> LinkState.valueOf("DWAIT");
							case "LINKED" -> LinkState.valueOf("LINKED");
							default -> LinkState.valueOf("UNKNOWN");
						}));
			}
			return result;

		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return Collections.emptyList();
		}
	}

	public static String getName(String uuid, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT username FROM mc_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return resultSet.getString(1);
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static String getUUIDByName(String name, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM mc_accounts WHERE username = ?");
			statement.setString(1, name);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return resultSet.getString(1);
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static void removeMember(long memberId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement discordAccounts = connection.prepareStatement("DELETE FROM discord_accounts WHERE id = ?");
			PreparedStatement discord2FA = connection.prepareStatement("DELETE FROM discord_2fa WHERE uuid = ?");
			PreparedStatement linkedAccounts = connection.prepareStatement("DELETE FROM linked_accounts WHERE id = ?");

			discordAccounts.setLong(1, memberId);
			discord2FA.setString(1, getUUID(String.valueOf(memberId), minecraft));
			linkedAccounts.setLong(1, memberId);

			discordAccounts.executeUpdate();
			discord2FA.executeUpdate();
			linkedAccounts.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static String prettyPrintLinkState(LinkState state)
	{
		return switch(state)
				{
					case MWAIT -> "Waiting on Minecraft";
					case DWAIT -> "Waiting on Discord";
					case LINKED -> "Linked!";
					default -> "Status missing or corrupted";
				};
	}

	public static void insertLink(String uuid, String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("INSERT INTO linked_accounts (uuid, id, current_status) VALUES (?,?,?)");
			statement.setString(1, uuid);
			statement.setString(2, id);
			statement.setString(3, LinkState.MWAIT.toString().toLowerCase());
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void removeLink(String uuid, String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("DELETE FROM linked_accounts WHERE uuid = ? AND id = ?");
			statement.setString(1, uuid);
			statement.setString(2, id);
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void updateLink(String uuid, String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement update = connection.prepareStatement("UPDATE linked_accounts SET current_status = ? WHERE uuid = ? AND id = ?");
			PreparedStatement clear = connection.prepareStatement("DELETE FROM linked_accounts WHERE current_status != ? AND id = ?");
			update.setString(1, LinkState.LINKED.toString().toLowerCase());
			update.setString(2, uuid);
			update.setString(3, id);

			clear.setString(1, LinkState.LINKED.toString().toLowerCase());
			clear.setString(2, id);

			update.executeUpdate();
			clear.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public enum TwoFactorState
	{
		ACCEPTED,
		EXPIRED,
		PENDING,
		UNKNOWN
	}

	public enum LinkState
	{
		MWAIT,
		DWAIT,
		LINKED,
		UNKNOWN
	}

	public static class Link
	{
		private final String id;
		private final String uuid;
		private final LinkState linkState;

		public Link(String id, String uuid, LinkState linkState)
		{
			this.id = id;
			this.uuid = uuid;
			this.linkState = linkState;
		}

		public String getId()
		{
			return id;
		}

		public String getUuid()
		{
			return uuid;
		}

		public LinkState getLinkState()
		{
			return linkState;
		}
	}
}
