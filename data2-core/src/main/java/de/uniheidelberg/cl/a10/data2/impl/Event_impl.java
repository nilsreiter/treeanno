package de.uniheidelberg.cl.a10.data2.impl;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Event;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.Token;

public abstract class Event_impl extends AnnotationObjectInDocument_impl
		implements Event {

	Token target = null;

	/**
	 * The relative position of the event in the document. Should be between 0.0
	 * and 1.0. If not set or unordered, -1.0.
	 */
	double position = -1.0;

	public Event_impl(final String id, final Token target) {
		super(id);
		this.target = target;
	}

	@Override
	public abstract Source source();

	/**
	 * @return the position
	 */

	@Override
	public double position() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(final double position) {
		this.position = position;
	}

	/**
	 * @return the target
	 */
	@Override
	public Token getTarget() {
		return target;
	}

	public static Event getEvent(final HasId a) {
		if (a instanceof Event) {
			return (Event) a;
		}
		if (a instanceof Frame) {
			return new FrameEvent_impl((Frame) a);
		}
		return null;
	}

}
