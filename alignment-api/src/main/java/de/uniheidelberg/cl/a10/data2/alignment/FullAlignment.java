package de.uniheidelberg.cl.a10.data2.alignment;

import java.util.Collection;

public interface FullAlignment<T> extends Alignment<T> {
	void addSingleton(String id, T object);

	void addSingletons(AlignmentIdProvider idp, Collection<T> objects);
}
