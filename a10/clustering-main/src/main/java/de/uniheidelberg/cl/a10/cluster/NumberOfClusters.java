package de.uniheidelberg.cl.a10.cluster;

public class NumberOfClusters<D> implements PartitionScorer<D> {

	int numberOfClusters;

	public NumberOfClusters(final int number) {
		this.numberOfClusters = number;
	}

	@Override
	public double getScore(final IPartition<D> partition) {
		if (partition.getClusters().size() == this.getNumberOfClusters()) {
			return 1.0;
		}
		return 0;
	}

	public int getNumberOfClusters() {
		return numberOfClusters;
	}

	public void setNumberOfClusters(final int numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}

}
