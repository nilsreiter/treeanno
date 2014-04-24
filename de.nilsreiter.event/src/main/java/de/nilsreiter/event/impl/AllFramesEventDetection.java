package de.nilsreiter.event.impl;

import de.nilsreiter.event.LocalEventDetector;
import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;
import de.uniheidelberg.cl.a10.data2.Frame;

public class AllFramesEventDetection implements LocalEventDetector {

	@Override
	public boolean isEvent(AnnotationObjectInDocument anchor) {
		return true;
	}

	@Override
	public Class<? extends AnnotationObjectInDocument> typeRestrictor() {
		return Frame.class;
	}
}