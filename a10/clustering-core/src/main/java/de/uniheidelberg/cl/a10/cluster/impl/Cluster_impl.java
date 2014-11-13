package de.uniheidelberg.cl.a10.cluster.impl;

import java.util.Collection;
import java.util.HashSet;

import de.uniheidelberg.cl.a10.cluster.ICluster;
import de.uniheidelberg.cl.reiter.util.HashCodeUtil;

public class Cluster_impl<D> extends HashSet<D> implements ICluster<D> {

	String id;
	String label;

	private static final long serialVersionUID = 1L;

	public Cluster_impl(final Collection<D> element) {
		super();
		this.addAll(element);
	}

	public Cluster_impl(D... elements) {
		super();
		for (int i = 0; i < elements.length; i++) {
			this.add(elements[i]);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(final String s) {
		label = s;
	}

	@Override
	public int hashCode() {
		return HashCodeUtil.hash(HashCodeUtil.SEED, this);
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof ICluster))
			return false;
		ICluster<D> cl = (ICluster<D>) o;
		return cl.hashCode() == this.hashCode();

	}

}
