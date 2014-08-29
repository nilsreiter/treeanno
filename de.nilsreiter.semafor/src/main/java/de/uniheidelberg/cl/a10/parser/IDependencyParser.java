package de.uniheidelberg.cl.a10.parser;

import de.uniheidelberg.cl.a10.data.BaseSentence;


public interface IDependencyParser {

	public boolean parse(BaseSentence sentence);
}
