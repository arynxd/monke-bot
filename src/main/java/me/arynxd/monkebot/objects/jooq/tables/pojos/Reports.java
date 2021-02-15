/*
 * This file is generated by jOOQ.
 */
package me.arynxd.monkebot.objects.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings ({"all", "unchecked", "rawtypes"})
public class Reports implements Serializable
{

	private static final long serialVersionUID = 1L;

	private final Long id;
	private final Long messageId;
	private final Long reportMessageId;
	private final Long channelId;
	private final Long guildId;
	private final Long reporterId;
	private final Long reportteeId;
	private final LocalDateTime timestamp;
	private final String reportText;

	public Reports(Reports value)
	{
		this.id = value.id;
		this.messageId = value.messageId;
		this.reportMessageId = value.reportMessageId;
		this.channelId = value.channelId;
		this.guildId = value.guildId;
		this.reporterId = value.reporterId;
		this.reportteeId = value.reportteeId;
		this.timestamp = value.timestamp;
		this.reportText = value.reportText;
	}

	public Reports(
			Long id,
			Long messageId,
			Long reportMessageId,
			Long channelId,
			Long guildId,
			Long reporterId,
			Long reportteeId,
			LocalDateTime timestamp,
			String reportText
	)
	{
		this.id = id;
		this.messageId = messageId;
		this.reportMessageId = reportMessageId;
		this.channelId = channelId;
		this.guildId = guildId;
		this.reporterId = reporterId;
		this.reportteeId = reportteeId;
		this.timestamp = timestamp;
		this.reportText = reportText;
	}

	/**
	 * Getter for <code>public.reports.id</code>.
	 */
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Getter for <code>public.reports.message_id</code>.
	 */
	public Long getMessageId()
	{
		return this.messageId;
	}

	/**
	 * Getter for <code>public.reports.report_message_id</code>.
	 */
	public Long getReportMessageId()
	{
		return this.reportMessageId;
	}

	/**
	 * Getter for <code>public.reports.channel_id</code>.
	 */
	public Long getChannelId()
	{
		return this.channelId;
	}

	/**
	 * Getter for <code>public.reports.guild_id</code>.
	 */
	public Long getGuildId()
	{
		return this.guildId;
	}

	/**
	 * Getter for <code>public.reports.reporter_id</code>.
	 */
	public Long getReporterId()
	{
		return this.reporterId;
	}

	/**
	 * Getter for <code>public.reports.reporttee_id</code>.
	 */
	public Long getReportteeId()
	{
		return this.reportteeId;
	}

	/**
	 * Getter for <code>public.reports.timestamp</code>.
	 */
	public LocalDateTime getTimestamp()
	{
		return this.timestamp;
	}

	/**
	 * Getter for <code>public.reports.report_text</code>.
	 */
	public String getReportText()
	{
		return this.reportText;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("Reports (");

		sb.append(id);
		sb.append(", ").append(messageId);
		sb.append(", ").append(reportMessageId);
		sb.append(", ").append(channelId);
		sb.append(", ").append(guildId);
		sb.append(", ").append(reporterId);
		sb.append(", ").append(reportteeId);
		sb.append(", ").append(timestamp);
		sb.append(", ").append(reportText);

		sb.append(")");
		return sb.toString();
	}
}
