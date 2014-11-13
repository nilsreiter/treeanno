package de.uniheidelberg.cl.reiter.util;

import java.util.Iterator;

/**
 * This interface defines an iterator that can be reset to its starting value.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public interface ResetableIterator<T> extends Iterator<T> {

	/**
	 * Resets the iterator, such that it starts from the beginning (whatever
	 * that was).
	 */
	public void reset();

}
