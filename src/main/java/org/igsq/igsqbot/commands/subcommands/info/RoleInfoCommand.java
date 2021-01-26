package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.info.RoleInfo;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.jetbrains.annotations.NotNull;

public class RoleInfoCommand extends Command
{
	public RoleInfoCommand(Command parent)
	{
		super(parent, "role", "Shows information about a role.", "[role]");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(event);
		}
		else
		{
			new Parser(ArrayUtils.arrayCompile(args.subList(0, args.size()), " "), event).parseAsRole(role ->
			{
				RoleInfo roleInfo = new RoleInfo(role);

				roleInfo.getMembers().onSuccess(members ->
				{
					int size = members.size();
					StringBuilder text = new StringBuilder();

					if(size > 5)
					{
						members = members.subList(0, 5);
					}

					members.forEach(member -> text.append(member.getAsMention()).append(" "));

					event.sendMessage(new EmbedBuilder()
							.setTitle("Information for role **" + role.getName() + "** (" + size + " Members)")
							.addField("Random members", text.length() == 0 ? "No members" : text.toString(), false));
				});
			});
		}
	}
}
