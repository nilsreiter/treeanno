package de.pado.sigf;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class TestFScoreART {

	InputStream[] streams;

	@Before
	public void setUp() {
		streams = new InputStream[2];

		streams[0] = this.getClass().getResourceAsStream(
				"/exampleFScore/model1");
		streams[1] = this.getClass().getResourceAsStream(
				"/exampleFScore/model2");
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		ApproximateRandomizationTest<Triple<Long>> art = new FScoreART();

		art.run(streams[0], streams[1], FScore.class, 1000);
	}
}