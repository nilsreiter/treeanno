package de.uniheidelberg.cl.a10.cluster;

import java.util.Collection;

import de.uniheidelberg.cl.a10.Evaluatable;

/**
 * This interface models partitions. We assume that each object is in at most
 * one cluster.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface IPartition<T> extends Evaluatable {
	/**
	 * A collection of objects, all objects that are clustered by this
	 * partition. Union of all the clusters
	 * 
	 * @return
	 */
	Collection<T> getObjects();

	/**
	 * Returns the clusters
	 * 
	 * @return
	 */
	Collection<? extends ICluster<T>> getClusters();

	/**
	 * Returns the cluster, in which the given object is.
	 * 
	 * @param object
	 * @return
	 */
	ICluster<T> getCluster(T object);

	/**
	 * The number of clusters in this partition
	 * 
	 * @return
	 */
	int size();

	/**
	 * Returns true, if the two objects are in the same cluster.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	boolean together(T o1, T o2);
}
