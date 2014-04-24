package de.nilsreiter.event;

import de.uniheidelberg.cl.a10.data2.AnnotationObjectInDocument;

public interface LocalEventDetector {
	public boolean isEvent(AnnotationObjectInDocument anchor);

	public Class<? extends AnnotationObjectInDocument> typeRestrictor();
}