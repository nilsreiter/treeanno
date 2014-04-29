package de.nilsreiter.event.impl;

import de.nilsreiter.event.EventFactory;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameElement;
import de.uniheidelberg.cl.a10.data2.impl.Event_impl;

public class FrameEventFactory implements EventFactory {

	static int idCounter = 0;

	@Override
	public Event makeEvent(AnnotationObjectInDocument anchor) {
		Event_impl event = new Event_impl("ev" + idCounter++, anchor);

		Frame frame = (Frame) anchor;
		event.setEventClass(frame.getFrameName());
		for (FrameElement fe : frame.getFrameElms()) {
			event.putArgument(fe.getName(), fe.getTokens());
		}

		return event;
	}

	public static Event event(AnnotationObjectInDocument anchor) {
		Event_impl event = new Event_impl("ev" + idCounter++, anchor);

		Frame frame = (Frame) anchor;
		event.setEventClass(frame.getFrameName());
		for (FrameElement fe : frame.getFrameElms()) {
			event.putArgument(fe.getName(), fe.getTokens());
		}

		return event;
	}
}