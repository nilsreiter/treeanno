package de.uniheidelberg.cl.a10.eval;

import de.uniheidelberg.cl.a10.cluster.IPartition;

public interface ClusterEvaluation<T> {
	SingleResult evaluate(IPartition<T> gold, IPartition<T> silver, Object name);

	double evaluate(IPartition<T> gold, IPartition<T> silver);

	void setRestriction(String id);
}
