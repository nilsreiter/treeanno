package de.nilsreiter.pipeline.uima.ocr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMyStringTokenizer {
	@Test
	public void test() {
		String s = "but one,  and \"had almost reduced that to capit-";

		MyStringTokenizer mst = new MyStringTokenizer(s);
		String test;

		test = mst.next();
		assertEquals("but", test);
		assertEquals(0, mst.getBegin());
		assertEquals(3, mst.getEnd());

		test = mst.next();
		assertEquals("one", test);
		assertEquals(4, mst.getBegin());
		assertEquals(7, mst.getEnd());

		test = mst.next();
		assertEquals(",", test);
		assertEquals(7, mst.getBegin());
		assertEquals(8, mst.getEnd());

		test = mst.next();
		assertEquals("and", test);
		assertEquals(10, mst.getBegin());
		assertEquals(13, mst.getEnd());

		test = mst.next();
		assertEquals("\"", test);
		assertEquals(14, mst.getBegin());
		assertEquals(15, mst.getEnd());

	}
}
