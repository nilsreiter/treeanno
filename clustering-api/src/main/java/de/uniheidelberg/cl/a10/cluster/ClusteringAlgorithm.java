package de.uniheidelberg.cl.a10.cluster;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ClusteringAlgorithm<D, V extends Comparable<V>> {

	void setDocumentSimilarityFunction(
			IDocumentSimilarityFunction<D, V> function);

	IDocumentSimilarityFunction<D, V> getDocumentSimilarityFunction();

	IFullPartition<D> cluster(Collection<D> documents);

	List<? extends IPartition<D>> getPartitionHistory();

	void parseOptionMap(Map<String, String> map);

}
