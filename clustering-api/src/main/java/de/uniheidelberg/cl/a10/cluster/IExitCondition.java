package de.uniheidelberg.cl.a10.cluster;

import java.util.List;

public interface IExitCondition<T> {
	boolean matches(List<? extends IPartition<T>> history);
}
