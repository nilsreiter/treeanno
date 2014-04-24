package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;
import java.util.HashSet;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.a10.cluster.IPartition;

public class Partition_impl<T> extends HashSet<ICluster<T>> implements
		IPartition<T> {

	private static final long serialVersionUID = 1L;

	public Partition_impl() {

	}

	public Partition_impl(final Collection<? extends Collection<T>> colls) {
		for (Collection<T> coll : colls) {
			ICluster<T> set = new Cluster_impl<T>();
			set.addAll(coll);
			this.add(set);
		}
	}

	@Override
	public Collection<T> getObjects() {
		Collection<T> r = new HashSet<T>();
		for (ICluster<T> cl : this) {
			r.addAll(cl);
		}
		return r;
	}

	@Override
	public Collection<? extends ICluster<T>> getClusters() {
		return this;
	}

	@Override
	public ICluster<T> getCluster(final T object) {
		for (ICluster<T> cluster : this) {
			if (cluster.contains(object))
				return cluster;
		}
		return null;
	}

	@Override
	public boolean together(final T o1, final T o2) {
		return this.getCluster(o1) == this.getCluster(o2);
	}

}
