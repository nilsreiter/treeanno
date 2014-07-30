package de.nilsreiter.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {

	public static interface ToString<S> {
		String toString(S obj);
	}

	public static String join(final Collection<? extends Object> coll,
			final String delim) {
		StringBuffer buf = new StringBuffer();
		Iterator<?> colli = coll.iterator();
		buf.append(colli.next().toString().trim());
		while (colli.hasNext()) {
			buf.append(delim);
			buf.append(colli.next().toString().trim());
		}
		return buf.toString();
	}

	public static <T> String join(Collection<T> collection, ToString<T> tos,
			String delim) {
		StringBuffer buf = new StringBuffer();
		Iterator<T> colli = collection.iterator();

		buf.append(colli.next().toString());
		while (colli.hasNext()) {
			buf.append(delim);
			buf.append(tos.toString(colli.next()));
		}
		return buf.toString();
	}

	public static String join(final Collection<? extends Object> coll,
			final String delim, final String prefix) {
		StringBuffer buf = new StringBuffer();
		Iterator<?> colli = coll.iterator();

		buf.append(prefix);
		buf.append(colli.next().toString());
		while (colli.hasNext()) {
			buf.append(delim);
			buf.append(prefix);
			buf.append(colli.next().toString());
		}
		return buf.toString();
	}

	public static String join(final int[] array, final String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buf.append(String.valueOf(array[i]));
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}

	public static String join(final Object[] array, final String delimiter,
			final int starting) {
		StringBuffer buf = new StringBuffer();
		for (int i = starting; i < array.length; i++) {
			if (array[i] == null)
				buf.append("NULL");
			else
				buf.append(array[i].toString());
			buf.append(delimiter);
		}
		String r = buf.toString();
		return r.substring(0, r.length() - delimiter.length());
	}
}
