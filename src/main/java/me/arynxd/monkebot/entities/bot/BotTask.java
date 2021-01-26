package me.arynxd.monkebot.entities.bot;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

/**
 * A class representing a managed task in the {@link me.arynxd.monkebot.Monke bot's} {@link me.arynxd.monkebot.handlers.TaskHandler TaskHandler}.
 */
public class BotTask
{
	private final ScheduledFuture<?> task;
	private final String name;
	private final long expiresAt;
	private final TimeUnit unit;

	/**
	 * Constructs a new {@link BotTask managed task} for use in the {@link me.arynxd.monkebot.handlers.TaskHandler Task Handler}.
	 *
	 * @param task      The {@link java.util.concurrent.ScheduledFuture future}d to be managed.
	 * @param name      The task name.
	 * @param expiresAt The task's expiry time.
	 * @param timeUnit  The {@link java.util.concurrent.TimeUnit unit} of the expiry time.
	 */
	public BotTask(@Nonnull ScheduledFuture<?> task, @Nonnull String name, @Nonnull Long expiresAt, @Nonnull TimeUnit timeUnit)
	{
		this.task = task;
		this.name = name;
		this.expiresAt = expiresAt;
		this.unit = timeUnit;
	}

	/**
	 * @return The {@link java.util.concurrent.TimeUnit unit} for this {@link BotTask task}.
	 */
	@Nonnull
	public TimeUnit getUnit()
	{
		return unit;
	}

	/**
	 * @return The {@link java.util.concurrent.ScheduledFuture future} for this {@link BotTask task}.
	 */
	@Nonnull
	public ScheduledFuture<?> getTask()
	{
		return task;
	}

	/**
	 * @return The name of this {@link BotTask task}, could be a UUID.
	 */
	@Nonnull
	public String getName()
	{
		return name;
	}

	/**
	 * @return The expiry time of this {@link BotTask task}. Never null.
	 */
	@Nonnull
	public Long getExpiresAt()
	{
		return expiresAt;
	}

	/**
	 * Cancels this task.
	 *
	 * @param shouldInterrupt Should interrupt the {@link BotTask task's} execution
	 */
	public void cancel(boolean shouldInterrupt)
	{
		task.cancel(shouldInterrupt);
	}
}
