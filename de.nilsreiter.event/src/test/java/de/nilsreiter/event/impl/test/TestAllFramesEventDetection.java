package de.nilsreiter.event.impl.test;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.AllFramesEventDetection;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestAllFramesEventDetection {

	Document text;
	BasicEventDetection bed;

	@Before
	public void setUp() throws Exception {
		URL url = this.getClass().getResource("/test_docs/document.xml");
		File testWsdl = new File(url.getFile());

		DataReader dr = new DataReader();

		text = dr.read(testWsdl);

		bed = new BasicEventDetection(new AllFramesEventDetection(),
				new FrameEventFactory());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

	}

}
