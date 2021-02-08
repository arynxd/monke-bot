package me.arynxd.monkebot.entities.cache;

import org.jetbrains.annotations.NotNull;

public interface ICacheableEntity<K, V>
{
	@NotNull K getKey();

	@NotNull V getData();
}
