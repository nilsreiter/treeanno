package de.uniheidelberg.cl.a10;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMain {

	Main main;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		main = new Main() {
		};
		main.processArguments(new String[] {});

		assertEquals("/home/users0/reiterns/local/wordnet", main
				.getConfiguration().getString("paths.wnhome"));
	}

}
