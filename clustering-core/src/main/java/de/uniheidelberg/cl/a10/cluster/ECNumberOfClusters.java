package de.uniheidelberg.cl.a10.cluster;

import java.util.List;

public class ECNumberOfClusters<T> implements IExitCondition<T> {

	int n;

	public ECNumberOfClusters(final int number) {
		n = number;
	}

	@Override
	public boolean matches(final List<? extends IPartition<T>> history) {
		return history.get(0).size() == n;
	}

}
