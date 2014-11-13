package de.uniheidelberg.cl.a10.cluster.measure;

import de.uniheidelberg.cl.a10.data2.alignment.Alignment;

public interface DocumentSimilarityMeasure {
	double getDocumentSimilarity(Alignment<?> alignment);

	static enum Type {
		ONE, TWO, THREE
	}
}
