package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.EmbedUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class MessageCommand extends Command
{
	public MessageCommand()
	{
		super("Message", "Sends default embed messages.", "[name]");
		addFlags(CommandFlag.DEVELOPER_ONLY);
		addAliases("message");
	}

	@Override
	public void run(@NotNull List<String> args, @NotNull CommandEvent event, @NotNull Consumer<CommandException> failure)
	{
		EmbedBuilder PUNISHMENT = new EmbedBuilder()
				.setTitle("**Punishment System**:")
				.setDescription("\n" +
						"Anyone found to be breaking any of the Guidelines above or doing\n" +
						"anything the staff deems rule-breaking **will** be put onto the disciplinary track.\n" +
						"\n" +
						"The disciplinary track is a way for you to know when you are doing something wrong, and the associated punishments.\n" +
						"\n" +
						"Every time you break the guidelines, **one (1) mark** will be added to your record.\n" +
						"\n" +
						"For every mark you go up the Disciplinary Track, the punishments will get more severe.\n" +
						"\n" +
						"**Disciplinary Track**:\n" +
						":one: Warning\n" +
						":two: Warning\n" +
						":three: Tempban (1 Day)\n" +
						":four: Tempban (3 Days)\n" +
						":five: Tempban (7 Days)\n" +
						":six: Permanent Ban\n" +
						"\n" +
						"**Recovery**:\n" +
						"Have you changed since you broke a guideline?\n" +
						"Don't worry, we understand people can change and be better.\n" +
						"\n" +
						"After **2 months** of not breaking any of the guidelines, you will recover one (1) position from the track;\n" +
						"this will continue every 2 months, until you reach position 0 again.\n" +
						"\n" +
						"**Notes**:\n" +
						"If it is severe enough you **can** receive multiple points at once.\n" +
						"\n" +
						"*User who recently joined the server (within the last hour or so) may have a more severe track if they break specific guidelines (this is to avoid people who joined with malicious intent).*");


		EmbedBuilder FULLRULES = new EmbedBuilder()
				.setTitle("**Welcome to IGSQ™!**")
				.setDescription("\nBelow are the full Guidelines to the Intergalactic Squirrels™ server!\n" +
						"By joining the server, you agree to Discord's Community Guidelines and Terms of Service.\n" +
						"\n" +
						"**Guidelines**:\n" +
						"\u200F\u200F\u200E\n" +
						"• **English only**.\n" +
						"- *Text chat should be in English only.\n" +
						"- Other languages are allowed in Voice Chat if everyone agrees.*\n" +
						"\n" +
						"• **No discrimination & toxic behavior**.\n" +
						"- *Includes homophobia/transphobia/etc., hating on specific groups...\n" +
						"- No promoting/hating religion.*\n" +
						"\n" +
						"• **No NSFW, dark humor, & related content.**\n" +
						"- *Absolutely no NSFW/18+ content allowed.\n" +
						"- Dark/edgy humor is NOT allowed.*\n" +
						"\u200F\u200F\u200E\n" +
						"• **Content cannot break Discord guidelines & TOS.**\n" +
						"\u200F\u200F\u200E- *That means no jokes & depictions of: self-harm; suicide; terrorism; violence; repulsive content (blood/vomit/etc.)\n" +
						"- No racism, targeted harassment, rape jokes & such.*\n" +
						"\n" +
						"• **No spam.**\n" +
						"\n" +
						"• **Keep on the channel's topic.**\n" +
						"- *Sensitive subjects should be discussed in <#702404934594986024>.\n" +
						"- Media belongs in <#613775244582322176> unless it is relevant to the conversation.*\n" +
						"\n" +
						"• **Don't beg.**\n" +
						"\n" +
						"• **No Advertising.**\n" +
						"- *Advertising is only allowed in <#583665055397183516>.\n" +
						"\u200F\u200F\u200E- You must be <@&579888281840386049> or above, as well as send an example of your content to a staff member to earn the ability to send links*");

		EmbedBuilder SHORTRULES = new EmbedBuilder().setTitle("**Welcome to IGSQ™!**")
				.setDescription("Below is a summary of the guidelines for the Intergalactic Squirrels™ server!\n" +
						"By joining the server, you agree to Discord's Community Guidelines and Terms of Service.\n" +
						"\n" +
						"**Summary**:\n" +
						"\u200F\u200F\u200E\n" +
						"• **English only.**\n" +
						"• **No discrimination & toxic behavior.**\n" +
						"• **No NSFW, dark humor, & related content.**\n" +
						"• **Content cannot break Discord guidelines & TOS.**\n" +
						"• **No spam.**\n" +
						"• **Keep on the channel's topic.**\n" +
						"• **Don't beg.**\n" +
						"• **No Advertising.**\n" +
						"\u200F\u200F\u200E\n" +
						"**Note**: This is a summary of our guidelines. More information will be available after verification!");

		EmbedBuilder ROLEINFO = new EmbedBuilder().setTitle("**Roles**:").setDescription("" +
				"<@&558697600375848970> are the administrators of the server.\n" +
				"\n" +
				"<@&742059848983904387> are the moderators of the server.\n" +
				"\n" +
				"<@&701141500029042718> are trusted members of the community that help us keep the server clean.\n" +
				"\n" +
				"<@&798626642125783050> is a unique role given to very few members. It shows continued respect and dedication to the community, so much so that the are given the highest non staff role available.\n" +
				"\n" +
				"<@&798626642125783050> is given to the most elite members of our server. It shows dedication over time to the community as a whole.\n" +
				"\n" +
				"<@&558698050256633868> are some of our most dedicated members. It's granted on an individual basis to people for various reasons.\n" +
				"\n" +
				"<@&636453797723504669> are people that reached MEE6 level 10.\n" +
				"\n" +
				"<@&579888281840386049> are people that reached MEE6 level 6. It grants you the DJ role, giving more options for the music bots.\n" +
				"\n" +
				"<@&558698195878936588> are people that reached MEE6 level 3.\n" +
				"\n" +
				"<@&558698326099492865> are the regular but highly valued members of the community!");

		EmbedBuilder OTHERROLES = new EmbedBuilder()
				.setTitle("**Other Roles | Intergalactic Squirrels™**")
				.setDescription("You can select as many roles as you want!")
				.addField("**Events**", "<@&636487094520250368>", false);

		switch(args.get(0))
		{
			case "verification" -> event.getChannel().sendMessage("This server has a **semi-automatic** verification system. It means the bot will take care of verification, but it will be manually checked by staff first. To give you the correct roles, please tell us in this channel:\n" +
					"\n" +
					Emoji.IGSQ1.getAsChat() + " What country are you from?\n" +
					"\n" +
					Emoji.IGSQ2.getAsChat() + " What game(s) did you join for?\n" +
					"\n" +
					"• By joining our server, you agree to follow our <#767657829401231360>.\n" +
					"\n" +
					"• Below you can see an example of a previous member who just joined the server. **You will be verified as soon as possible.** This is usually in less than a minute, but if nobody is online, it can take a couple hours.").queue();

			case "shortrules" -> event.getChannel().sendMessage(SHORTRULES
					.setColor(Constants.IGSQ_PURPLE)
					.setFooter("© 2020 The Intergalactic Squirrels™")
					.build()).queue();

			case "longrules" -> event.getChannel().sendMessage(FULLRULES
					.setColor(Constants.IGSQ_PURPLE)
					.setFooter("© 2020 The Intergalactic Squirrels™")
					.build()).queue();

			case "punishment" -> event.getChannel().sendMessage(PUNISHMENT
					.setColor(Constants.IGSQ_PURPLE)
					.setFooter("© 2020 The Intergalactic Squirrels™")
					.build()).queue();

			case "roleinfo" -> event.getChannel().sendMessage(ROLEINFO
					.setColor(Constants.IGSQ_PURPLE)
					.setFooter("© 2020 The Intergalactic Squirrels™")
					.build()).queue();

			case "otherroles" -> event.getChannel().sendMessage(OTHERROLES
					.setColor(Constants.IGSQ_PURPLE)
					.setFooter("© 2020 The Intergalactic Squirrels™")
					.build()).queue();

			default -> EmbedUtils.sendSyntaxError(event);
		}
	}
}