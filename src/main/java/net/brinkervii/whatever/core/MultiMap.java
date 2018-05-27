package net.brinkervii.whatever.core;

import java.util.*;

public class MultiMap<T, K> implements Map<T, K> {
	private LinkedList<Map<T, K>> sources = new LinkedList<>();
	private Map<T, K> defaultSource = new HashMap<>();

	public MultiMap() {
		sources.add(defaultSource);
	}

	public MultiMap(Map<T, K>... sources) {
		this();
		this.sources.addAll(Arrays.asList(sources));
	}

	public void appendSource(Map<T, K> source) {
		sources.add(source);
	}

	@Override
	public int size() {
		return keySet().size();
	}

	@Override
	public boolean isEmpty() {
		return size() <= 0;
	}

	@Override
	public boolean containsKey(Object o) {
		return keySet().contains((T) o);
	}

	@Override
	public boolean containsValue(Object o) {
		return values().contains((K) o);
	}

	@Override
	public K get(Object o) {
		T kast = (T) o;

		for (Map<T, K> source : sources) {
			if (source.containsKey(kast)) {
				return source.get(o);
			}
		}

		return null;
	}

	@Override
	public K put(T t, K k) {
		return defaultSource.put(t, k);
	}

	@Override
	public K remove(Object o) {
		return null;
	}

	@Override
	public void putAll(Map<? extends T, ? extends K> map) {
		defaultSource.putAll(map);
	}

	@Override
	public void clear() {
		defaultSource.clear();
		sources.clear();
		sources.add(defaultSource);
	}

	@Override
	public Set<T> keySet() {
		LinkedHashSet<T> keys = new LinkedHashSet<>();
		for (Map<T, K> source : sources) {
			keys.addAll(source.keySet());
		}

		return keys;
	}

	@Override
	public Collection<K> values() {
		LinkedHashSet<K> values = new LinkedHashSet<>();
		for (T key : keySet()) {
			values.add(get(key));
		}

		return values;
	}

	@Override
	public Set<Entry<T, K>> entrySet() {
		LinkedHashSet<Entry<T, K>> entries = new LinkedHashSet<>();
		for (T key : keySet()) {
			entries.add(new AbstractMap.SimpleEntry<>(key, get(key)));
		}

		return entries;
	}
}
