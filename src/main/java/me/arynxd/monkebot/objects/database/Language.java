package me.arynxd.monkebot.objects.database;

public enum Language
{
	EN_US("en_US");

	private final String languageCode;

	Language(String lanugageCode)
	{
		this.languageCode = lanugageCode;
	}

	public String getLanguageCode()
	{
		return languageCode;
	}
}
