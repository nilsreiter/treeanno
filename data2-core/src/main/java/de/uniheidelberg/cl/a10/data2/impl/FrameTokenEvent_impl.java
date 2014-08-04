package de.uniheidelberg.cl.a10.data2.impl;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.data2.Frame;
import de.uniheidelberg.cl.a10.data2.FrameTokenEvent;
import de.uniheidelberg.cl.a10.data2.Token;

@Deprecated
public abstract class FrameTokenEvent_impl extends
		AnnotationObjectInDocument_impl implements FrameTokenEvent {

	Token target = null;

	/**
	 * The relative position of the event in the document. Should be between 0.0
	 * and 1.0. If not set or unordered, -1.0.
	 */
	double position = -1.0;

	public FrameTokenEvent_impl(final String id, final Token target) {
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

	public static FrameTokenEvent getEvent(final HasId a) {
		if (a instanceof FrameTokenEvent) {
			return (FrameTokenEvent) a;
		}
		if (a instanceof Frame) {
			return new FrameEvent_impl((Frame) a);
		}
		return null;
	}

}
