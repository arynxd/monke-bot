package me.arynxd.monkebot.commands.commands.fun;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import net.dv8tion.jda.api.EmbedBuilder;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.util.FileUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MockCommand extends Command
{
	public MockCommand()
	{
		super("Mock", "Mocks the specified text.", "[text]");
		addAliases("mock");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;
		if(CommandChecks.argsEmbedCompatible(event, failure)) return;

		InputStream file = FileUtils.getResourceFile("mock.jpg");
		if(file != null)
		{
			event.getChannel().sendFile(file, "mock.jpg").embed(new EmbedBuilder()
					.setTitle(mockText(args))
					.setColor(Constants.IGSQ_PURPLE)
					.setImage("attachment://mock.jpg")
					.setTimestamp(Instant.now())
					.build()).queue();
		}
		else
		{
			failure.accept(new CommandResultException("An error occurred while loading the mock image."));
		}
	}

	private String mockText(List<String> args)
	{
		StringBuilder mockText = new StringBuilder();
		Random random = new Random();
		mockText.append('"');
		args.forEach(word ->
		{
			for(String selectedChar : word.split(""))
			{
				mockText.append(random.nextBoolean() ? selectedChar.toUpperCase() : selectedChar.toLowerCase());
			}
			mockText.append(" ");
		});
		mockText.deleteCharAt(mockText.lastIndexOf(" "));
		mockText.append('"');
		return mockText.toString();
	}
}
