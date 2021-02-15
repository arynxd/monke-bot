package me.arynxd.monkebot.objects.database;

import java.sql.Connection;
import me.arynxd.monkebot.Monke;
import me.arynxd.monkebot.objects.jooq.Tables;

import static me.arynxd.monkebot.objects.jooq.tables.Reports.REPORTS;

public class Report
{
	private final long messageId;
	private final long commandMessageId;
	private final long channelId;
	private final long guildId;
	private final long reportedUserId;
	private final String reason;
	private final Monke monke;
	private final long reportingUserId;

	private Report(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reportingUserId, String reason, Monke monke)
	{
		this.messageId = messageId;
		this.commandMessageId = commandMessageId;
		this.channelId = channelId;
		this.guildId = guildId;
		this.reportedUserId = reportedUserId;
		this.reportingUserId = reportingUserId;
		this.reason = reason;
		this.monke = monke;
	}

	public static Report getById(long messageId, Monke monke)
	{
		try (Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(REPORTS).where(REPORTS.MESSAGE_ID.eq(messageId));
			var result = query.fetch();
			query.close();

			if (!result.isEmpty())
			{
				var report = result.get(0);
				return new Report(report.getMessageId(), report.getReportMessageId(), report.getChannelId(), report.getGuildId(), report.getReporterId(), report.getReportteeId(), report.getReportText(), monke);
			}
			else
			{
				return null;
			}
		}
		catch (Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	public static void add(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reportingUserId, String reason, Monke monke)
	{
		try (Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var ctx = monke.getDatabaseHandler().getContext(connection);

			ctx.insertInto(REPORTS)
					.columns(REPORTS.MESSAGE_ID, REPORTS.REPORT_MESSAGE_ID, REPORTS.CHANNEL_ID, REPORTS.GUILD_ID, REPORTS.REPORTER_ID, REPORTS.REPORTTEE_ID, REPORTS.REPORT_TEXT)
					.values(messageId, commandMessageId, channelId, guildId, reportedUserId, reportingUserId, reason)
					.execute();
		}
		catch (Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void remove(long messageId, Monke monke)
	{
		try (Connection connection = monke.getDatabaseHandler().getConnection())
		{
			var context = monke.getDatabaseHandler().getContext(connection);
			context.deleteFrom(Tables.REPORTS).where(REPORTS.MESSAGE_ID.eq(messageId)).execute();
		}
		catch (Exception exception)
		{
			monke.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getReportingUserId()
	{
		return reportingUserId;
	}

	public long getMessageId()
	{
		return messageId;
	}

	public long getCommandMessageId()
	{
		return commandMessageId;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public long getGuildId()
	{
		return guildId;
	}

	public long getReportedUserId()
	{
		return reportedUserId;
	}

	public String getReason()
	{
		return reason;
	}

	public Monke getIgsqBot()
	{
		return monke;
	}
}
