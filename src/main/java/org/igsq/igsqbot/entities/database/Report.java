package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;

import static org.igsq.igsqbot.entities.jooq.tables.Reports.REPORTS;

public class Report
{
	private final long messageId;
	private final long commandMessageId;
	private final long channelId;
	private final long guildId;
	private final long reportedUserId;
	private final String reason;
	private final IGSQBot igsqBot;
	private final long reporteeUserId;

	public long getReporteeUserId()
	{
		return reporteeUserId;
	}

	private Report(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reporteeUserId, String reason, IGSQBot igsqBot)
	{
		this.messageId = messageId;
		this.commandMessageId = commandMessageId;
		this.channelId = channelId;
		this.guildId = guildId;
		this.reportedUserId = reportedUserId;
		this.reporteeUserId = reporteeUserId;
		this.reason = reason;
		this.igsqBot = igsqBot;
	}
	public static Report getById(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(REPORTS).where(REPORTS.MESSAGE_ID.eq(messageId));
			var result = query.fetch();
			query.close();

			if(!result.isEmpty())
			{
				var report = result.get(0);
				return new Report(report.getMessageId(), report.getReportMessageId(), report.getChannelId(), report.getGuildId(), report.getReporterId(),report.getReportteeId(), report.getReportText(), igsqBot);
			}
			else
			{
				return null;
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	public static void add(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, long reporteeUserId, String reason, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);

			ctx.insertInto(REPORTS)
					.columns(REPORTS.MESSAGE_ID, REPORTS.REPORT_MESSAGE_ID, REPORTS.CHANNEL_ID, REPORTS.GUILD_ID, REPORTS.REPORTER_ID, REPORTS.REPORTTEE_ID, REPORTS.REPORT_TEXT)
					.values(messageId, commandMessageId, channelId, guildId, reportedUserId,reporteeUserId, reason)
					.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void remove(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			context.deleteFrom(Tables.REPORTS).where(REPORTS.MESSAGE_ID.eq(messageId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
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

	public IGSQBot getIgsqBot()
	{
		return igsqBot;
	}
}
