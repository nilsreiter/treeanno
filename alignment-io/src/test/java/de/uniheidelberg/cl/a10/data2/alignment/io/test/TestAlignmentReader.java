package de.uniheidelberg.cl.a10.data2.alignment.io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.alignment.Alignment;
import de.uniheidelberg.cl.a10.data2.alignment.Link;
import de.uniheidelberg.cl.a10.data2.alignment.io.AlignmentReader;
import de.uniheidelberg.cl.a10.io.ResourceBasedStreamProvider;

public class TestAlignmentReader {

	AlignmentReader<Token> tar;

	DataStreamProvider dsp;

	@Before
	public void setUp() {
		dsp = new ResourceBasedStreamProvider();
		tar = new AlignmentReader<Token>(dsp);
	}

	@Test
	public void testTokenAlignmentReader() throws IOException {
		Alignment<Token> alignment =
				tar.read(dsp.findStreamFor("alignment1.xml"));
		assertNotNull(alignment);

		assertEquals(4, alignment.getAlignments().size());

		Iterator<? extends Link<Token>> iterator =
				alignment.getAlignments().iterator();

		// al0
		Link<Token> link = iterator.next();
		assertNotNull(link);
		assertEquals(2, link.getElements().size());
		assertEquals("al0", link.getId());
		Iterator<Token> tokenIterator = link.getElements().iterator();

		Token token = tokenIterator.next();
		assertEquals("t224", token.getId());
	}
}
