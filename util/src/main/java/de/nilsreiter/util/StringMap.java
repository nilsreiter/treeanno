package de.nilsreiter.util;

import java.util.HashMap;

public class StringMap<K> extends HashMap<K, String> {

	private static final long serialVersionUID = 1L;

	public void append(K key, String s) {
		if (containsKey(key)) {
			put(key, get(key) + s);
		} else {
			put(key, s);
		}
	}
}
