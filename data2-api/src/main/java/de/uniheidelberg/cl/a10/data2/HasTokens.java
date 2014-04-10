package de.uniheidelberg.cl.a10.data2;

import java.util.List;

public interface HasTokens extends Iterable<Token>, AnnotationObjectInDocument {

	List<Token> getTokens();

	Token lastToken();

	Token firstToken();

	List<Token> getTokensBetween(final int end, final int begin);

	public abstract int numberOfTokens();

}