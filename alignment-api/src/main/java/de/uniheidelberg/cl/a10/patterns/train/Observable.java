package de.uniheidelberg.cl.a10.patterns.train;

public interface Observable<T> {
	T getPayload();

	boolean isRegular();
}
