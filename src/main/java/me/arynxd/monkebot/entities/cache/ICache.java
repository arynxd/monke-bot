package me.arynxd.monkebot.entities.cache;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICache<K, V extends ICacheableEntity<K, V>> //K-KEY TO ENTITY V-VALUE TO ENTITY
{
	void put(V value);
	void put(Collection<V> values);

	@Nullable
	V get(K key);

	void update(V oldValue, V newValue);
	void update(K oldValue, V newValue);

	@Nonnull
	Boolean isCached(K key);

	void remove(K key);
	void remove(V key);
	void remove(Collection<V> values);
}
