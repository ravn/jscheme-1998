package com.norvig.jscheme1998.jsr223;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import com.norvig.jscheme1998.Environment;

public class SchemeEnvironmentBindings implements Bindings {

	private final Environment environment;

	public SchemeEnvironmentBindings(Environment environment) {
		this.environment = environment;
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

	public Object put(String name, Object value) {
		Object oldValue = null;
		try {
			// oldValue = get(name);
		} catch (Exception e) {
			// Not bound...
		}
		environment.define(name, value);
		return oldValue; // null for now, get(name) prints to System.err
	}

	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException();
	}

	public Object get(Object key) {
		return environment.lookup((String) key); // only strings
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

}
