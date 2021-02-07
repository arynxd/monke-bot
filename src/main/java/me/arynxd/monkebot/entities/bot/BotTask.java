package me.arynxd.monkebot.entities.bot;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

public class BotTask
{
	private final ScheduledFuture<?> task;
	private final String name;
	private final long expiresAt;
	private final TimeUnit unit;

	public BotTask(@Nonnull ScheduledFuture<?> task, @Nonnull String name, @Nonnull Long expiresAt, @Nonnull TimeUnit timeUnit)
	{
		this.task = task;
		this.name = name;
		this.expiresAt = expiresAt;
		this.unit = timeUnit;
	}

	public @Nonnull
	TimeUnit getUnit()
	{
		return unit;
	}

	public @Nonnull
	ScheduledFuture<?> getTask()
	{
		return task;
	}

	public @Nonnull
	String getName()
	{
		return name;
	}

	public @Nonnull
	Long getExpiresAt()
	{
		return expiresAt;
	}

	public void cancel(boolean shouldInterrupt)
	{
		task.cancel(shouldInterrupt);
	}
}
