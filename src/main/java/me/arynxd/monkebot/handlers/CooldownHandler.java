package me.arynxd.monkebot.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.arynxd.monkebot.entities.command.Command;
import net.dv8tion.jda.api.entities.Member;

public abstract class CooldownHandler
{
	private static final Map<CooledCommand, Long> COOLDOWN_MAP = new ConcurrentHashMap<>(); //K = userId, guildId, command V = timestamp

	private CooldownHandler() //TODO: Remove shit from the map
	{
		//Overrides the default, public, constructor
	}

	public static boolean isOnCooldown(Member member, Command command)
	{
		long userId = member.getIdLong();
		long guildId = member.getGuild().getIdLong();
		for(Map.Entry<CooledCommand, Long> entry : COOLDOWN_MAP.entrySet())
		{
			CooledCommand cooledCommand = entry.getKey();
			long expiry = entry.getValue();

			if(cooledCommand.getUserId() == userId && cooledCommand.getGuildId() == guildId && cooledCommand.getCommand().equals(command))
			{
				return System.currentTimeMillis() <= expiry;
			}
		}
		return false;
	}

	public static void addCooldown(Member member, Command command)
	{
		COOLDOWN_MAP.put(new CooledCommand(member, command), System.currentTimeMillis() + command.getCooldown());
	}

	public static class CooledCommand
	{
		private final long userId;
		private final long guildId;
		private final Command command;

		public CooledCommand(Member member, Command command)
		{
			this.userId = member.getIdLong();
			this.guildId = member.getGuild().getIdLong();
			this.command = command;
		}

		public long getUserId()
		{
			return userId;
		}

		public long getGuildId()
		{
			return guildId;
		}

		public Command getCommand()
		{
			return command;
		}
	}
}
