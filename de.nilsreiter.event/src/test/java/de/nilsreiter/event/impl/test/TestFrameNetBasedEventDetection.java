package de.nilsreiter.event.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.nilsreiter.event.impl.FrameNetBasedEventDetection;
import de.saar.coli.salsa.reiter.framenet.FNDatabaseReader15;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestFrameNetBasedEventDetection {

	public static String fnPath = "/Users/reiterns/Documents/Resources/framenet-1.5";

	Document text;
	BasicEventDetection bed;

	Frame frame;

	@Before
	public void setUp() throws Exception {
		URL url = this.getClass().getResource("/test_docs/r0010.xml");
		File testWsdl = new File(url.getFile());

		DataReader dr = new DataReader();

		text = dr.read(testWsdl);

		FrameNet frameNet = new FrameNet();
		frameNet.readData(new FNDatabaseReader15(new File(fnPath), false));

		bed = new BasicEventDetection(new FrameNetBasedEventDetection(frameNet,
				"Event"), new FrameEventFactory());

		frame = mock(Frame.class);
		when(frame.getFrameName()).thenReturn("Cutting");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		bed.detectEvents(text);
		assertTrue(text.getFrames().size() > text.getEvents().size());
		assertEquals(34, text.getEvents().size());

	}

	@Test
	public void testMock() {
		assertTrue(bed.getLocalEventDetector().isEvent(frame));

	}

}
