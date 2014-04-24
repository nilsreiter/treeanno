package de.uniheidelberg.cl.a10.cluster.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.cluster.WordOverlap;
import de.uniheidelberg.cl.a10.cluster.impl.WordOverlap_impl;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Token;

public class TestOverlap {

	Document[] docs = null;

	@Before
	public void setUp() throws Exception {
		docs = new Document[2];

		List<Token> t1 = Arrays.asList(mock(Token.class), mock(Token.class));
		List<Token> t2 = Arrays.asList(mock(Token.class), mock(Token.class));

		when(t1.get(0).getLemma()).thenReturn("the");
		when(t1.get(1).getLemma()).thenReturn("dog");

		when(t2.get(0).getLemma()).thenReturn("the");
		when(t2.get(1).getLemma()).thenReturn("cat");

		docs[0] = mock(Document.class);
		docs[1] = mock(Document.class);

		when(docs[0].getTokens()).thenReturn(t1);
		when(docs[1].getTokens()).thenReturn(t2);

	}

	@Test
	public void testOverlap() throws IOException {
		WordOverlap wo = new WordOverlap_impl();
		assertEquals(0.25, wo.getSimilarity(docs[0], docs[1]).getProbability(),
				0.0001);
	}
}
