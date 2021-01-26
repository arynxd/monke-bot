package me.arynxd.monkebot.entities.bot;

/**
 * An enum containing all possible {@link Configuration configuration} options
 */
public enum ConfigOption
{
	TOKEN("token", "token"),
	PRIVILEGEDUSERS("privilegedusers", "0000000000000"),
	HOMESERVER("homeserver", "000000000000000000"),

	LOCALUSERNAME("localusername", "username"),
	LOCALPASSWORD("localpassword", "password"),
	LOCALDRIVER("localdriver", "org.postgresql.Driver"),
	LOCALURL("localurl", "jdbc:type://host:port/database");

	private final String key;
	private final String defaultValue;

	ConfigOption(String key, String defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public String getKey()
	{
		return key;
	}
}
