package net.brinkervii.jewel.core;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectMapAdapter implements Map<String, Object> {
	private final Object innerObject;
	private final Class<?> innerClass;

	public ObjectMapAdapter(Object innerObject) {
		this.innerObject = innerObject;
		this.innerClass = innerObject.getClass();
	}

	@Override
	public int size() {
		return innerClass.getDeclaredFields().length;
	}

	@Override
	public boolean isEmpty() {
		return innerClass.getDeclaredFields().length <= 0;
	}

	@Override
	public boolean containsKey(Object o) {
		String s = String.valueOf(o);
		for (Field field : innerClass.getDeclaredFields()) {
			if (field.getName().equals(s)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean containsValue(Object o) {
		for (String s : keySet()) {
			if (get(s).equals(o)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object get(Object o) {
		return get(String.valueOf(o));
	}

	public Object get(String o) {
		try {
			final Field field = innerClass.getDeclaredField(o);
			field.setAccessible(true);
			return field.get(innerObject);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object put(String s, Object o) {
		try {
			final Field field = innerClass.getDeclaredField(s);
			field.setAccessible(true);
			field.set(innerObject, o);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return o;
	}

	@Override
	public Object remove(Object o) {
		boolean success = false;

		for (String s : this.keySet()) {
			if (get(s).equals(o)) {
				put(s, null);
				success = true;
			}
		}

		return success;
	}

	@Override
	public void putAll(Map<? extends String, ?> map) {
		for (String s : map.keySet()) {
			put(s, map.get(s));
		}
	}

	@Override
	public void clear() {
		for (String s : keySet()) {
			put(s, null);
		}
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new LinkedHashSet<>();
		for (Field field : innerClass.getDeclaredFields()) {
			set.add(field.getName());
		}

		return set;
	}

	@Override
	public Collection<Object> values() {
		Collection<Object> values = new LinkedList<>();
		for (String s : keySet()) {
			values.add(get(s));
		}

		return values;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		Set<Entry<String, Object>> entries = new LinkedHashSet<>();

		for (String s : keySet()) {
			entries.add(new AbstractMap.SimpleEntry<>(s, get(s)));
		}

		return entries;
	}
}
