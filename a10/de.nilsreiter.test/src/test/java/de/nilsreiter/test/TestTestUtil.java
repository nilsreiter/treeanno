package de.nilsreiter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;

public class TestTestUtil {
	@Test
	public void testGetDocumentWithEvents() {
		Document doc = TestUtil.getDocumentWithEvents("doc0", 10);
		assertNotNull(doc);
		assertEquals("doc0", doc.getId());
		assertNotNull(doc.getEvents());
		assertEquals(10, doc.getEvents().size());
		for (int i = 0; i < 10; i++) {
			Event ev = doc.getEvents().get(i);
			assertNotNull(ev);
			assertNotNull(ev.getRitualDocument());
			assertEquals(doc, ev.getRitualDocument());
			assertEquals("ev" + i, ev.getId());
		}
	}
}
