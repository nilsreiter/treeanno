package de.nilsreiter.event;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;

public interface EventFactory {
	Event makeEvent(AnnotationObjectInDocument source);
}