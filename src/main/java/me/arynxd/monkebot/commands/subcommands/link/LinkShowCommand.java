package me.arynxd.monkebot.commands.subcommands.link;

import java.util.List;
import java.util.function.Consumer;

import me.arynxd.monkebot.entities.command.Command;
import me.arynxd.monkebot.entities.command.CommandEvent;
import me.arynxd.monkebot.entities.exception.CommandException;
import me.arynxd.monkebot.minecraft.Minecraft;
import me.arynxd.monkebot.minecraft.MinecraftUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import me.arynxd.monkebot.Constants;
import me.arynxd.monkebot.util.Parser;
import me.arynxd.monkebot.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class LinkShowCommand extends Command
{
	public LinkShowCommand(Command parent)
	{
		super(parent, "show", "Shows Minecraft links.", "[none]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		Minecraft minecraft = event.getMonke().getMinecraft();

		if(!event.isFromGuild())
		{
			showSelf(MinecraftUtils.getLinks(event.getAuthor().getId(), minecraft), event);
			return;
		}

		if(args.isEmpty())
		{
			showSelf(MinecraftUtils.getLinks(event.getAuthor().getId(), minecraft), event);
			return;
		}

		if(event.memberPermissionCheck(Permission.MESSAGE_MANAGE))
		{
			String arg = args.get(0);
			new Parser(arg, event).parseAsUser(user ->
			{
				List<MinecraftUtils.Link> links = MinecraftUtils.getLinks(user.getId(), minecraft);
				StringBuilder text = new StringBuilder();

				for(MinecraftUtils.Link link : links)
				{
					text
							.append(StringUtils.getUserAsMention(link.getId()))
							.append(" -- ")
							.append(MinecraftUtils.getName(link.getUuid(), minecraft))
							.append(" -- ")
							.append(MinecraftUtils.prettyPrintLinkState(link.getLinkState()))
							.append("\n");
				}

				event.getChannel().sendMessage(new EmbedBuilder()
						.setTitle("Links for user: " + user.getAsTag())
						.setDescription(text.length() == 0 ? "No links found" : text.toString())
						.setColor(Constants.IGSQ_PURPLE)
						.build()).queue();
			});
			return;
		}

		showSelf(MinecraftUtils.getLinks(event.getAuthor().getId(), minecraft), event);
	}

	private void showSelf(List<MinecraftUtils.Link> links, CommandEvent ctx)
	{
		StringBuilder text = new StringBuilder();
		Minecraft minecraft = ctx.getMonke().getMinecraft();
		for(MinecraftUtils.Link link : links)
		{
			text
					.append(StringUtils.getUserAsMention(link.getId()))
					.append(" -- ")
					.append(MinecraftUtils.getName(link.getUuid(), minecraft))
					.append(" -- ")
					.append(MinecraftUtils.prettyPrintLinkState(link.getLinkState()))
					.append("\n");
		}

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("Links for user: " + ctx.getAuthor().getAsTag())
				.setDescription(text.length() == 0 ? "No links found" : text.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}
}
