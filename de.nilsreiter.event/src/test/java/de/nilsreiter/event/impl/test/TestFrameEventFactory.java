package de.nilsreiter.event.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.nilsreiter.event.EventFactory;
import de.nilsreiter.event.GlobalEventDetection;
import de.nilsreiter.event.impl.AllFramesEventDetection;
import de.nilsreiter.event.impl.BasicEventDetection;
import de.nilsreiter.event.impl.FrameEventFactory;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Token;
import de.uniheidelberg.cl.a10.data2.impl.Event_impl;
import de.uniheidelberg.cl.a10.data2.impl.FrameElm_impl;
import de.uniheidelberg.cl.a10.data2.impl.Frame_impl;
import de.uniheidelberg.cl.a10.data2.io.DataReader;
import de.uniheidelberg.cl.a10.data2.io.DataWriter;

public class TestFrameEventFactory {

	Frame_impl frame;

	FrameElm_impl[] fe;

	EventFactory factory;

	@Before
	public void setUp() throws Exception {
		fe = new FrameElm_impl[2];
		fe[0] = mock(FrameElm_impl.class);
		fe[1] = mock(FrameElm_impl.class);

		when(fe[0].getName()).thenReturn("Agent");
		when(fe[1].getName()).thenReturn("Patient");
		when(fe[0].getTokens()).thenReturn(Arrays.asList(mock(Token.class)));
		when(fe[1].getTokens()).thenReturn(
				Arrays.asList(mock(Token.class), mock(Token.class)));

		frame = new Frame_impl("test");

		frame.addFrameElm(fe[0]);
		frame.addFrameElm(fe[1]);

		factory = new FrameEventFactory();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Event event = factory.makeEvent(frame);

		// Testing basics
		assertNotNull(event);
		assertNotNull(event.getId());
		assertNotNull(event.getArguments());
		assertSame(frame, event.getAnchor());
		assertNull(event.getRitualDocument());

		// Testing arguments
		assertEquals(2, event.getArguments().size());
		assertEquals(1, event.getArguments().get("Agent").size());
		assertEquals(2, event.getArguments().get("Patient").size());

	}

	@Test
	public void testEvents() {
		Token token = mock(Token.class);
		Event_impl event = new Event_impl("ev0", token);
		event.putArgument("Subject", Arrays.asList(token));

		assertSame(token, event.getAnchor());
		assertEquals(1, event.getArguments().size());
		assertEquals(1, event.getArguments().get("Subject").size());
		assertSame(token, event.getArguments().get("Subject").get(0));
	}

	@Test
	public void doRun() throws IOException {
		URL url = this.getClass().getResource("/test_docs/document.xml");
		File testWsdl = new File(url.getFile());
		DataReader dr = new DataReader();
		Document doc = dr.read(testWsdl);
		GlobalEventDetection ged = new BasicEventDetection(
				new AllFramesEventDetection(), new FrameEventFactory());
		ged.detectEvents(doc);

		DataWriter dw = new DataWriter(new BufferedOutputStream(System.out));
		dw.write(doc);
	}

}
