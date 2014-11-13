package de.uniheidelberg.cl.a10.patterns.io;

import java.util.HashMap;
import java.util.Map;

public class IdMap extends HashMap<Integer, String> {
	Map<String, Integer> currentId = new HashMap<String, Integer>();
	private static final long serialVersionUID = 1L;

	public synchronized void newId(final String prefix, final Object obj) {
		if (this.currentId.containsKey(prefix)) {
			this.currentId.put(prefix, this.currentId.get(prefix) + 1);
		} else {
			this.currentId.put(prefix, 1);
		}
		this.put(obj.hashCode(), prefix + currentId.get(prefix));
	}
}