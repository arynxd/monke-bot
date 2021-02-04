package me.arynxd.monkebot.entities.cache;

public interface ICacheableEntity<K, V>
{
	K getKey();

	V getData();
}
