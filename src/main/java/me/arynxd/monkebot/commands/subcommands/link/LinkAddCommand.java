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

public class LinkAddCommand extends Command
{
	public LinkAddCommand(Command parent)
	{
		super(parent, "add", "Adds or confirms Minecraft links.", "[mcUsername]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(event, failure)) return;

		User author = event.getAuthor();
		Minecraft minecraft = event.getMonke().getMinecraft();
		String arg = args.get(0);

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + arg + "** does not exist. Please ensure you have played on our server."));
			return;
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + account + "** is already linked."));
			return;
		}


		if(MinecraftChecks.isDuplicate(uuid, author.getId(), minecraft))
		{
			failure.accept(new CommandResultException("You cannot make duplicate link requests."));
			return;
		}
		if(MinecraftChecks.isPendingDiscord(uuid, minecraft))
		{
			MinecraftUtils.updateLink(uuid, author.getId(), minecraft);
			event.replySuccess("Confirmed link for account **" + account + "**");
		}
		if(MinecraftChecks.isUserLinked(author.getId(), minecraft))
		{
			failure.accept(new CommandResultException("You are already linked to an account."));
			return;
		}

		MinecraftUtils.insertLink(uuid, author.getId(), minecraft);
		event.replySuccess("Added link for account **" + account + "** confirm it in Minecraft now.");


	}
}