package de.uniheidelberg.cl.a10.patterns.similarity;

import java.io.File;

import de.uniheidelberg.cl.a10.patterns.data.Probability;
import de.uniheidelberg.cl.a10.patterns.data.matrix.MapMatrix;

public abstract class AbstractSimilarityFunction<T> implements
		SimilarityFunction<T> {

	MapMatrix<Integer, Integer, Probability> history = new MapMatrix<Integer, Integer, Probability>();

	boolean saveHistory = false;

	boolean useHistory = true;

	static File cache = new File("similaritycache");

	boolean configured = false;

	protected SimilarityConfiguration config = null;

	public AbstractSimilarityFunction() {
		history.setDefaultValue(null);
	}

	boolean positivePreCheck(final T e1, final T e2) {
		return (e1 == e2);
	}

	boolean negativePreCheck(final T e1, final T e2) {
		return (e1 == null || e2 == null);

	}

	protected void putInHistory(final T arg0, final T arg1, final Probability p) {
		if (useHistory) {
			this.history.put(arg0.hashCode(), arg1.hashCode(), p);
			this.history.put(arg1.hashCode(), arg0.hashCode(), p);
		}
	}

	protected Probability getFromHistory(final T arg0, final T arg1) {
		if (useHistory)
			return this.history.get(arg0.hashCode(), arg1.hashCode());
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void readConfiguration(final SimilarityConfiguration tc) {
		this.config = tc;

	}

	public void clear() {
		this.history.clear();
	}

}
