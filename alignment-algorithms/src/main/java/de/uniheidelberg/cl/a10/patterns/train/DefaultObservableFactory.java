package de.uniheidelberg.cl.a10.patterns.train;

import java.util.HashMap;
import java.util.Map;

public class DefaultObservableFactory<T> implements ObservableFactory<T> {

	Map<T, Observable<T>> map = new HashMap<T, Observable<T>>();
	Map<String, Observable<T>> specialMap =
			new HashMap<String, Observable<T>>();

	@Override
	public Observable<T> getObservable(T obj) {
		if (map.containsKey(obj)) {
			return map.get(obj);
		} else {
			Observable<T> obs = new DefaultObservable<T>(obj);
			map.put(obj, obs);
			return obs;
		}
	};

	@Override
	public Observable<T> getSpecialObservable(String desc) {
		if (specialMap.containsKey(desc)) {
			return specialMap.get(desc);
		} else {
			Observable<T> obs = new Observable<T>() {

				@Override
				public T getPayload() {
					return null;
				}

				@Override
				public boolean isRegular() {
					return false;
				}

			};
			specialMap.put(desc, obs);
			return obs;
		}
	}

	public static class DefaultObservable<T> implements Observable<T> {
		T payload;

		public DefaultObservable(T pl) {
			payload = pl;
		}

		@Override
		public T getPayload() {
			return payload;
		}

		@Override
		public boolean isRegular() {
			return true;
		}

		@Override
		public String toString() {
			return "Observable(" + payload.toString() + ")";
		}

	}

}
