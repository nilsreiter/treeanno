package de.uniheidelberg.cl.a10.cluster.impl;

import de.uniheidelberg.cl.a10.cluster.FullPartition;
import de.uniheidelberg.cl.a10.cluster.ICluster;

public class FullPartition_impl<T> extends Partition_impl<T> implements
		FullPartition<T> {

	private static final long serialVersionUID = 1L;

	public FullPartition_impl() {
		super();
	}

	@Override
	public void addSingleton(final T object) {
		this.add(new Cluster_impl<T>(object));

	}

	@Override
	public void addCluster(final ICluster<T> cluster) {
		this.add(cluster);
	}

}
