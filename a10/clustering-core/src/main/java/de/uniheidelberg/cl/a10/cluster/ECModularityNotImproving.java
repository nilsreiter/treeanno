package de.uniheidelberg.cl.a10.cluster;

import java.util.List;

public class ECModularityNotImproving<T> implements IExitCondition<T> {

	/**
	 * @param modularity
	 */
	public ECModularityNotImproving(final Modularity<T> modularity) {
		super();
		this.modularity = modularity;
	}

	Modularity<T> modularity;

	@Override
	public boolean matches(final List<? extends IPartition<T>> history) {
		if (history.size() < 2)
			return false;
		System.err.println("Testing exit condition");
		double modOld = modularity.getModularity(history.get(1));
		double modNew = modularity.getModularity(history.get(0));
		System.err.println("mod_old = " + modOld);
		System.err.println("mod_new = " + modNew);

		return modNew < modOld;
	}
}
