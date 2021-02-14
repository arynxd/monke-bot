package me.arynxd.monkebot.objects.database;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public enum Language
{
	EN_US("en_US", List.of("")),
	UNKNOWN("");

	private final String languageCode;
	private final List<String> aliases;

	public List<String> getAliases()
	{
		return aliases;
	}

	Language(String lanugageCode, List<String> aliases)
	{
		this.languageCode = lanugageCode;
		this.aliases = aliases;
	}

	Language(String lanugageCode)
	{
		this.languageCode = lanugageCode;
		this.aliases = Collections.emptyList();
	}

	public String getLanguageCode()
	{
		return languageCode;
	}

	public static @NotNull Language getFromName(String text)
	{
		return Arrays.stream(values())
				.filter(lang ->
						lang.getLanguageCode().equalsIgnoreCase(text)
						|| lang.getAliases().contains(text.toLowerCase()))
				.findFirst().orElse(Language.UNKNOWN);
	}
}
