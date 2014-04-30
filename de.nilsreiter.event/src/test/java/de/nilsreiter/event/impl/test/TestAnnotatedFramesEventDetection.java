package de.nilsreiter.event.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.AnnotatedFramesEventDetection;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestAnnotatedFramesEventDetection {

	Document text;
	BasicEventDetection bed;

	@Before
	public void setUp() throws Exception {
		URL url = this.getClass().getResource("/test_docs/r0010.xml");
		File testWsdl = new File(url.getFile());

		DataReader dr = new DataReader();

		text = dr.read(testWsdl);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		BasicEventDetection bed = new BasicEventDetection(
				new AnnotatedFramesEventDetection(), new FrameEventFactory());
		bed.detectEvents(text);
		assertEquals(62, text.getFrames().size());
		assertEquals(26, text.getEvents().size());
		for (Event event : text.getEvents()) {
			assertFalse("People_by_religion".equalsIgnoreCase(event
					.getEventClass()));
		}
	}

}
