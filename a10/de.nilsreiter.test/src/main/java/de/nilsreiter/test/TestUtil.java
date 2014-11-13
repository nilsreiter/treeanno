package de.nilsreiter.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.Event;

public class TestUtil {
	public static Document[] getDocumentsWithEvents(int numberOfDocuments,
			int numberOfEventsPerDocument) {
		Document[] docs = new Document[numberOfDocuments];

		for (int i = 0; i < docs.length; i++) {
			docs[i] =
					getDocumentWithEvents("doc" + i, numberOfEventsPerDocument);
		}

		return docs;
	};

	public static Document getDocumentWithEvents(String documentId,
			int numberOfEvents) {
		Document doc = mock(Document.class);
		List<Event> events = new LinkedList<Event>();
		for (int i = 0; i < numberOfEvents; i++) {
			String id = "ev" + String.valueOf(i);
			Event event = mock(Event.class);
			when(event.getId()).thenReturn(id);
			when(event.getRitualDocument()).thenReturn(doc);
			when(doc.getById(id)).thenReturn(event);
			when(event.toString()).thenReturn(id);
			events.add(event);
		}
		when(doc.getId()).thenReturn(documentId);
		when(doc.getEvents()).thenReturn(events);
		return doc;
	}
}
