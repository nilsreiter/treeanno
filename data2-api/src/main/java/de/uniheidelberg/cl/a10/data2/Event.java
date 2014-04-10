package de.uniheidelberg.cl.a10.data2;

import de.uniheidelberg.cl.a10.HasId;
import de.uniheidelberg.cl.a10.HasTarget;

public interface Event extends HasTarget, HasId, AnnotationObjectInDocument {
	Source source();

	public enum Source {
		Frame, Token
	};

	Frame getFrame();

	double position();

}
