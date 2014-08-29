package de.uniheidelberg.cl.a10.parser.dep;

import java.util.HashMap;
import java.util.Map;

public class StringDependency implements IDependency {
	String name;

	private static Map<String, StringDependency> relations = new HashMap<String, StringDependency>();

	public StringDependency() {
	};

	protected StringDependency(final String name) {
		this.name = name;
	}

	@Override
	public IDependency getParent() {
		return null;
	}

	@Override
	public boolean isRootRelation() {
		return name.equalsIgnoreCase("root");
	}

	public static StringDependency fromString(final String s) {
		if (!relations.containsKey(s.toUpperCase())) {
			relations.put(s.toUpperCase(),
					new StringDependency(s.toUpperCase()));
		}
		return relations.get(s.toUpperCase());
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public IDependency[] getCategories() {
		return relations.values().toArray(new StringDependency[] {});
	}

}
