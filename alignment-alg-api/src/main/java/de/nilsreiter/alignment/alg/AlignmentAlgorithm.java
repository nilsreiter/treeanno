package de.nilsreiter.alignment.alg;

import java.util.List;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface AlignmentAlgorithm<T> {
	Alignment<T> align(List<T> l1, List<T> l2);
}