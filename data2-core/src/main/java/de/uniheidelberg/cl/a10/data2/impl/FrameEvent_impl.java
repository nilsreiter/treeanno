package de.uniheidelberg.cl.a10.data2.impl;

import java.util.Set;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Frame;

@Deprecated
public class FrameEvent_impl extends FrameTokenEvent_impl {

	Frame frame = null;

	public FrameEvent_impl(final Frame frame) {
		super(frame.getId(), frame.getTarget());
		this.frame = frame;
		this.position =
				(double) frame.indexOf()
						/ (double) frame.getRitualDocument().getFrames().size();
		this.ritualDocument = frame.getRitualDocument();
	}

	@Override
	public int indexOf() {
		return frame.indexOf();
	}

	@Override
	public Source source() {
		return Source.Frame;
	}

	/**
	 * @return the frame
	 */
	@Override
	public Frame getFrame() {
		return frame;
	}

	@Override
	public Set<AnnotationObjectInDocument> getRelated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<AnnotationObjectInDocument> getClosure() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return this.getGlobalId();
	}

}
