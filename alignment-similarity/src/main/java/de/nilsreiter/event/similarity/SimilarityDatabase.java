package de.nilsreiter.event.similarity;

import java.sql.SQLException;

public interface SimilarityDatabase<T> extends SimilarityProvider<T> {

	void rebuild() throws SQLException;

	void dropType(String s) throws SQLException;

	void putSimilarity(Class<? extends EventSimilarityFunction> class1,
			T event1, T event2, double probability) throws SQLException;

	void finish() throws SQLException;
}
