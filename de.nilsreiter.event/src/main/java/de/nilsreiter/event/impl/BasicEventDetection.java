package de.nilsreiter.event.impl;

import de.nilsreiter.event.EventFactory;
import de.nilsreiter.event.GlobalEventDetection;
import de.nilsreiter.event.LocalEventDetector;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.data2.HasTokens;

public class BasicEventDetection implements GlobalEventDetection {

	LocalEventDetector localEventDetector;

	EventFactory eventFactory;

	public BasicEventDetection(LocalEventDetector localEventDetector,
			EventFactory eventFactory) {
		super();
		this.localEventDetector = localEventDetector;
		this.eventFactory = eventFactory;
	}

	@Override
	public void detectEvents(Document document) {
		for (AnnotationObjectInDocument aoi : document
				.getAnnotations(localEventDetector.typeRestrictor())) {
			if (localEventDetector.isEvent(aoi)) {
				document.addEvent(eventFactory.makeEvent((HasTokens) aoi));
			}
		}
	}

	public LocalEventDetector getLocalEventDetector() {
		return localEventDetector;
	}

	public void setLocalEventDetector(LocalEventDetector localEventDetector) {
		this.localEventDetector = localEventDetector;
	}

	public EventFactory getEventFactory() {
		return eventFactory;
	}

	public void setEventFactory(EventFactory eventFactory) {
		this.eventFactory = eventFactory;
	}
}