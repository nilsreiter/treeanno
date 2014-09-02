package de.uniheidelberg.cl.a10.opennlp;

import opennlp.tools.chunker.ChunkerContextGenerator;

public interface A10ChunkerContextGenerator extends ChunkerContextGenerator {
	public String[] getContext(int i, String[][] features);
}
