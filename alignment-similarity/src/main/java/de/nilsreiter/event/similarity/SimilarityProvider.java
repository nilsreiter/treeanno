package de.nilsreiter.event.similarity;

import java.sql.SQLException;
import java.util.Map;

import de.uniheidelberg.cl.a10.data2.Document;
import de.uniheidelberg.cl.a10.patterns.data.matrix.Matrix;
import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface SimilarityProvider<T> {
	double getSimilarity(Class<? extends SimilarityFunction<T>> simType, T e1,
			T e2) throws SQLException;

	Map<String, Matrix<T, T, Double>> getSimilarities(Document doc1,
			Document doc2) throws SQLException;
}
