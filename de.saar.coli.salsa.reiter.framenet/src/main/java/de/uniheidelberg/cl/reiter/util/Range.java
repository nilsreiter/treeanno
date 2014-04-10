/**
 * 
 * Copyright 2009-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.uniheidelberg.cl.reiter.util;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a pair of integer values and is used to express
 * character ranges.
 * 
 * @author reiter
 * 
 */
public class Range extends Pair<Integer, Integer> {

    /**
     * A constructor taking the two integer values.
     * 
     * @param e1
     *            first value
     * @param e2
     *            second value
     */
    public Range(final Integer e1, final Integer e2) {
	super(e1, e2);
    }

    /**
     * This method increments both values by s.
     * 
     * @param s
     *            The value to add to both elements
     */
    public void increment(final Integer s) {
	super.elemnt1 += s;
	super.elemnt2 += s;
    }

    @Override
    public Range copy() {
	Range ret = new Range(this.elemnt1, this.elemnt2);
	return ret;
    }

    /**
     * Creates a copy of the range object, with values shifted by el1 and el2,
     * resp.
     * 
     * @param el1
     *            Shifting parameter for element 1
     * @param el2
     *            Shifting parameter for element 2
     * @return A new range object
     */
    public Range copy(final int el1, final int el2) {
	return new Range(this.elemnt1 + el1, this.elemnt2 + el2);
    }

    /**
     * This method returns true, when r is a sub range of this range.
     * 
     * @param r
     *            The assumed sub-range
     * @return true or false
     */
    public boolean isSubRange(final Range r) {
	return r.getElement1() >= this.getElement1()
		&& r.getElement2() <= this.getElement2();
    }

    /**
     * This method checks, whether multiple range objects overlap.
     * 
     * @param ranges
     *            A collection of Range objects to be checked.
     * @return True, if at least one range object overlaps partially with a
     *         successor.
     */
    public static boolean isOverlapping(final Range... ranges) {
	SortedSet<Range> ts = new TreeSet<Range>();
	for (Range r : ranges) {
	    ts.add(r);
	}
	Range prev = null;
	for (Range r : ts) {
	    if (prev != null) {
		if (r.getElement1() <= prev.getElement2()) {
		    return true;
		}
	    }
	    prev = r;
	}
	return false;
    }

    /**
     * See {@link #isOverlapping(Range...)}.
     * 
     * @param coll
     *            A collection of Range objects to be checked
     * @return True, if at least one range object overlaps partially with a
     *         successor.
     */
    public static boolean isOverlapping(final Collection<Range> coll) {
	return isOverlapping(coll.toArray(new Range[coll.size()]));
    }

    @Override
    public int hashCode() {
	return elemnt1.hashCode() + elemnt2.hashCode();

    }

    @Override
    public boolean equals(final Object other) {
	if (other.getClass() == Range.class) {
	    if (this.hashCode() != other.hashCode()) {
		return false;
	    }
	    Range r = (Range) other;
	    return this.getElement1() == r.getElement1()
		    && this.getElement2() == r.getElement2();
	} else {
	    return false;
	}
    }
}
