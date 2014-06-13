package de.nilsreiter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.stat.descriptive.moment.GeometricMean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWeightedGeometricMean {

	double[] values;

	GeometricMean reference;

	WeightedGeometricMean wGeo;

	@Before
	public void setUp() throws Exception {
		values = new double[5];
		values[0] = 0.5;
		values[1] = 0.1;
		values[2] = 0.6;
		values[3] = 0.7;
		values[4] = 0.8;

		reference = new GeometricMean();
		wGeo = new WeightedGeometricMean();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEvaluateDoubleArrayIntIntDoubleArray() {
		assertEquals(
				reference.evaluate(values),
				wGeo.evaluate(values, 0, 5, new double[] { 1.0, 1.0, 1.0, 1.0,
						1.0 }), 0.0);
		assertTrue(reference.evaluate(values) < wGeo.evaluate(values, 0, 5,
				new double[] { 2.0, 1.0, 1.0, 1.0, 1.0 }));
		assertTrue(reference.evaluate(values) > wGeo.evaluate(values, 0, 5,
				new double[] { 1.0, 2.0, 1.0, 1.0, 1.0 }));
	}
}
