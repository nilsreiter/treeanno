package de.uniheidelberg.cl.a10.data2;

import java.util.List;

public interface Entity extends Iterable<Mention>, AnnotationObjectInDocument {

	Sense getSense();

	List<? extends Mention> getMentions();

}