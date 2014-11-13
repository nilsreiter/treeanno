package edu.cuny.qc.speech;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestClusterEvaluator {
	ContingencyTable table;
	ContingencyTable table2;
	ClusterEvaluator ce;

	@Before
	public void setUp() {
		table = new ContingencyTable(4, 3);
		table.set(0, 0, 5.0);
		table.set(0, 1, 0.0);
		table.set(0, 2, 0.0);

		table.set(1, 0, 0.0);
		table.set(1, 1, 7.0);
		table.set(1, 2, 0.0);

		table.set(2, 0, 0.0);
		table.set(2, 1, 0.0);
		table.set(2, 2, 3.0);

		table.set(3, 0, 0.0);
		table.set(3, 1, 0.0);
		table.set(3, 2, 3.0);
		ce = new ClusterEvaluator();

		/**
		 * This data comes from the NVI paper, and they give results for a few
		 * measures. But the results I get using the library are different for V
		 * and VI. Matches for NVI.
		 */
		Double[][] d = new Double[10][10];
		d[0] = new Double[] { 7d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 0d };
		d[1] = new Double[] { 0d, 7d, 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d };
		d[2] = new Double[] { 0d, 0d, 7d, 1d, 1d, 1d, 0d, 0d, 0d, 0d };
		d[3] = new Double[] { 0d, 0d, 0d, 7d, 1d, 1d, 1d, 0d, 0d, 0d };
		d[4] = new Double[] { 0d, 0d, 0d, 0d, 7d, 1d, 1d, 1d, 0d, 0d };
		d[5] = new Double[] { 0d, 0d, 0d, 0d, 0d, 7d, 1d, 1d, 1d, 0d };
		d[6] = new Double[] { 0d, 0d, 0d, 0d, 0d, 0d, 7d, 1d, 1d, 1d };
		d[7] = new Double[] { 1d, 0d, 0d, 0d, 0d, 0d, 0d, 7d, 1d, 1d };
		d[8] = new Double[] { 1d, 1d, 0d, 0d, 0d, 0d, 0d, 0d, 7d, 1d };
		d[9] = new Double[] { 1d, 1d, 1d, 0d, 0d, 0d, 0d, 0d, 0d, 7d };

		table2 = new ContingencyTable(d);

	}

	@Test
	public void testRandIndex() {

		ce.setData(table);

		assertEquals(0.9411, ce.getRandIndex(), 1e-4);

	}

	@Test
	public void testAdjustedRandIndex() {
		ce.setData(table);

		assertEquals(0.8518, ce.getAdjustedRandIndex(), 1e-4);
	}

	@Test
	public void testPurity() {
		ce.setData(table);

		assertEquals(0.9999, ce.getPurity(), 1e-4);
	}

	@Test
	public void testVI() {
		ce.setData(table);
		assertEquals(0.3333, ce.getVI(), 1e-4);

		ce.setData(table2);
		assertEquals(1.88, ce.getVI(), 1e-4);
	}

	@Test
	public void testNVI() {

		ce.setData(table2);
		assertEquals(0.81, ce.getNVI(), 1e-2);
	}

	@Test
	public void testV() {
		ce.setData(table2);
		assertEquals(0.587, ce.getNVI(), 1e-3);
	}

}