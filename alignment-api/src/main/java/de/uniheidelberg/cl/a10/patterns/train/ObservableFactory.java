package de.uniheidelberg.cl.a10.patterns.train;

public interface ObservableFactory<T> {
	public static final String SPECIAL_START = "Start";
	public static final String SPECIAL_END = "End";

	Observable<T> getObservable(T obj);

	Observable<T> getSpecialObservable(String desc);
}
