package de.uniheidelberg.cl.a10.data2.io;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.uniheidelberg.cl.a10.api.DataStreamProvider;
import de.uniheidelberg.cl.a10.data2.DocumentSet;
import de.uniheidelberg.cl.a10.io.ResourceBasedStreamProvider;

public class TestDocumentSetReader {
	DocumentSetReader dsr;
	DataStreamProvider streamProvider;

	@Before
	public void setUp() throws Exception {
		streamProvider = new ResourceBasedStreamProvider();
		dsr = new DocumentSetReader(streamProvider);
	}

	@Test
	public void test() throws IOException {
		DocumentSet ds = dsr.read(streamProvider
				.findStreamFor("test_docs/docset.xml"));
		assertNotNull(ds);
	}
}
