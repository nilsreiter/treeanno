package de.nilsreiter.event.similarity;

import java.sql.SQLException;

import de.uniheidelberg.cl.a10.patterns.similarity.SimilarityFunction;

public interface SimilarityDatabase<T> {

	double getSimilarity(Class<? extends SimilarityFunction> simType, T e1, T e2)
			throws SQLException;
}
