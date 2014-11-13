package de.nilsreiter.event.impl;

import java.util.Collection;

import de.nilsreiter.event.LocalEventDetector;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;

public class FrameNetBasedEventDetection implements LocalEventDetector {

	FrameNet frameNet = null;

	String rootFrame = null;

	Frame referenceFrame;

	public FrameNetBasedEventDetection(FrameNet frameNet, String rootFrame)
			throws FrameNotFoundException {
		super();
		this.frameNet = frameNet;
		this.rootFrame = rootFrame;
		this.referenceFrame = frameNet.getFrame(rootFrame);
	}

	@Override
	public boolean isEvent(AnnotationObjectInDocument anchor) {
		de.uniheidelberg.cl.a10.data2.Frame frame = (de.uniheidelberg.cl.a10.data2.Frame) anchor;

		try {
			Frame fnFrame = frameNet.getFrame(frame.getFrameName());

			Collection<Frame> inh = fnFrame.allInheritedFrames();

			return inh.contains(this.referenceFrame);
		} catch (FrameNotFoundException e) {
			return false;
		}
	}

	@Override
	public Class<? extends AnnotationObjectInDocument> typeRestrictor() {
		return de.uniheidelberg.cl.a10.data2.Frame.class;
	}

}