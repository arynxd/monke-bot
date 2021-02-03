package me.arynxd.monkebot.entities.cache;

public class CachedGuildSetting implements ICacheableEntity<String, CachedGuildSetting>
{
	private final Long guildId;
	private final String key;
	private final String value;

	public CachedGuildSetting(Long guildId, String key, String value) //Getters and setters here, will be lazy loaded
	{
		this.guildId = guildId;
		this.key = key;
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public CachedGuildSetting getData()
	{
		return this;
	}
}
