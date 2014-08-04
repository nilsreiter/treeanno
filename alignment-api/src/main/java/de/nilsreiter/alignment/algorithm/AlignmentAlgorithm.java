package de.nilsreiter.alignment.algorithm;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface AlignmentAlgorithm<D> {
	public Alignment<D> align(List<D> list1, List<D> list2);

	Class<?> getConfigurationBean();

}