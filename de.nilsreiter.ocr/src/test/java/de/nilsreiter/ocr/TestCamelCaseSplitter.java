package de.nilsreiter.ocr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestCamelCaseSplitter {

	CamelCaseSplitter splitter;

	String[] noCamelCase = new String[] { "The", "the", "The Dog",
			"The Dog Barks", "The! Dog", "The-Dog", "The! Dog! barks", "ABC" };

	String[] camelCase = new String[] { "TheDog", "TheDog Barks",
			"The dog BarkS", "AbC" };

	@Before
	public void setUp() {
		splitter = new CamelCaseSplitter();
	}

	@Test
	public void testIsCamelCase() {
		for (String s : noCamelCase) {
			assertFalse(s, splitter.isCamelCase(s));
		}

		for (String s : camelCase) {
			assertTrue(s, splitter.isCamelCase(s));
		}
	}

	@Test
	public void testSplitAtCaseChange() {
		assertEquals(2, splitter.splitAtCaseChange("TheDog").length);
		assertEquals(2, splitter.splitAtCaseChange("TheDog Barks").length);
		assertEquals(2, splitter.splitAtCaseChange("The Dog barkS").length);
		assertEquals(2, splitter.splitAtCaseChange("AbC").length);
		assertEquals(
				2,
				splitter.splitAtCaseChange("This text contains a CamelCase token").length);

	}

}
