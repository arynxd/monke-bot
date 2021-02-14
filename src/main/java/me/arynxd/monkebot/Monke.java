package me.arynxd.monkebot;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import me.arynxd.monkebot.objects.bot.ConfigOption;
import me.arynxd.monkebot.objects.bot.Configuration;
import me.arynxd.monkebot.objects.bot.EventWaiter;
import me.arynxd.monkebot.objects.database.Tempban;
import me.arynxd.monkebot.objects.database.Vote;
import me.arynxd.monkebot.objects.info.BotInfo;
import me.arynxd.monkebot.events.command.ReportCommandReactionAdd;
import me.arynxd.monkebot.events.logging.MemberEventsLogging;
import me.arynxd.monkebot.events.logging.MessageEventsLogging;
import me.arynxd.monkebot.events.logging.VoiceEventsLogging;
import me.arynxd.monkebot.events.main.GuildEventsMain;
import me.arynxd.monkebot.events.main.MessageEventsMain;
import me.arynxd.monkebot.handlers.*;
import me.arynxd.monkebot.util.DatabaseUtils;
import me.arynxd.monkebot.util.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.OnlineStatus;
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
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monke extends ListenerAdapter
{
	private final DatabaseHandler databaseHandler;
	private final CommandHandler commandHandler;
	private final LocalDateTime startTimestamp;
	private final Configuration configuration;
	private final MusicHandler musicHandler;
	private final OkHttpClient okHttpClient;
	private final TaskHandler taskHandler;
	private final EventWaiter eventWaiter;
	private final WebHandler webHandler;
	private final Logger logger;
	private ShardManager shardManager;

	public Monke()
	{
		this.logger = LoggerFactory.getLogger(Monke.class);
		this.configuration = new Configuration(this);
		this.databaseHandler = new DatabaseHandler(this);
		this.commandHandler = new CommandHandler(this);
		this.startTimestamp = LocalDateTime.now();
		this.taskHandler = new TaskHandler();
		this.eventWaiter = new EventWaiter();
		this.okHttpClient = new OkHttpClient();
		this.musicHandler = new MusicHandler(this);
		this.webHandler = new WebHandler(this);
	}

	public WebHandler getWebHandler()
	{
		return webHandler;
	}

	public OkHttpClient getOkHttpClient()
	{
		return okHttpClient;
	}

	public MusicHandler getMusicHandler()
	{
		return musicHandler;
	}

	public EventWaiter getEventWaiter()
	{
		return eventWaiter;
	}

	public void build() throws LoginException
	{

		this.shardManager = DefaultShardManagerBuilder
				.create(getConfiguration().getString(ConfigOption.TOKEN),
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

				.setHttpClient(okHttpClient)

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
				.setActivity(Activity.playing(" loading."))
				.setStatus(OnlineStatus.DO_NOT_DISTURB)
				.build();
	}

	@Override
	public void onReady(ReadyEvent event)
	{
		registerGuilds(event.getJDA().getShardManager());
		switchStatus(event.getJDA());

		getLogger().info("  ___      _     ___ _            _          _ _ ");
		getLogger().info(" | _ ) ___| |_  / __| |_ __ _ _ _| |_ ___ __| | |");
		getLogger().info(" | _ \\/ _ \\  _| \\__ \\  _/ _` | '_|  _/ -_) _` |_|");
		getLogger().info(" |___/\\___/\\__| |___/\\__\\__,_|_|  \\__\\___\\__,_(_)");
		getLogger().info("");
		getLogger().info("Account:         " + event.getJDA().getSelfUser().getAsTag() + " / " + event.getJDA().getSelfUser().getId());
		getLogger().info("Total Shards:    " + BotInfo.getTotalShards(event.getJDA().getShardManager()));
		getLogger().info("Total Guilds:    " + BotInfo.getGuildCount(event.getJDA().getShardManager()));
		getLogger().info("JDA Version:     " + JDAInfo.VERSION);
		getLogger().info("Monke Version:   " + Constants.VERSION);
		getLogger().info("JVM Version:     " + BotInfo.getJavaVersion());

		getTaskHandler().addRepeatingTask(() ->
		{
			DatabaseUtils.getExpiredTempbans(this).forEach(tempban -> Tempban.remove(tempban.getUserId(), this));
			DatabaseUtils.getExpiredVotes(this).forEach(vote -> Vote.closeById(vote.getVoteId(), vote.getGuildId(), this));
		}, TimeUnit.SECONDS, 15);

		getTaskHandler().addRepeatingTask(() -> switchStatus(event.getJDA()), TimeUnit.MINUTES, 2);
	}

	public SelfUser getSelfUser()
	{
		if(getJDA() == null)
		{
			throw new UnsupportedOperationException("No JDA present.");
		}
		return getJDA() .getSelfUser();
	}

	public JDA getJDA()
	{
		return shardManager.getShardCache().stream().filter(Objects::nonNull).findFirst().orElse(null);
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

	private void switchStatus(JDA jda)
	{
		byte[] array = new byte[10];
		new Random().nextBytes(array);

		String generatedString = new String(array, StandardCharsets.UTF_8);
		ShardManager manager = jda.getShardManager();

		if(manager == null)
		{
			return;
		}

		List<Activity> status = List.of(
				Activity.watching(BotInfo.getGuildCount(manager) + " guild" + StringUtils.plurify((int) BotInfo.getGuildCount(manager))),
				Activity.watching(BotInfo.getUserCount(manager) + " user" + StringUtils.plurify((int) BotInfo.getUserCount(manager))),
				Activity.listening("your commands"),
				Activity.playing(generatedString),
				Activity.playing("forknife!!!!")
		);

		jda.getPresence().setPresence(OnlineStatus.ONLINE, status.get(new Random().nextInt(status.size() - 1)));
	}

	public LocalDateTime getStartTimestamp()
	{
		return this.startTimestamp;
	}

	public ShardManager getShardManager()
	{
		if(this.shardManager == null)
		{
			throw new UnsupportedOperationException("Shardmanager is not built.");
		}
		return this.shardManager;
	}

	public Configuration getConfiguration()
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
