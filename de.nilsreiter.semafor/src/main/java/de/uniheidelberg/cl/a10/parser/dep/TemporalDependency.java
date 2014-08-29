package de.uniheidelberg.cl.a10.parser.dep;

public enum TemporalDependency implements IDependency {
	BEFORE, CONTAINS, OVERLAP, AFTER, IS_CONTAINED_IN, INCLUDES, IS_INCLUDED, SAME_AS, NO_RELATIONS;

	@Override
	public IDependency getParent() {
		return null;
	}

	@Override
	public boolean isRootRelation() {
		return false;
	}

	@Override
	public IDependency[] getCategories() {
		return TemporalDependency.values();
	}

	public static TemporalDependency fromString(final String s) {
		return TemporalDependency.valueOf(s.toUpperCase().replaceAll(" ", "_"));
	}

}
