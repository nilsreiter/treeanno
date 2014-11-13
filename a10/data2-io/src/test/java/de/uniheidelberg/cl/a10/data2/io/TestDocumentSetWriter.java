package de.uniheidelberg.cl.a10.data2.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.data2.impl.DocumentSet_impl;

public class TestDocumentSetWriter {
	DocumentSet ds;

	@Before
	public void setUp() throws Exception {
		Document[] docs = new Document[2];
		for (int i = 0; i < docs.length; i++) {
			docs[i] = mock(Document.class);
			when(docs[i].getId()).thenReturn("doc" + i);
		}
		ds = new DocumentSet_impl("ds1", docs);

	}

	@Test
	public void test() throws IOException {
		DocumentSetWriter dsw = new DocumentSetWriter(System.out);
		dsw.write(ds);
	}

}
