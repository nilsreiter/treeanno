package de.uniheidelberg.cl.reiter.util;

import java.util.Iterator;
import java.util.SortedMap;

/**
 * This class represents an iterator over a sorted map, in which the key of the
 * map is a Range-object.
 * 
 * @author reiter
 * 
 * @param <V>
 *            The value of the map
 */
public class SortedMapIterator<V> implements Iterator<V> {

    /**
     * The map.
     */
    private SortedMap<Range, V> map;

    /**
     * Constructor.
     * 
     * @param sortedMap
     *            The map we want to iterate over
     */
    public SortedMapIterator(final SortedMap<Range, V> sortedMap) {
	this.map = sortedMap;
    }

    @Override
    public boolean hasNext() {
	return map.size() > 0;
    }

    @Override
    public V next() {
	Range lowestKey = map.firstKey().copy();
	V returnValue = map.get(lowestKey);
	lowestKey.increment(1);
	map = map.tailMap(lowestKey);
	return returnValue;
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException();
    }

}
