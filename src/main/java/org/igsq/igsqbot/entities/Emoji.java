package org.igsq.igsqbot.entities;

import java.util.List;

public enum Emoji
{
	PEAR(":pear:", "U+1F350"),
	WATERMELON(":watermelon:", "U+1F349"),
	PINEAPPLE(":pineapple:", "U+1F34D"),
	APPLE(":apple:", "U+1F34E"),
	BANANA(":banana:", "U+1F34C"),
	AVOCADO(":avocado:", "U+1F951"),
	EGGPLANT(":eggplant:", "U+1F346"),
	KIWI(":kiwi:", "U+1F95D"),
	GRAPES(":grapes:", "U+1F347"),
	BLUEBERRIES(":blueberries:", "U+1FAD0"),
	CHERRIES(":cherries:", "U+1F352"),
	ONION(":onion:", "U+1F9C5"),
	PEACH(":peach:", "U+1F351"),
	LEMON(":lemon:", "U+1F34B"),
	TANGERINE(":tangerine:", "U+1F34A"),
	MELON(":melon:", "U+1F348"),
	COCONUT(":coconut:", "U+1F965"),
	GARLIC(":garlic:", "U+1F9C4"),
	CUCUMBER(":cucumber:", "U+1F952"),
	SQUID(":squid:", "U+1F991"),

	THUMB_UP(":thumbsup:", "\uD83D\uDC4D"),
	THUMB_DOWN(":thumbsdown:", "\uD83D\uDC4E"),

	GREEN_TICK(":white_check_mark:", "\u2705"),
	GREEN_CROSS(":negative_squared_cross_mark:", "\u274E"),

	ARROW_LEFT(":arrow_left:", "\u2B05\uFE0F"),
	ARROW_RIGHT(":arrow_right:", "\u27A1\uFE0F"),

	STOP_SIGN(":octagonal_sign:", "\uD83D\uDED1"),

	WASTE_BASKET(":wastebasket:", "\uD83D\uDDD1\uFE0F"),

	SUCCESS("801087111855079424"),
	FAILURE("801087074949791754"),

	IGSQ1("798965091247587348"),
	IGSQ2("798965111590223892"),
	IGSQ3("798965123494182933"),
	IGSQ4("798965137075470336"),
	IGSQ5("798965148357230662"),

	ZERO(":zero:", "\u0030\uFE0F"),
	ONE(":one:", "\u0031\uFE0F"),
	TWO(":two:", "\u0032\uFE0F"),
	THREE(":three:", "\u0033\uFE0F"),
	FOUR(":four:", "\u0034\uFE0F"),
	FIVE(":five:", "\u0035\uFE0F"),
	SIX(":six:", "\u0036\uFE0F"),
	SEVEN(":seven:", "\u0037\uFE0F"),
	EIGHT(":eight:", "\u0038\uFE0F"),
	NINE(":nine:", "\u0039\uFE0F");

	private final String unicode;
	private final String emote;
	private final boolean isAnimated;

	Emoji(String emote, String unicode)
	{
		this.emote = emote;
		this.unicode = unicode;
		this.isAnimated = false;
	}

	Emoji(String emote)
	{
		this.emote = emote;
		this.unicode = "";
		this.isAnimated = false;
	}

	public static List<Emoji> getPoll()
	{
		return List.of(
				PEAR,
				WATERMELON,
				PINEAPPLE,
				APPLE,
				BANANA,
				AVOCADO,
				EGGPLANT,
				KIWI,
				GRAPES,
				BLUEBERRIES,
				CHERRIES,
				ONION,
				PEACH,
				LEMON,
				TANGERINE,
				MELON,
				COCONUT,
				GARLIC,
				CUCUMBER,
				SQUID);
	}

	public String getUnicode()
	{
		return unicode;
	}

	public String getEmote()
	{
		return emote;
	}

	public String getAsReaction()
	{
		if(this.unicode.isBlank())
		{
			return "emote:" + this.emote;
		}
		return this.unicode;
	}

	public String getAsChat()
	{
		if(this.unicode.isBlank())
		{
			if(this.isAnimated)
			{
				return "<a:emote:" + this.emote + ">";
			}
			return "<:emote:" + this.emote + ">";
		}
		return this.emote;
	}

	public List<Emoji> getIGSQNumbers()
	{
		return List.of(IGSQ1, IGSQ2, IGSQ3, IGSQ4);
	}


}
