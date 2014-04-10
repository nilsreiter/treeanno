package de.saar.coli.salsa.reiter.framenet.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

import de.uniheidelberg.cl.reiter.util.Pair;
import de.uniheidelberg.cl.reiter.util.Range;

public class TestPair {

    @Test
    public void testPair() {
	Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(5, 6);
	Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(5, 7);
	Pair<Integer, Integer> pair3 = new Pair<Integer, Integer>(5, 7);
	Pair<Integer, Integer> pair4 = new Pair<Integer, Integer>(5, 8);
	Pair<Integer, Integer> pair5 = new Pair<Integer, Integer>(6, 7);

	assertEquals("5-6", pair1.toString());
	assertEquals(false, pair1.equals(pair2));
	assertEquals(true, pair2.equals(pair3));
	assertEquals(new Integer(5), pair1.getElement1());
	assertEquals(new Integer(6), pair1.getElement2());
	assertEquals(0, pair2.compareTo(pair3));
	assertEquals(-1, pair1.compareTo(pair2));
	assertEquals(1, pair2.compareTo(pair1));
	assertEquals(0, pair2.compareTo(pair3));
	assertEquals(-1, pair4.compareTo(pair5));
    }

    @Test
    public void testRange() {
	Range r1 = new Range(1, 5);
	Range r2 = new Range(1, 4);
	Range r3 = new Range(2, 7);
	Range r4 = new Range(2, 4);
	Range r5 = new Range(6, 9);

	// isSubRange
	assertTrue(r1.isSubRange(r2));
	assertTrue(r1.isSubRange(r4));
	assertFalse(r1.isSubRange(r3));
	assertFalse(r1.isSubRange(r5));
	assertTrue(r1.isSubRange(r1));
	assertFalse(r2.isSubRange(r1));

	// copy
	Range r1c = r1.copy();
	assertFalse(r1c == r1);
	assertEquals(r1c.getElement1(), r1.getElement1());
	assertEquals(r1c.getElement2(), r1.getElement2());

	// increment
	r1c.increment(10);
	assertEquals(11, r1c.getElement1().intValue());
	assertEquals(15, r1c.getElement2().intValue());

	assertEquals(1, r1.getElement1().intValue());
	assertEquals(5, r1.getElement2().intValue());

	// isOverlapping
	Collection<Range> coll = new HashSet<Range>();
	coll.add(r1);
	coll.add(r2);
	coll.add(r3);
	coll.add(r4);
	coll.add(r5);

	assertTrue(Range.isOverlapping(r1, r2, r3, r4, r5));
	assertFalse(Range.isOverlapping(r1, r5));
	assertFalse(Range.isOverlapping(r2, new Range(5, 8), new Range(9, 12)));
    }
}
