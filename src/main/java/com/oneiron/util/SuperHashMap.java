package com.oneiron.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SuperHashMap extends HashMap<String, Object> implements Map<String, Object>{
	private static final long serialVersionUID = 4075742283466322480L;
	
	public SuperHashMap() {
		super();
	}

	@Override
	public int size() {
		return super.size();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public Object get(Object key) {
		return super.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@Override
	public Object put(String key, Object value) {
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		super.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		return super.remove(key);
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public Set<String> keySet() {
		return super.keySet();
	}

	@Override
	public Collection<Object> values() {
		return super.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return super.entrySet();
	}

	@Override
	public Object getOrDefault(Object key, Object defaultValue) {
		return super.getOrDefault(key, defaultValue);
	}

	@Override
	public Object putIfAbsent(String key, Object value) {
		return super.putIfAbsent(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return super.remove(key, value);
	}

	@Override
	public boolean replace(String key, Object oldValue, Object newValue) {
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public Object replace(String key, Object value) {
		return super.replace(key, value);
	}

	@Override
	public Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return super.computeIfAbsent(key, mappingFunction);
	}

	@Override
	public Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return super.computeIfPresent(key, remappingFunction);
	}

	@Override
	public Object compute(String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return super.compute(key, remappingFunction);
	}

	@Override
	public Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return super.merge(key, value, remappingFunction);
	}

	@Override
	public void forEach(BiConsumer<? super String, ? super Object> action) {
		super.forEach(action);
	}

	@Override
	public void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		super.replaceAll(function);
	}

	@Override
	public Object clone() {
		return super.clone();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
