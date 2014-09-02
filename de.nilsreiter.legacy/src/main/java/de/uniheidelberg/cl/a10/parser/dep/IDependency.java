package de.uniheidelberg.cl.a10.parser.dep;

public interface IDependency {
	public IDependency getParent();

	public boolean isRootRelation();

	public IDependency[] getCategories();
}
