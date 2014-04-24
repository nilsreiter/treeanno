package de.uniheidelberg.cl.a10.cluster;

import java.util.Set;

import de.uniheidelberg.cl.a10.HasId;

public interface ICluster<D> extends Set<D>, HasId {
	String getLabel();

	void setLabel(String s);
}
