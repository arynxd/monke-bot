package org.igsq.igsqbot.minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.bot.ConfigOption;
import org.igsq.igsqbot.entities.bot.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftSync
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftSync.class);
	private final Minecraft minecraft;
	private final IGSQBot igsqbot;

	public MinecraftSync(Minecraft minecraft)
	{
		LOGGER.debug("Minecraft sync started.");
		this.minecraft = minecraft;
		this.igsqbot = minecraft.getIGSQBot();
	}

	public void start()
	{
		if(minecraft.getDatabaseHandler().isOffline())
		{
			igsqbot.getLogger().warn("Minecraft syncing stopped. Database not online.");
		}
		else
		{
			LOGGER.debug("Starting initial Sync");
			initialSync();
		}
	}

	public void syncMember(Member newMember)
	{
		if(newMember.getUser().isBot())
		{
			return;
		}
		LOGGER.debug("Syncing Member " + newMember.getId());
		List<String> ranks = newMember.getRoles().stream()
				.map(Role::getId)
				.filter(role -> getRanks().containsKey(role))
				.map(role -> getRanks().get(role))
				.collect(Collectors.toList());

		if(ranks.isEmpty())
		{
			ranks.add("group.default");
		}

		Configuration configuration = igsqbot.getConfig();
		MinecraftUser minecraftUser = new MinecraftUser();
		minecraftUser.setId(newMember.getId());
		minecraftUser.setUsername(newMember.getUser().getAsTag());
		minecraftUser.setNickname(newMember.getEffectiveName());
		minecraftUser.setRole(ranks.get(0));

		minecraftUser.setFounder(hasRole(configuration.getString(ConfigOption.FOUNDER).split(","), newMember));
		minecraftUser.setBirthday(hasRole(configuration.getString(ConfigOption.BIRTHDAY).split(","), newMember));
		minecraftUser.setNitroboost(hasRole(configuration.getString(ConfigOption.NITROBOOST).split(","), newMember));
		minecraftUser.setSupporter(hasRole(configuration.getString(ConfigOption.SUPPORTER).split(","), newMember));
		minecraftUser.setDeveloper(hasRole(configuration.getString(ConfigOption.DEVELOPER).split(","), newMember));

		int isPresent = MinecraftUtils.isMemberPresent(minecraftUser, minecraft);
		if(isPresent == -1)
		{
			return;
		}

		if(isPresent == 1)
		{
			MinecraftUtils.updateMember(minecraftUser, minecraft);
		}
		else
		{
			MinecraftUtils.insertMember(minecraftUser, minecraft);
		}

		LOGGER.debug("Member " + newMember.getId() + " Syncing complete.");
	}

	public void removeMember(long memberId)
	{
		MinecraftUtils.removeMember(memberId, minecraft);
	}

	private void initialSync()
	{
		Guild guild = igsqbot.getShardManager().getGuildById(igsqbot.getConfig().getString(ConfigOption.HOMESERVER));

		if(guild == null)
		{
			close("Homeserver was null.");
		}
		else
		{
			guild.loadMembers().onSuccess(
			members ->
			{
				members.forEach(
				member ->
				{
					if(member.getUser().isBot())
					{
						return;
					}
					syncMember(member);
				});
				cleanMembers(members);
			});
		}
	}

	private void cleanMembers(List<Member> members)
	{
		List<Long> inDB = MinecraftUtils.getAllMembers(minecraft);
		List<Long> inGuild = members.stream().map(Member::getIdLong).collect(Collectors.toList());

		for(long id : inDB)
		{
			if(!inGuild.contains(id))
			{
				removeMember(id);
			}
		}
	}

	private int hasRole(String role, Member member)
	{
		return member.getRoles().stream().map(Role::getId).filter(role::equals).count() == 1 ? 1 : 0;
	}

	private int hasRole(String[] roles, Member member)
	{
		for(String role : roles)
		{
			if(hasRole(role, member) == 1)
			{
				return 1;
			}
		}
		return 0;
	}

	private Map<String, String> getRanks()
	{
		Map<String, String> result = new HashMap<>();
		for(ConfigOption configOption : ConfigOption.getRanks())
		{
			try
			{
				for(String rank : igsqbot.getConfig().getString(configOption).split(","))
				{
					result.put(rank, configOption.getKey());
				}
			}
			catch(Exception exception)
			{
				close("An error occurred while loading the ranks.");
			}
		}
		return result;
	}

	public void close()
	{
		igsqbot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqbot.getLogger().info("Minecraft syncing stopped.");
		LOGGER.debug("Minecraft syncing closed.");
	}

	public void close(String message)
	{
		igsqbot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqbot.getLogger().info("Minecraft syncing stopped. " + message);
		LOGGER.debug("Minecraft syncing closed.");
	}
}
