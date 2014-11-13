package de.uniheidelberg.cl.a10.data2;

import java.util.ArrayList;


public interface Section extends Iterable<Sentence>, AnnotationObjectInDocument {

	ArrayList<Sentence> getSentences();

	String getRitualText();

}