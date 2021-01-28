package me.arynxd.monkebot.commands.commands.developer;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandFlag;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.util.CommandChecks;
import me.arynxd.monkebot.entities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")

public class EvalCommand extends Command
{
	private static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("groovy");
	private static final List<String> DEFAULT_IMPORTS = List.of("net.dv8tion.jda.api.entities.impl", "net.dv8tion.jda.api.managers", "net.dv8tion.jda.api.entities", "net.dv8tion.jda.api",
			"java.io", "java.math", "java.util", "java.util.concurrent", "java.time", "java.util.stream");

	private static final ExecutorService EVAL_EXECUTOR = Executors.newSingleThreadExecutor();

	public EvalCommand()
	{
		super("Eval", "Evaluates Java code", "[code]");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.DEVELOPER_ONLY);
		addAliases("eval", "evaluate", "code");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		Future<?> evalTask = EVAL_EXECUTOR.submit(() ->
		{
			Object out;
			String status = "Success";

			if(event.isFromGuild())
			{
				SCRIPT_ENGINE.put("guild", event.getGuild());
				SCRIPT_ENGINE.put("member", event.getMember());
			}

			SCRIPT_ENGINE.put("ctx", event);
			SCRIPT_ENGINE.put("message", event.getMessage());
			SCRIPT_ENGINE.put("channel", event.getChannel());
			SCRIPT_ENGINE.put("args", event.getArgs());
			SCRIPT_ENGINE.put("jda", event.getJDA());
			SCRIPT_ENGINE.put("author", event.getAuthor());

			StringBuilder imports = new StringBuilder();
			DEFAULT_IMPORTS.forEach(imp -> imports.append("import ").append(imp).append(".*; "));
			String code = String.join(" ", event.getArgs());
			long start = System.currentTimeMillis();

			try
			{
				out = SCRIPT_ENGINE.eval(imports + code);
			}
			catch(Exception exception)
			{
				out = exception.getMessage();
				status = "Failed";
			}

			event.sendMessage(new EmbedBuilder()
					.setTitle("Evaluated Result")
					.addField("Status:", status, true)
					.addField("Duration:", (System.currentTimeMillis() - start) + "ms", true)
					.addField("Code:", "```java\n" + code + "\n```", false)
					.addField("Result:", out == null ? "No result." : out.toString(), false));
		});

		event.getMonke().getTaskHandler().addTask(() ->
		{
			if(!evalTask.isDone())
			{
				event.replyError("Eval task expired, did you enter an infinite loop?");
			}
			evalTask.cancel(true);
		}, TimeUnit.SECONDS, 5);
	}
}

