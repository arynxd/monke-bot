package me.arynxd.monkebot.entities.database;

public enum Language
{
	EN_US("en_US");

	private final String languageCode;

	public String getLanguageCode()
	{
		return languageCode;
	}

	Language(String lanugageCode)
	{
		this.languageCode = lanugageCode;
	}
}
