package de.uniheidelberg.cl.a10.cluster;

public interface PartitionScorer<D> {

	double getScore(IPartition<D> partition);
}
