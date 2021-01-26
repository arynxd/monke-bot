package org.igsq.igsqbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.igsq.igsqbot.entities.bot.ConfigOption;
import org.igsq.igsqbot.entities.bot.Configuration;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.database.Tempban;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.entities.info.BotInfo;
import org.igsq.igsqbot.events.command.ReportCommandReactionAdd;
import org.igsq.igsqbot.events.logging.MemberEventsLogging;
import org.igsq.igsqbot.events.logging.MessageEventsLogging;
import org.igsq.igsqbot.events.logging.VoiceEventsLogging;
import org.igsq.igsqbot.events.main.GuildEventsMain;
import org.igsq.igsqbot.events.main.MessageEventsMain;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.DatabaseHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IGSQBot extends ListenerAdapter
{
	private final DatabaseHandler databaseHandler;
	private final CommandHandler commandHandler;
	private final LocalDateTime startTimestamp;
	private final List<EmbedBuilder> helpPages;
	private final Configuration configuration;
	private final TaskHandler taskHandler;
	private final EventWaiter eventWaiter;
	private final Logger logger;
	private ShardManager shardManager;
	private Minecraft minecraft;
	private JDA jda;

	public IGSQBot()
	{
		this.logger = LoggerFactory.getLogger(IGSQBot.class);
		this.configuration = new Configuration(this);
		this.databaseHandler = new DatabaseHandler(this);
		this.commandHandler = new CommandHandler(this);
		this.startTimestamp = LocalDateTime.now();
		this.helpPages = new ArrayList<>();
		this.taskHandler = new TaskHandler();
		this.eventWaiter = new EventWaiter();
	}

	public EventWaiter getEventWaiter()
	{
		return eventWaiter;
	}

	public void build() throws LoginException
	{
		this.shardManager = DefaultShardManagerBuilder
				.create(getConfig().getString(ConfigOption.TOKEN),
						GatewayIntent.GUILD_MEMBERS,

						GatewayIntent.DIRECT_MESSAGES,
						GatewayIntent.DIRECT_MESSAGE_REACTIONS,

						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MESSAGE_REACTIONS,
						GatewayIntent.GUILD_VOICE_STATES)

				.disableCache(
						CacheFlag.ACTIVITY,
						CacheFlag.EMOTE,
						CacheFlag.CLIENT_STATUS,
						CacheFlag.ROLE_TAGS,
						CacheFlag.MEMBER_OVERRIDES)

				.setMemberCachePolicy(MemberCachePolicy.NONE)
				.setShardsTotal(-1)

				.addEventListeners(
						this,
						eventWaiter,

						new MessageEventsMain(this),
						new GuildEventsMain(this),

						new ReportCommandReactionAdd(this),

						new VoiceEventsLogging(this),
						new MessageEventsLogging(this),
						new MemberEventsLogging(this)
				)

				.setActivity(Activity.listening(" your commands."))
				.build();
	}

	@Override
	public void onReady(ReadyEvent event)
	{
		this.jda = event.getJDA();
		this.minecraft = new Minecraft(this);
		getStartTimestamp();

		registerGuilds(event.getJDA().getShardManager());

		getLogger().info("  ___ ___ ___  ___  ___      _     ___ _            _          _ ");
		getLogger().info(" |_ _/ __/ __|/ _ \\| _ ) ___| |_  / __| |_ __ _ _ _| |_ ___ __| |");
		getLogger().info("  | | (_ \\__ \\ (_) | _ \\/ _ \\  _| \\__ \\  _/ _` | '_|  _/ -_) _` |");
		getLogger().info(" |___\\___|___/\\__\\_\\___/\\___/\\__| |___/\\__\\__,_|_|  \\__\\___\\__,_|");
		getLogger().info("");
		getLogger().info("Account:         " + event.getJDA().getSelfUser().getAsTag() + " / " + event.getJDA().getSelfUser().getId());
		getLogger().info("Total Shards:    " + BotInfo.getTotalShards(event.getJDA().getShardManager()));
		getLogger().info("Total Guilds:    " + BotInfo.getTotalServers(event.getJDA().getShardManager()));
		getLogger().info("JDA Version:     " + JDAInfo.VERSION);
		getLogger().info("IGSQBot Version: " + Constants.VERSION);
		getLogger().info("JVM Version:     " + BotInfo.getJavaVersion());

		getTaskHandler().addRepeatingTask(() ->
		{
			DatabaseUtils.getExpiredTempbans(this).forEach(tempban -> Tempban.remove(tempban.getUserId(), this));
			DatabaseUtils.getExpiredVotes(this).forEach(vote -> Vote.closeById(vote.getVoteId(), vote.getGuildId(), this));
		}, TimeUnit.SECONDS, 15);
	}

	public SelfUser getSelfUser()
	{
		if(this.jda == null)
		{
			throw new UnsupportedOperationException("No JDA present.");
		}
		return this.jda.getSelfUser();
	}

	public JDA getJDA()
	{
		return this.jda;
	}

	public void registerGuilds(ShardManager shardManager)
	{
		if(shardManager == null)
		{
			throw new UnsupportedOperationException("Cannot register guilds without a shard manager.");
		}
		for(Guild guild : shardManager.getGuilds())
		{
			DatabaseUtils.registerGuild(guild, this);
		}
	}

	public void registerGuilds()
	{
		if(this.shardManager == null)
		{
			throw new UnsupportedOperationException("Cannot register guilds without a shard manager.");
		}
		for(Guild guild : this.shardManager.getGuilds())
		{
			DatabaseUtils.registerGuild(guild, this);
		}
	}

	public LocalDateTime getStartTimestamp()
	{
		return this.startTimestamp;
	}

	public List<EmbedBuilder> getHelpPages()
	{
		if(this.helpPages.isEmpty())
		{
			List<Command> commands = new ArrayList<>();
			for(Command cmd : getCommandHandler().getCommandMap().values())
			{
				if(!commands.contains(cmd))
				{
					commands.add(cmd);
				}
			}

			EmbedBuilder embedBuilder = new EmbedBuilder();
			int fieldCount = 0;
			int page = 1;
			for(int i = 0; i < commands.size(); i++)
			{
				Command cmd = commands.get(i);
				if(fieldCount < 6)
				{
					fieldCount++;
					embedBuilder.setTitle("Help page: " + page);
					embedBuilder.addField(cmd.getName(), cmd.getDescription() + "\n**" + cmd.getAliases().get(0) + "**`" + cmd.getSyntax() + "`", false);
					embedBuilder.setColor(Constants.IGSQ_PURPLE);
					embedBuilder.setFooter("<> Optional;  [] Required; {} Maximum Quantity | ");
				}
				else
				{
					this.helpPages.add(embedBuilder);
					embedBuilder = new EmbedBuilder();
					fieldCount = 0;
					page++;
					i--;
				}
			}
		}
		return this.helpPages;
	}

	public ShardManager getShardManager()
	{
		if(this.shardManager == null)
		{
			throw new UnsupportedOperationException("Shardmanager is not built.");
		}
		return this.shardManager;
	}

	public Minecraft getMinecraft()
	{
		return this.minecraft;
	}

	public Configuration getConfig()
	{
		return this.configuration;
	}

	public CommandHandler getCommandHandler()
	{
		return this.commandHandler;
	}

	public Logger getLogger()
	{
		return this.logger;
	}

	public TaskHandler getTaskHandler()
	{
		return this.taskHandler;
	}

	public DatabaseHandler getDatabaseHandler()
	{
		return this.databaseHandler;
	}
}
