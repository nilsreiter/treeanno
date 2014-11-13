package de.uniheidelberg.cl.a10.cluster;

public interface FullPartition<D> extends IFullPartition<D> {
	void addCluster(ICluster<D> cluster);

	void addSingleton(D object);

}
