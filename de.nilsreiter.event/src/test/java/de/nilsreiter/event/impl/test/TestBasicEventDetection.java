package de.nilsreiter.event.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.GlobalEventDetection;
import de.nilsreiter.event.LocalEventDetector;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.io.DataReader;

public class TestBasicEventDetection {

	Document text;
	GlobalEventDetection bed;

	@Before
	public void setUp() throws Exception {
		URL url = this.getClass().getResource("/test_docs/document.xml");
		File testWsdl = new File(url.getFile());

		DataReader dr = new DataReader();

		text = dr.read(testWsdl);

		bed = new BasicEventDetection(new LocalEventDetector() {

			@Override
			public boolean isEvent(AnnotationObjectInDocument anchor) {
				return true;
			}

			@Override
			public Class<? extends AnnotationObjectInDocument> typeRestrictor() {
				return Frame.class;
			}
		}, new FrameEventFactory());

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		bed.detectEvents(text);
		assertNotNull(text.getEvents());
		assertEquals(text.getFrames().size(), text.getEvents().size());
		assertNotSame(text.getFrames().get(0), text.getEvents().get(0));
	}

}
