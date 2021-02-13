package me.arynxd.monkebot.objects.cache;

import org.jetbrains.annotations.NotNull;

public interface ICacheableEntity<K, V>
{
	@NotNull K getKey();

	@NotNull V getData();
}
