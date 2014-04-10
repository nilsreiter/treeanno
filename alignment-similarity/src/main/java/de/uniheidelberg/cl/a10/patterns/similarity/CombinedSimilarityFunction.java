package de.uniheidelberg.cl.a10.patterns.similarity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniheidelberg.cl.a10.patterns.data.PMath;
import de.uniheidelberg.cl.a10.patterns.data.Probability;

/**
 * This similarity function allows an averaged combination of multiple
 * similarity functions.
 * 
 * @author reiter
 * 
 * @param <T>
 */
public class CombinedSimilarityFunction<T> extends
		AbstractSimilarityFunction<T> {

	Operation operation = Operation.AVG;
	List<SimilarityFunction<T>> features = null;
	List<Double> weights = null;

	// Map<BasicPair<T, T>, Probability> history = new HashMap<BasicPair<T, T>,
	// Probability>();

	public CombinedSimilarityFunction() {
		features = new LinkedList<SimilarityFunction<T>>();
		weights = new LinkedList<Double>();
		useHistory = true;
	}

	public CombinedSimilarityFunction(final SimilarityFunction<T>... fs) {
		features = new LinkedList<SimilarityFunction<T>>();
		weights = new LinkedList<Double>();
		for (SimilarityFunction<T> f : fs) {
			features.add(f);
			weights.add(1.0);
		}
		useHistory = true;
	}

	public CombinedSimilarityFunction(final Operation oper,
			final SimilarityFunction<T>... fs) {
		features = new LinkedList<SimilarityFunction<T>>();
		weights = new LinkedList<Double>();
		for (SimilarityFunction<T> f : fs) {
			features.add(f);
			weights.add(1.0);
		}
		useHistory = true;
		this.operation = oper;
	}

	public void addFeature(final SimilarityFunction<T> sf, final double weight) {
		this.features.add(sf);
		this.weights.add(weight);
	}

	public void addFeature(final SimilarityFunction<T> sf) {
		this.features.add(sf);
		this.weights.add(1.0);
	}

	@Override
	public synchronized Probability sim(final T arg0, final T arg1) {
		if (features.size() == 0)
			return Probability.NULL;
		if (arg0 == null || arg1 == null)
			return Probability.NULL;

		Probability p = this.getFromHistory(arg0, arg1);
		if (p != null)
			return p;

		List<Probability> ps = new ArrayList<Probability>();
		for (int i = 0; i < features.size(); i++) {
			for (int j = 0; j < weights.get(i); j++) {
				try {
					ps.add(features.get(i).sim(arg0, arg1));
				} catch (IncompatibleException e) {
					// If a function is not compatible, we silently catch the
					// exception
				}
			}
		}
		if (ps.size() == 0) {
			return Probability.NULL;
		}
		p = this.combine(ps);
		this.putInHistory(arg0, arg1, p);

		return p;
	}

	private Probability combine(final List<Probability> ps) {
		switch (operation) {
		case GEO:
			return PMath.geometricMean(ps);
		case HARM:
			return PMath.harmonicMean(ps);
		case MULT:
			return PMath.multiply(ps);
		default:
			return PMath.arithmeticMean(ps);
		}

	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(final Operation operation) {
		if (this.operation != operation) {
			clear();
		}
		this.operation = operation;
	}

	@Override
	public String toString() {
		return super.toString() + "-" + this.operation;
	}

	@Override
	public void readConfiguration(final SimilarityConfiguration tc) {
		super.readConfiguration(tc);
		for (SimilarityFunction<? extends T> sf : features) {
			sf.readConfiguration(tc);
		}
	}
}
