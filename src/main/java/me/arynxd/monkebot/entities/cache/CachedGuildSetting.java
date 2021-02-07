package me.arynxd.monkebot.entities.cache;

public class CachedGuildSetting implements ICacheableEntity<String, CachedGuildSetting>
{
	private final String key;
	private final String value;

	public CachedGuildSetting(String key, String value)
	{
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
