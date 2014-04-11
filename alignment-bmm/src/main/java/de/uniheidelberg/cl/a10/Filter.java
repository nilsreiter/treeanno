package de.uniheidelberg.cl.a10;

/**
 * This interface can be implemented by any class filtering anything
 * 
 * @author reiter
 * 
 * @param <T>
 *            The class we can filter
 */
public interface Filter<T> {

	/**
	 * Returns true, if the object passes the filter, false otherwise
	 * 
	 * @param t
	 *            The object to filter
	 * @return true or false
	 */
	boolean check(T t);
}
