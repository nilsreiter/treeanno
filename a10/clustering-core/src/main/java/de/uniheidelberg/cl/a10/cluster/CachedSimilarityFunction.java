package de.uniheidelberg.cl.a10.cluster;

import java.io.File;
import java.io.IOException;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.io.DocumentSimilarityReader;
import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;

public class CachedSimilarityFunction implements
		IDocumentSimilarityFunction<Document, Probability> {
	Matrix<Document, Document, Probability> matrix = null;

	public CachedSimilarityFunction(final File dataDirectory,
			final File cacheFile) throws IOException {
		DocumentSimilarityReader dsr = new DocumentSimilarityReader(
				dataDirectory);
		dsr.setDefaultValue(Probability.NULL);
		matrix = dsr.read(cacheFile);
	}

	@Override
	public Probability getSimilarity(final Document rd1, final Document rd2) {
		if (rd1 == rd2)
			return Probability.ONE;
		return matrix.get(rd1, rd2);
	}

}
