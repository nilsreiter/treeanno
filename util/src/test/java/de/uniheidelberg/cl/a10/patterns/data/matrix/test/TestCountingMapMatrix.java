package de.uniheidelberg.cl.a10.patterns.data.matrix.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.patterns.data.matrix.CountingMapMatrix;
import de.uniheidelberg.cl.a10.patterns.data.matrix.IterableMatrix;

public class TestCountingMapMatrix {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testMatrix() throws Exception {
		IterableMatrix<String, String, Integer> matrix =
				new CountingMapMatrix<String, String, Integer>();
		matrix.put("f", "g", 6);
		matrix.put("a", "b", 1);
		matrix.put("g", "h", 7);
		matrix.put("d", "e", 4);
		matrix.put("b", "c", 2);
		matrix.put("h", "i", 8);
		matrix.put("c", "d", 3);
		matrix.put("e", "f", 5);

		Iterator<Pair<String, String>> iterator =
				matrix.getNavigableSet().iterator();

		assertEquals("a", iterator.next().getKey());
		assertEquals("b", iterator.next().getKey());
		assertEquals("c", iterator.next().getKey());
		assertEquals("d", iterator.next().getKey());
		assertEquals("e", iterator.next().getKey());
		assertEquals("f", iterator.next().getKey());
		assertEquals("g", iterator.next().getKey());
		assertEquals("h", iterator.next().getKey());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testMatrix2() throws Exception {
		IterableMatrix<String, String, Integer> matrix =
				new CountingMapMatrix<String, String, Integer>();
		matrix.put("a", "b", 3);
		matrix.put("a", "c", 2);
		matrix.put("b", "c", 1);

		Iterator<Pair<String, String>> iterator =
				matrix.getNavigableSet().iterator();
		Pair<String, String> pair;
		pair = iterator.next();
		assertEquals("b", pair.getKey());
		assertEquals("c", pair.getValue());
		pair = iterator.next();
		assertEquals("a", pair.getKey());
		assertEquals("c", pair.getValue());
		pair = iterator.next();
		assertEquals("a", pair.getKey());
		assertEquals("b", pair.getValue());
	}

}
