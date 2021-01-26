package me.arynxd.monkebot.commands.subcommands.link;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.entities.exception.CommandResultException;
import me.arynxd.monkebot.minecraft.Minecraft;
import me.arynxd.monkebot.minecraft.MinecraftChecks;
import me.arynxd.monkebot.minecraft.MinecraftUtils;
import net.dv8tion.jda.api.entities.User;
import me.arynxd.monkebot.util.CommandChecks;
import org.jetbrains.annotations.NotNull;

public class LinkRemoveCommand extends Command
{
	public LinkRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes Minecraft links.", "[mcUsername]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		String arg = args.get(0);
		User author = event.getAuthor();
		Minecraft minecraft = event.getMonke().getMinecraft();

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + arg + "** does not exist. Please ensure you have played on our server."));
			return;
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(!MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + account + "** is not linked."));
			return;
		}

		if(!MinecraftChecks.isOwnerOfAccount(uuid, author.getId(), minecraft))
		{
			failure.accept(new CommandResultException("Account **" + account + "** does not belong to you."));
			return;
		}


		MinecraftUtils.removeLink(uuid, author.getId(), minecraft);
		event.replySuccess("Removed link **" + account + "**");


	}
}
