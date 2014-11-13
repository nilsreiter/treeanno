package de.uniheidelberg.cl.a10.cluster;

public interface IDocumentSimilarityFunction<D, V extends Comparable<V>> {
	V getSimilarity(D rd1, D rd2);
}
